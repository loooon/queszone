package com.xp.queszone.controller;

import com.xp.queszone.model.*;
import com.xp.queszone.service.MessageService;
import com.xp.queszone.service.SensitiveService;
import com.xp.queszone.service.UserService;
import com.xp.queszone.util.QuesZoneUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    SensitiveService sensitiveService;

    @RequestMapping(path = {"/msg/list"}, method = RequestMethod.GET)
    public String getConversationList(Model model) {
        User user = hostHolder.getUser();
        if (null == user) {
            return "redirect:/reglogin";
        }
        int localUserId = user.getId();
        List<MessageSummary> conversationList = messageService.getConversationList(localUserId,0,10);
        List<ViewObject> conversations = new ArrayList<>();
        for (MessageSummary messageSummary: conversationList) {
            ViewObject vo = new ViewObject();
            vo.set("conversation",messageSummary);
            int targetId = messageSummary.getFromId() == localUserId ? messageSummary.getToId() : messageSummary.getFromId();
            vo.set("user",userService.getUser(targetId));
            vo.set("unread",messageService.getConvesationUnreadCount(localUserId,messageSummary.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute("conversations",conversations);
        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"}, method = RequestMethod.GET)
    public String getConversationList(Model model, @RequestParam("conversationId") String conversationId) {
        try {
            List<Message> messageList = messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messages = new ArrayList<>();
            for (Message message : messageList) {
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("user", userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
        } catch (Exception e) {
            logger.error("获取详情失败！"+e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/addMessage"}, method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        try {
            if (null == hostHolder.getUser()) {
                return QuesZoneUtil.getJSONString(999,"未登录!");
            } else {
                User user = userService.selectByName(toName);
                if (null == user) {
                    return QuesZoneUtil.getJSONString(1,"用户不存在！");
                }
                Message message = new Message();
                message.setContent(sensitiveService.filter(content));
                message.setCreatedDate(new Date());
                message.setFromId(hostHolder.getUser().getId());
                message.setToId(user.getId());
                messageService.addMessage(message);
                return QuesZoneUtil.getJSONString(0);
            }
        } catch (Exception e) {
            logger.error("发送消息失败！"+e.getMessage());
            return QuesZoneUtil.getJSONString(1,"发送站内信失败！");
        }
    }
}
