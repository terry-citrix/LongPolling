package com.longpoll.service.thread.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class MessageQueueImpl implements MessageQueue {

    private Map<String, List<Object>> lockMap = new HashMap<String, List<Object>>();
    private Map<String, String> messageMap = Collections.synchronizedMap(new HashMap<String, String>());

    @Override
    public void storeLock(String groupId, Object lock) {
        if (!lockMap.containsKey(groupId)) {
            List<Object> locks = new ArrayList<Object>();
            lockMap.put(groupId, locks);
        }

        List<Object> locks = lockMap.get(groupId);
        if (locks == null) {
            locks = new ArrayList<Object>();
        }

        locks.add(lock);

    }

    @Override
    public void pushMessage(String groupId, String value) {
        messageMap.put(groupId, value);

        // Now wake up all callers that were waiting on a new message. 
        List<Object> locks = lockMap.get(groupId);
        if (locks != null) {
            locks.stream().forEach(lock -> {
                    synchronized(lock) {
                        lock.notifyAll();
                    }
                }
            );
        }
    }

    @Override
    public String getMessage(String groupId) {
        return messageMap.get(groupId);
    }
    
}
