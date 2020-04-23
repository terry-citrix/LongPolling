package com.longpoll.service.nothread.logic;

public interface NoThreadMessageQueue {

    void pushMessage(String groupId, String value);

    void storeContext(String groupId, ContextInfo contextInfo);

    String getMessage(String groupId);
}
