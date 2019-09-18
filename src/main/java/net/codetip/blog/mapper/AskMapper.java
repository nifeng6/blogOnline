package net.codetip.blog.mapper;

import com.sun.javafx.collections.MappingChange;
import net.codetip.blog.domain.Ask;
import net.codetip.blog.domain.AskAnswer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;
import java.util.List;

public interface AskMapper {
    @Insert("insert into ask(`sort_id`,`title`,`content`,`uid`,`cates`,`create_date`,`update_date`) values(#{sortId},#{title},#{content},#{uid},#{cates},#{createDate},#{updateDate})")
    void insertAsk(Ask ask);

    @Select("select a.sort_id,a.id,a.uid,a.cates,a.content,a.create_date,a.title,a.visit,u.username,u.name,u.img from ask a INNER JOIN user u ON a.uid=u.id where a.id=#{id}")
    Map findAskById(int id);


    @Select("SELECT ass.title asksort,a.id,a.uid,a.title,a.content,a.create_date,a.update_date,a.cates,a.visit,u.name,u.username,u.img FROM (`ask` a INNER JOIN `user` u ON u.id=a.uid)\n" +
            "INNER JOIN ask_sort ass \n" +
            "ON ass.id=a.sort_id\n" +
            "order by a.${sort} desc")
    List<Map> findAsks(String sort);

    @Select("SELECT ass.title asksort,a.id,a.uid,a.title,a.content,a.create_date,a.update_date,a.cates,a.visit,u.username,u.name,u.img FROM " +
            "(`ask` a INNER JOIN `user` u ON u.id=a.uid)\n" +
            "INNER JOIN ask_sort ass \n" +
            "ON ass.id=a.sort_id\n" +
            "WHERE a.sort_id=#{sortId} order by a.create_date desc ")
    List<Map> findAsksBySort(String sort,String sortId);

    @Update("UPDATE ask set visit = visit+1 WHERE id = #{id}")
    void addVisit(int id);

    @Select("select * from ask where uid=#{uid}")
    List<Map> findAskByUId(int uid);

    @Select("select * from ask order by ${sort} desc limit 6")
    List<Map> findByHotNew(String sort);

    @Select("SELECT COUNT(1) FROM ask WHERE uid=#{uid}")
    int releaseAsk(int uid);

    @Update("update ask set content=#{content},title=#{title},cates=#{cates},update_date=#{updateDate},sort_id=#{sortId} where id=#{id}")
    void askEdit(Ask ask);

}
