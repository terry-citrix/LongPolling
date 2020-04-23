package com.longpoll.service.logic;

public interface MessageQueue {

    final static String GROUP1 = "group1";

    void pushMessage(String groupId, String value);

    String getMessage(String groupId);

    Object getLock(String groupId);

}
