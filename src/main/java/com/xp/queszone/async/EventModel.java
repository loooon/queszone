package com.xp.queszone.async;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EventModel {

    private EventType eventType;
    private int actorId;
    private int entityType;
    private int entityId;
    private int entityOwnerId;
    private Map<String, String> exts = new HashMap<>();

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public String getExt(String key) {
        return exts.get(key);
    }
}
