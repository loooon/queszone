package com.xp.queszone.controller;

import com.xp.queszone.async.EventModel;
import com.xp.queszone.async.EventProducer;
import com.xp.queszone.async.EventType;
import com.xp.queszone.model.*;
import com.xp.queszone.service.CommentService;
import com.xp.queszone.service.FollowService;
import com.xp.queszone.service.QuestionService;
import com.xp.queszone.service.UserService;
import com.xp.queszone.util.QuesZoneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FollowController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/followUser"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public  String followUser(@RequestParam("userId") int userId) {
        if (null == hostHolder.getUser()) {
            return QuesZoneUtil.getJSONString(999);
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        EventModel eventModel = new EventModel();
        eventModel.setActorId(hostHolder.getUser().getId())
                .setEntityOwnerId(userId)
                .setEventType(EventType.FOLLOW)
                .setEntityType(EntityType.ENTITY_USER)
                .setEntityOwnerId(userId);

        eventProducer.fireEvent(eventModel);
        return QuesZoneUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = {"/unfollowUser"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public  String unfollowUser(@RequestParam("userId") int userId) {
        if (null == hostHolder.getUser()) {
            return QuesZoneUtil.getJSONString(999);
        }
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        return QuesZoneUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = {"/followQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId") int questionId) {
        if (null == hostHolder.getUser()) {
            return QuesZoneUtil.getJSONString(999);
        }
        Question question = questionService.selectById(questionId);
        if (null == question) {
            return QuesZoneUtil.getJSONString(1,"问题不存在");
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);

        EventModel eventModel = new EventModel();
        eventModel.setEventType(EventType.FOLLOW)
                .setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId)
                .setEntityType(EntityType.ENTITY_QUESTION)
                .setEntityOwnerId(question.getUserId());
        eventProducer.fireEvent(eventModel);

        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return QuesZoneUtil.getJSONString(ret ? 0 : 1, info);
    }

    @RequestMapping(path = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        if (null == hostHolder.getUser()) {
            return QuesZoneUtil.getJSONString(999);
        }
        Question question = questionService.selectById(questionId);
        if (null == question) {
            return QuesZoneUtil.getJSONString(1,"问题不存在");
        }
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, questionId);

        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));

        return QuesZoneUtil.getJSONString(ret ? 0 : 1, info);
    }

    @RequestMapping(path = {"/user/{uid}/followees"}, method = RequestMethod.GET)
    public String followees(Model model, @PathVariable("uid") int userId) {
        List<Integer> followeeIds = followService.getFollowees(userId, EntityType.ENTITY_USER, 0, 10);
        if (null != hostHolder.getUser()) {
            model.addAttribute("followees", getUserInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUserInfo(0, followeeIds));
        }
        model.addAttribute("curUser", userService.getUser(userId));
        model.addAttribute("followeeCount",followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        return "followees";
    }

    @RequestMapping(path = {"/user/{uid}/followers"}, method = RequestMethod.GET)
    public String followers(Model model, @PathVariable("uid") int userId) {
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId,0, 10);
        if (null != hostHolder.getUser()) {
            model.addAttribute("followers", getUserInfo(hostHolder.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUserInfo(0, followerIds));
        }
        model.addAttribute("curUser", userService.getUser(userId));
        model.addAttribute("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        return "followers";
    }

    private List<ViewObject> getUserInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<>();
        for (Integer uid : userIds) {
            User user = userService.getUser(uid);
            if (null == user) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER,uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            if (0 != localUserId) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            vo.set("commentCount", commentService.getUserCommentCount(uid));
            userInfos.add(vo);
        }
        return userInfos;
    }
}
