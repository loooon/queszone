package com.xp.queszone.controller;

import com.xp.queszone.model.*;
import com.xp.queszone.service.CommentService;
import com.xp.queszone.service.FollowService;
import com.xp.queszone.service.QuestionService;
import com.xp.queszone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/","/index"}, method = RequestMethod.GET)
    public String index(Model model) {
        List<ViewObject> viewObjects = generateViewObjects(0,0,10);
        model.addAttribute("vos",viewObjects);
        return "index";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        List<ViewObject> viewObjects = generateViewObjects(userId,0,10);
        model.addAttribute("vos",viewObjects);
        User user = userService.getUser(userId);
        ViewObject vo = new ViewObject();
        vo.set("user", user);
        vo.set("commentCount", commentService.getUserCommentCount(userId));
        vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        vo.set("followeeCount", followService.getFolloweeCount(userId, EntityType.ENTITY_USER));
        if (hostHolder.getUser() != null) {
            vo.set("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId));
        } else {
            vo.set("followed", false);
        }
        model.addAttribute("profileUser", vo);
        return "profile";
    }

    private List<ViewObject> generateViewObjects(int userId, int offset, int limit) {
        List<Question> questions = questionService.getLatestQuestions(userId, offset, limit);
        List<ViewObject> viewObjects = new ArrayList<>();
        for (Question question: questions) {
            ViewObject viewObject = new ViewObject();
            viewObject.set("question",question);
            viewObject.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
            viewObject.set("user",userService.getUser(question.getUserId()));
            viewObjects.add(viewObject);
        }
        return viewObjects;
    }
}
