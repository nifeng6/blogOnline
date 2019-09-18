package net.codetip.blog.dto;


public class UploadMsg {
    public int success;
    public String message;
    public String url;

    public UploadMsg(int success, String message, String url) {
        this.success = success;
        this.message = message;
        this.url = url;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}