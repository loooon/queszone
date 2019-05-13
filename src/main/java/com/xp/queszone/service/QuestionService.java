package com.xp.queszone.service;

import com.xp.queszone.dao.QuestionDao;
import com.xp.queszone.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    @Autowired
    SensitiveService sensitiveService;

    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDao.selectLatestQuestions(userId, offset, limit);
    }

    public int addQuestion(Question question) {
        //对提问问题中的html标签进行过滤转义
        question.setContent(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        //对提问中的敏感词进行过滤
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));

        return questionDao.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public Question selectById(int id) {
        return questionDao.selectById(id);
    }
}
