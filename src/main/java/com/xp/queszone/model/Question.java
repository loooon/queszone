package com.xp.queszone.model;

import lombok.Data;

import java.util.Date;

@Data
public class Question {
    private int id;
    private String title;
    private String content;
    private int userId;
    private Date createdDate;
    private Integer commentCount;
}
