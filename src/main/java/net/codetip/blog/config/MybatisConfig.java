package net.codetip.blog.config;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class MybatisConfig {
        @Bean
        public PageHelper pageHelper(){
            System.out.print("pageHelper");
            PageHelper pageHelper = new PageHelper();
            Properties properties = new Properties();

            properties.setProperty("offsetAsPageNum","true");
            properties.setProperty("rowBoundsWithCount","true");
            properties.setProperty("reasonable","true");

            pageHelper.setProperties(properties);

            return pageHelper;
        }
}
