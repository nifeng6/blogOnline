package net.codetip.blog.mapper;

import net.codetip.blog.domain.AskAnswer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface AskAnswerMapper {
    @Insert("insert into ask_answer(`content`,`uid`,`ask_id`,`create_date`,`update_date`) values(#{content},#{uid},#{askId},#{createDate},#{updateDate})")
    void insertAnswer(AskAnswer askAnswer);

    @Select("select a.id,a.uid,a.content,a.ask_id,a.create_date,a.update_date,u.username,u.name,u.img from `ask_answer` a INNER JOIN `user` u ON a.uid=u.id where ask_id=#{id} order by a.create_date desc")
    List<Map> findAnswersById(int id);

    @Select("select count(1) from ask_answer where ask_id=#{id}")
    int askCount(int id);

    @Select("SELECT COUNT(1) FROM ask_answer WHERE uid=#{uid}")
    int releaseAskAnswer(int uid);
}
