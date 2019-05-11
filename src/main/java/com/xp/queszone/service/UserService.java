package com.xp.queszone.service;

import com.xp.queszone.dao.UserDao;
import com.xp.queszone.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User getUser(int id) {
        return userDao.selectById(id);
    }
}
