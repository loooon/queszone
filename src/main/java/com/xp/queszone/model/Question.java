package com.xp.queszone.model;

import lombok.Data;

import java.util.Date;

@Data
public class Question {
    private Integer id;
    private String title;
    private String context;
    private Integer userId;
    private Date createdDate;
    private Integer commentCount;
}
