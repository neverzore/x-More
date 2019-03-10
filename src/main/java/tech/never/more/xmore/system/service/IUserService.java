package tech.never.more.xmore.system.service;

import tech.never.more.xmore.system.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhouzb
 * @since 2018-09-09
 */
public interface IUserService extends IService<User> {
    User findByName(String name);

    User findByNamePassword(String userName, String password);

    void addUser(User user);
}
