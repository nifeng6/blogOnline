package net.codetip.blog.domain;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class AskAnswer {

  private int id;
  private String content;
  private int uid;
  private int path;
  private int askId;
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


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }


  public int getPath() {
    return path;
  }

  public void setPath(int path) {
    this.path = path;
  }


  public int getAskId() {
    return askId;
  }

  public void setAskId(int askId) {
    this.askId = askId;
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
