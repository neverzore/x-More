package tech.never.more.xmore.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import tech.never.more.xmore.system.entity.Role;

import java.util.List;

/**
 * Created by zhouzb on 2018/9/19.
 */
public interface UserRoleMapper extends BaseMapper<Role> {
    @Select(" select r.code, r.name from user_role ur, user u, role r where u.id = ur.user_id and r.id = ur.role_id and u.id = #{userId} ")
    List<Role> getRoles(Long userId);
}
