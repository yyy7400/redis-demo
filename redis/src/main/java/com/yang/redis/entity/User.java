package com.yang.redis.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息实体
 * @author yangyuyang
 * @date 2019-11-29
 */
public class User implements Serializable {
    private static final long serialVersionUID = -5760002778349631015L;
    private Long id;
    private String guid;
    private String name;
    private String age;
    private Date createTime;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
