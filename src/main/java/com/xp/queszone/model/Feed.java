package com.xp.queszone.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;

@Data
public class Feed {

    private int id;

    /**
     * Feed类型，关注或者评论
     */
    private int type;

    /**
     * 产生关注或者评论的用户id
     */
    private int userId;

    /**
     * 产生关注或者评论的时间
     */
    private Date createdDate;

    /**
     * 产生评论或者关注的详细信息，JSON格式
     */
    private String data;

    private JSONObject dataGetHelper = null;

    public void setData(String data) {
        this.data = data;
        dataGetHelper = JSONObject.parseObject(data);
    }

    public String get(String key) {
        return dataGetHelper == null ? null : dataGetHelper.getString(key);
    }

}
