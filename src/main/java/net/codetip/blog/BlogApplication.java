package net.codetip.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@MapperScan("net.codetip.blog.mapper")
@SpringBootApplication
public class BlogApplication  extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);

    }

}
