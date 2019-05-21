package com.xp.queszone.controller;

import com.xp.queszone.async.EventModel;
import com.xp.queszone.async.EventProducer;
import com.xp.queszone.async.EventType;
import com.xp.queszone.model.Comment;
import com.xp.queszone.model.EntityType;
import com.xp.queszone.model.HostHolder;
import com.xp.queszone.model.User;
import com.xp.queszone.service.CommentService;
import com.xp.queszone.service.LikeService;
import com.xp.queszone.util.QuesZoneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/like"}, method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        User user = hostHolder.getUser();
        if (null == user) {
            return QuesZoneUtil.getJSONString(999);
        }
        long likeCount = likeService.like(user.getId(), EntityType.ENTITY_COMMENT, commentId);
        Comment comment = commentService.getCommentById(commentId);
        EventModel eventModel = new EventModel();
        eventModel.setEventType(EventType.LIKE)
                .setActorId(user.getId())
                .setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT)
                .setEntityOwnerId(comment.getUserId())
                .setExt("questionId",String.valueOf(comment.getEntityId()));
        eventProducer.fireEvent(eventModel);
        return QuesZoneUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = RequestMethod.POST)
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        User user = hostHolder.getUser();
        if (null == user) {
            return QuesZoneUtil.getJSONString(999);
        }
        long likeCount = likeService.dislike(user.getId(), EntityType.ENTITY_COMMENT, commentId);
        return QuesZoneUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
