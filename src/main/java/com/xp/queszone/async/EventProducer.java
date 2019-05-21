package com.xp.queszone.async;

import com.alibaba.fastjson.JSONObject;
import com.xp.queszone.util.JedisAdapter;
import com.xp.queszone.util.RedisKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {
        try {
            String eventJson = JSONObject.toJSONString(eventModel);
            String eventQueueKey = RedisKeyGenerator.getEventQueueKey();
            jedisAdapter.lpush(eventQueueKey,eventJson);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
