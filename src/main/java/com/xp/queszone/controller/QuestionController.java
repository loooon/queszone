package com.xp.queszone.controller;

import com.xp.queszone.model.HostHolder;
import com.xp.queszone.model.Question;
import com.xp.queszone.service.QuestionService;
import com.xp.queszone.service.UserService;
import com.xp.queszone.util.QuesZoneUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/question/add", method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {
        try {
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if (null == hostHolder.getUser()) {
                question.setUserId(QuesZoneUtil.ANONYMOUS_USERID);
            } else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if (questionService.addQuestion(question) > 0) {
                return QuesZoneUtil.getJSONString(0);
            }
        } catch (Exception e) {
          logger.error("提问失败"+e.getMessage());
        }
        return QuesZoneUtil.getJSONString(1,"失败");
    }

    @RequestMapping(value = "/question/{qid}")
    public String questionDetail(Model model,
                                 @PathVariable("qid") int qid) {
        Question question = questionService.selectById(qid);
        model.addAttribute("question",question);
        model.addAttribute("user",userService.getUser(question.getUserId()));
        return "detail";
    }
}
