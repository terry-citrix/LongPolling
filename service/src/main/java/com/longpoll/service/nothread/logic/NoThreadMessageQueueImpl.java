package com.longpoll.service.nothread.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.stereotype.Component;

@Component
public class NoThreadMessageQueueImpl implements NoThreadMessageQueue {

    private Map<String, List<ContextInfo>> contextMap = Collections.synchronizedMap(new HashMap<String, List<ContextInfo>>());
    private Map<String, String> messageMap = Collections.synchronizedMap(new HashMap<String, String>());

    @Override
    public void pushMessage(String groupId, String value) {
        messageMap.put(groupId, value);

        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        SortedSet<String> sortedThreadSet = new TreeSet<String>();
        threadSet.stream().forEach(thread -> sortedThreadSet.add(thread.getName()));
        
        System.out.println("List of active threads (" + sortedThreadSet.size() + "):");
        sortedThreadSet.stream().sorted().forEachOrdered(name -> {
            System.out.println("   " + name);
        });

        // Now return the message in all of the AsyncContexts that we're tracking. 
        List<ContextInfo> contexts = contextMap.get(groupId);
        if (contexts != null) {
            // Create a local copy.
            List<ContextInfo> localContexts = new ArrayList<ContextInfo>();
            contexts.stream().forEach(contextInfo -> localContexts.add(contextInfo));

            // Remove each instance in our local copy from the non-local member variable.
            localContexts.stream().forEach(contextInfo -> contexts.remove(contextInfo));

            // Now, on each local instance, complete the HttpResponse with the message.
            localContexts.stream().forEach(contextInfo -> {
                // Uses a new thread to return the response.
                contextInfo.context.start(new CompleteResponse(contextInfo, this, groupId));
            });
        }
    }

    public String getMessage(String groupId) {
        return messageMap.get(groupId);
    }

    @Override
    public void storeContext(String groupId, ContextInfo contextInfo) {
        if (!contextMap.containsKey(groupId)) {
            List<ContextInfo> contexts = new ArrayList<ContextInfo>();
            contextMap.put(groupId, contexts);
        }

        List<ContextInfo> contexts = contextMap.get(groupId);
        if (contexts == null) {
            contexts = new ArrayList<ContextInfo>();
            contextMap.put(groupId, contexts);
        }

        contexts.add(contextInfo);
    }

}