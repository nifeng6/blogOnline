package net.codetip.blog.domain;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Ask {

  private int id;
  private String title;
  private String content;
  private int uid;
  @DateTimeFormat(pattern = "yyyy-MM-dd")//通过valid注解获取当前时间
  private Date createDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd")//通过valid注解获取当前时间
  private Date updateDate;
  private String cates;
  private String visit;
  private int sortId;

  public int getSortId() {
    return sortId;
  }

  public void setSortId(int sortId) {
    this.sortId = sortId;
  }

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


  public String getCates() {
    return cates;
  }

  public void setCates(String cates) {
    this.cates = cates;
  }


  public String getVisit() {
    return visit;
  }

  public void setVisit(String visit) {
    this.visit = visit;
  }

}
