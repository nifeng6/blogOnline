package net.codetip.blog.domain;

public class GitHubUser {
    private String name;
    private int id;
    private String dio;
    private String AvatarUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDio() {
        return dio;
    }

    public void setDio(String dio) {
        this.dio = dio;
    }

    public String getAvatarUrl() {
        return AvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        AvatarUrl = avatarUrl;
    }
}
