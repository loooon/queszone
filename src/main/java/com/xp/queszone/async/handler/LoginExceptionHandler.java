package com.xp.queszone.async.handler;

import com.xp.queszone.async.EventHandler;
import com.xp.queszone.async.EventModel;
import com.xp.queszone.async.EventType;
import com.xp.queszone.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginExceptionHandler implements EventHandler {

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel eventModel) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", eventModel.getExt("username"));
        mailSender.sendWithHTMLTemplate(eventModel.getExt("email"), "登录IP异常", "mails/login_exception.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
