package net.codetip.blog.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.xml.crypto.Data;
import java.util.Date;

public class AskSort {
    private int id;
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")//通过valid注解获取当前时间
    private Date createDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")//通过valid注解获取当前时间
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
