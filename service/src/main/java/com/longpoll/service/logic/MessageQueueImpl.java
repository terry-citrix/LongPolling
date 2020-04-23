package com.longpoll.service.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class MessageQueueImpl implements MessageQueue {

    private Map<String, Object> lockMap = new HashMap<String, Object>();
    private Map<String, String> messageMap = Collections.synchronizedMap(new HashMap<String, String>());

    @Override
    public Object getLock(String groupId) {
        if (lockMap.containsKey(groupId)) {
            return lockMap.get(groupId);
        } else {
            Object lock = new Object();
            lockMap.put(groupId, lock);
            return lock;
        }
    }

    @Override
    public void pushMessage(String groupId, String value) {
        messageMap.put(groupId, value);

        // Now wake up all theads that were waiting on a new message. 
        Object lock = lockMap.get(groupId);
        if (lock != null) {
            synchronized(lock) {
                lock.notifyAll();
            }
        }
    }

    @Override
    public String getMessage(String groupId) {
        return messageMap.get(groupId);
    }
    
}
