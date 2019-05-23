package com.xp.queszone.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.xp.queszone.async.EventHandler;
import com.xp.queszone.async.EventModel;
import com.xp.queszone.async.EventType;
import com.xp.queszone.model.EntityType;
import com.xp.queszone.model.Feed;
import com.xp.queszone.model.Question;
import com.xp.queszone.model.User;
import com.xp.queszone.service.FeedService;
import com.xp.queszone.service.FollowService;
import com.xp.queszone.service.QuestionService;
import com.xp.queszone.service.UserService;
import com.xp.queszone.util.JedisAdapter;
import com.xp.queszone.util.RedisKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FeedHandler implements EventHandler {

    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    @Autowired
    FeedService feedService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    QuestionService questionService;

    private String buildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<>();
        // 触发用户是通用的
        User actor = userService.getUser(model.getActorId());
        if (actor == null) {
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());

        if (model.getEventType() == EventType.COMMENT ||
                (model.getEventType() == EventType.FOLLOW  && model.getEntityType() == EntityType.ENTITY_QUESTION)) {
            Question question = questionService.selectById(model.getEntityId());
            if (question == null) {
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());

            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandle(EventModel eventModel) {
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(eventModel.getActorId());
        feed.setType(eventModel.getEventType().getValue());
        feed.setData(buildFeedData(eventModel));
        if (null == feed.getData()) {
            return;
        }
        feedService.addFeed(feed);

        // 获得所有粉丝
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER, eventModel.getActorId(), Integer.MAX_VALUE);
        // 系统队列
        followers.add(0);
        // 给所有粉丝推事件
        for (int follower : followers) {
            String timelineKey = RedisKeyGenerator.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
            /**
             * TODO 限制最长长度，如果timelineKey的长度过大，就删除后面的新鲜事
             */
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }
}
