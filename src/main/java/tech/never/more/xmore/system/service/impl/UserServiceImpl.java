package tech.never.more.xmore.system.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import tech.never.more.xmore.system.entity.User;
import tech.never.more.xmore.system.entity.Role;
import tech.never.more.xmore.system.mapper.UserMapper;
import tech.never.more.xmore.system.service.IUserRoleService;
import tech.never.more.xmore.system.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhouzb
 * @since 2018-09-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private BCryptPasswordEncoder cryptPasswordEncoder;

    @Override
    public User findByName(String name) {
        User user = userMapper.findByName(name);

        if (user != null) {

            List<Role> roles = userRoleService.getRoles(user.getId());

            if (!CollectionUtils.isEmpty(roles)) {
                user.setAuthority(new ArrayList<>());
                GrantedAuthority grantedAuthority;
                for (Role role : roles) {
                    grantedAuthority = new SimpleGrantedAuthority(role.getCode());
                    user.getAuthority().add(grantedAuthority);
                }
            }
        }

        return user;
    }

    @Override
    public User findByNamePassword(String userName, String password) {
        User user = this.findByName(userName);

        if (user == null) {
            return null;
        }

        String salt = user.getSalt();
        String pwd = user.getPassword();

        if (cryptPasswordEncoder.matches(StringUtils.join(password, salt), pwd)) {
            return user;
        }

        return null;
    }

    @Override
    public void addUser(User user) {
        String salt = RandomStringUtils.random(8, true, true);
        user.setSalt(salt);
    }
}
