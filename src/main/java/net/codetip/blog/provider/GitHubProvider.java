package net.codetip.blog.provider;

import com.alibaba.fastjson.JSON;
import net.codetip.blog.domain.GitHubUser;
import net.codetip.blog.dto.GitHubAccessTokenDto;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GitHubProvider {
    public String getAccessToken(GitHubAccessTokenDto gitHubAccessTokenDto){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(gitHubAccessTokenDto));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("=")[1].split("&")[0];
            return token;
        } catch (IOException e) {
        }
        return null;
    }

    public GitHubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GitHubUser gitHubUser = JSON.parseObject(string, GitHubUser.class);
            return gitHubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
