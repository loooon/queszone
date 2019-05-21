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
public class FollowHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(QuesZoneUtil.SYSTEM);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());

        message.setContent("用户" + user.getName() + "关注了你,http://localhost:8080/user/" + model.getActorId());
//        if (model.getEntityType() == EntityType.ENTITY_QUESTION) {
//            message.setContent("用户" + user.getName()
//                    + "关注了你的问题,http://127.0.0.1:8080/question/" + model.getEntityId());
//        } else if (model.getEntityType() == EntityType.ENTITY_USER) {
//            message.setContent("用户" + user.getName()
//                    + "关注了你,http://127.0.0.1:8080/user/" + model.getActorId());
//        }
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
