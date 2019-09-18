package net.codetip.blog.domain;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class User {

  private int id;
  private String username;
  private String password;
  private String img;
  private String name;
  private int age;
  private String school;
  private String intro;
  private String sex;
  private String phone;
  @DateTimeFormat(pattern = "yyyy-MM-dd")//通过valid注解获取当前时间
  private Date createDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd")//通过valid注解获取当前时间
  private Date updateDate;
  private String majoy;
  private String email;
  private int GithubId;
  private String userRole;

  public String getUserRole() {
    return userRole;
  }

  public void setUserRole(String userRole) {
    this.userRole = userRole;
  }

  public int getGithubId() {
    return GithubId;
  }

  public void setGithubId(int githubId) {
    GithubId = githubId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMajoy() {
    return majoy;
  }

  public void setMajoy(String majoy) {
    this.majoy = majoy;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }


  public String getSchool() {
    return school;
  }

  public void setSchool(String school) {
    this.school = school;
  }


  public String getIntro() {
    return intro;
  }

  public void setIntro(String intro) {
    this.intro = intro;
  }


  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }


  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
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
