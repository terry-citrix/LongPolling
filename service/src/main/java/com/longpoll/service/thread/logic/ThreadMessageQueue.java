package com.longpoll.service.thread.logic;

public interface ThreadMessageQueue {

    void pushMessage(String groupId, String value);

    String getMessage(String groupId);

    void storeLock(String groupId, Object lock);

}
