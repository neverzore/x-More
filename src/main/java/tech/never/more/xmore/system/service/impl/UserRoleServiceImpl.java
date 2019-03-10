package tech.never.more.xmore.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.never.more.xmore.system.entity.Role;
import tech.never.more.xmore.system.mapper.UserRoleMapper;
import tech.never.more.xmore.system.service.IUserRoleService;

import java.util.List;

/**
 * Created by zhouzb on 2018/9/19.
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, Role> implements IUserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Role> getRoles(Long userId) {
        List<Role> roles = userRoleMapper.getRoles(userId);

        return roles;
    }
}
