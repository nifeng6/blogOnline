package net.codetip.blog.mapper;

import com.sun.javafx.collections.MappingChange;
import net.codetip.blog.domain.Blogtags;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BlogTagsMapper {
    @Select("select * from blogtags")
    List<Blogtags> findTags();

    @Select("select name from blogtags where path!=0 limit 27")
    List<Map> findHotTags();

}
