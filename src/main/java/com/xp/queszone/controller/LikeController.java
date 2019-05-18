package com.xp.queszone.controller;

import com.xp.queszone.model.EntityType;
import com.xp.queszone.model.HostHolder;
import com.xp.queszone.model.User;
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

    @RequestMapping(path = {"/like"}, method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        User user = hostHolder.getUser();
        if (null == user) {
            return QuesZoneUtil.getJSONString(999);
        }
        long likeCount = likeService.like(user.getId(), EntityType.ENTITY_COMMENT, commentId);
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
