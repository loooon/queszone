package com.xp.queszone.async.handler;

import com.xp.queszone.async.EventHandler;
import com.xp.queszone.async.EventModel;
import com.xp.queszone.async.EventType;
import com.xp.queszone.model.Message;
import com.xp.queszone.model.User;
import com.xp.queszone.service.MessageService;
import com.xp.queszone.service.UserService;
import com.xp.queszone.util.QuesZoneUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel eventModel) {
        Message message = new Message();
        message.setFromId(QuesZoneUtil.SYSTEM);
        message.setToId(eventModel.getEntityOwnerId());
        message.setCreatedDate(new Date());

        User user = userService.getUser(eventModel.getActorId());
        message.setContent(user.getName()+"赞了你的评论"+"http://localhost:8080/question/"+eventModel.getExt("questionId"));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
//        List<EventType> eventTypes = new ArrayList<>();
//        eventTypes.add(EventType.LIKE);
//        return eventTypes;
        return Arrays.asList(EventType.LIKE);
    }
}
