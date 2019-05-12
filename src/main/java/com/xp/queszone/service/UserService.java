package com.xp.queszone.service;

import com.xp.queszone.dao.LoginTicketDao;
import com.xp.queszone.dao.UserDao;
import com.xp.queszone.model.LoginTicket;
import com.xp.queszone.model.User;
import com.xp.queszone.util.QuesZoneUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketDao loginTicketDao;

    public User getUser(int id) {
        return userDao.selectById(id);
    }

    public Map<String, String> login(String userName, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(userName)) {
            map.put("msg","用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg","密码不能为空");
            return map;
        }
        User user = userDao.selectByName(userName);
        if (null == user) {
            map.put("msg","您尚未注册");
            return map;
        }
        String salt = user.getSalt();
        if (!user.getPassword().equals(QuesZoneUtil.MD5(password+salt))) {
            map.put("msg","密码错误");
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public Map<String, String> register(String userName, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(userName)) {
            map.put("msg","用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg","密码不能为空");
            return map;
        }
        if (null != userDao.selectByName(userName)) {
            map.put("msg","用户名已被注册");
            return map;
        }

        User user = new User();
        user.setName(userName);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setPassword(QuesZoneUtil.MD5(password+user.getSalt()));
        userDao.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;

    }

    public String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(now.getTime()+3600*24*100); //设置loginTicket有效期 100 天
        loginTicket.setExpired(now);
        loginTicket.setStatus(0); //默认有效，若用户手动登出则改为1
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDao.addLoginTicket(loginTicket);
        return loginTicket.getTicket();
    }
}
