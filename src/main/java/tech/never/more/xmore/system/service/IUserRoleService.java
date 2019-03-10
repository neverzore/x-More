package tech.never.more.xmore.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.never.more.xmore.system.entity.Role;

import java.util.List;

/**
 * Created by zhouzb on 2018/9/19.
 */
public interface IUserRoleService extends IService<Role> {

    List<Role> getRoles(Long userId);
}
