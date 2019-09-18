package net.codetip.blog.mapper;

import com.sun.javafx.collections.MappingChange;
import net.codetip.blog.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    @Select("select * from user")
    List<User> findUsers();

    @Select("select u.id,u.username,u.name,u.age,u.email,u.github_id,u.img,u.create_date,u.intro,u.majoy,u.phone,u.`password`,u.sex,u.school,ur.name user_role from user u inner join user_role ur on u.role_id=ur.id where u.id=#{id}")
    User findUser(int id);

    @Select("select * from user where username=#{username}")
    User findUserByName(String username);

    @Insert("insert into user(`create_date`,`update_date`,`username`,`password`,`email`,`github_id`,`img`,`name`) values(#{createDate},#{updateDate},#{username},#{password},#{email},#{GithubId},#{img},#{name})")
    void addUser(User user);

    @Select("select * from user order by create_date desc limit 6")
    List<Map> LimitUsers();

    @Select("select * from user where github_id=#{GitHubId}")
    User findGitId(int GitHubId);

    @Update("update user set password=#{password} where github_id=#{GithubId}")
    void updateGitHubPwd(User user);

    @Update("update user set password=#{password} where id=#{id}")
    void updateUserPwd(User user);

    @Update("update user set `name`=#{name},`intro`=#{intro},`sex`=#{sex},`email`=#{email},`phone`=#{phone},`update_date`=#{updateDate} where `id`=#{id}")
    void updateUserInfo(User user);

    @Update("update user set img=#{img} where id=#{uid}")
    void updateUserImg(String img,int uid);

}
