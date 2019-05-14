package com.xp.queszone.controller;

import com.xp.queszone.model.Comment;
import com.xp.queszone.model.EntityType;
import com.xp.queszone.model.HostHolder;
import com.xp.queszone.service.CommentService;
import com.xp.queszone.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class CommentController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    QuestionService questionService;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/addComment"}, method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content,
                             HttpServletRequest request) {
        Comment comment = new Comment();
        comment.setContent(content);
        if (null != hostHolder.getUser()) {
            comment.setUserId(hostHolder.getUser().getId());
        } else {
            String url = request.getRequestURL().toString();
            return "redirect:/reglogin?next=/question/"+questionId;
        }
        comment.setCreatedDate(new Date());
        comment.setEntityType(EntityType.ENTITY_QUESTION);
        comment.setEntityId(questionId);
        commentService.addComment(comment);
        int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
        questionService.updateCommentCount(comment.getEntityId(), count);
        return "redirect:/question/"+questionId;
    }
}
