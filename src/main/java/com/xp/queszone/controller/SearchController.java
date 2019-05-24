package com.xp.queszone.controller;

import com.xp.queszone.model.EntityType;
import com.xp.queszone.model.Question;
import com.xp.queszone.model.ViewObject;
import com.xp.queszone.service.FollowService;
import com.xp.queszone.service.QuestionService;
import com.xp.queszone.service.SearchService;
import com.xp.queszone.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    SearchService searchService;

    @Autowired
    FollowService followService;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/search"}, method = {RequestMethod.POST, RequestMethod.GET})
    public String search(Model model,
                         @RequestParam("q") String keyword,
                         @RequestParam(value = "offset", defaultValue = "0") int offset,
                         @RequestParam(value = "count", defaultValue = "10") int count) {
        try {
            List<Question> questionList = searchService.searchQuestion(keyword, offset, count, "<em>", "</em>");
            List<ViewObject> vos = new ArrayList<>();
            for (Question question : questionList) {
                Question q = questionService.selectById(question.getId());
                ViewObject vo = new ViewObject();
                if (null != question.getTitle()) {
                    q.setTitle(question.getTitle());
                }
                if (null != question.getContent()) {
                    q.setTitle(question.getContent());
                }
                vo.set("question",q);
                vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
                vo.set("user", userService.getUser(q.getUserId()));
                vos.add(vo);
            }
            model.addAttribute("vos",vos);
        } catch (Exception e) {
            logger.error("solr检索异常"+e.getMessage());
        }
        return "result";
    }
}
