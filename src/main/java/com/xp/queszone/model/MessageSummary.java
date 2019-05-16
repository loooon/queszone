package com.xp.queszone.model;

import lombok.Data;

import java.util.Date;

@Data
public class MessageSummary {
    private int id;
    private int fromId;
    private int toId;
    private String content;
    private Date createdDate;
    private int hasRead;
    private String conversationId;
    private int cnt;

    public String getConversationId() {
        if (fromId < toId) {
            return String.format("%d_%d",fromId,toId);
        } else {
            return String.format("%d_%d",toId,fromId);
        }
    }
}
