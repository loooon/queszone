package com.xp.queszone.service;

import com.xp.queszone.dao.MessageDao;
import com.xp.queszone.model.Message;
import com.xp.queszone.model.MessageSummary;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageDao messageDao;

    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message) {
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDao.addMessage(message) > 0 ? message.getId() : 0 ;
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDao.getConversationDetail(conversationId, offset, limit);
    }

    public List<MessageSummary> getConversationList(int userId, int offset, int limit) {
        return messageDao.getConversationList(userId, offset, limit);
    }

    public int getConvesationUnreadCount(int userId, String conversationId) {
        return messageDao.getConvesationUnreadCount(userId, conversationId);
    }

    public int updateMessageStatus(String conversationId) {
        return messageDao.updateMessageStatus(conversationId);
    }
}
