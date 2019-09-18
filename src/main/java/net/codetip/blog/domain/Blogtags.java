package net.codetip.blog.domain;



public class Blogtags {

  private long id;
  private String name;
  private String desc;
  private long path;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }


  public long getPath() {
    return path;
  }

  public void setPath(long path) {
    this.path = path;
  }

}
