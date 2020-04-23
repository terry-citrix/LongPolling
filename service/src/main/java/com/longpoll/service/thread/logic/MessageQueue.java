package com.longpoll.service.thread.logic;

public interface MessageQueue {

    final static String GROUP1 = "group1";

    void pushMessage(String groupId, String value);

    String getMessage(String groupId);

    void storeLock(String groupId, Object lock);

}
