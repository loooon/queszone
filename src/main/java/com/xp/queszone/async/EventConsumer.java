package com.xp.queszone.async;

import com.alibaba.fastjson.JSON;
import com.xp.queszone.util.JedisAdapter;
import com.xp.queszone.util.RedisKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EventConsumer implements InitializingBean, ApplicationContextAware {

    private final static Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        //获取spring容器中所有实现了EventHandler的实现类
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (null != beans) {
            for (String key : beans.keySet()) {
                EventHandler eventHandler = beans.get(key);
                List<EventType> eventTypes = eventHandler.getSupportEventTypes();
                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(eventHandler);
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String key = RedisKeyGenerator.getEventQueueKey();
                    List<String> events = jedisAdapter.brpop(0, key);
                    for (String message : events) {
                        if (message.equals(key)) {
                            continue;
                        }
                        EventModel eventModel  = JSON.parseObject(message, EventModel.class);
                        if (!config.containsKey(eventModel.getEventType())) {
                            logger.error("不能识别的事件");
                        } else {
                            List<EventHandler> eventHandlers = config.get(eventModel.getEventType());
                            for (EventHandler eventHandler : eventHandlers) {
                                eventHandler.doHandle(eventModel);
                            }
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
