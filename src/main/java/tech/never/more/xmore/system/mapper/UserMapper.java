package tech.never.more.xmore.system.mapper;

import org.apache.ibatis.annotations.Select;
import tech.never.more.xmore.system.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhouzb
 * @since 2018-09-09
 */
public interface UserMapper extends BaseMapper<User> {

    @Select(" select id, name, password, salt from user where name = #{name}")
    User findByName(String name);

    @Select(" select name, password, salt from user where name = #{name} and password = #{password}")
    User findByNamePassword(String name, String password);
}
