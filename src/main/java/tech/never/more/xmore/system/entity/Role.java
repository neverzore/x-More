package tech.never.more.xmore.system.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by zhouzb on 2018/9/19.
 */
@Getter
@Setter
public class Role implements Serializable {

    private static final long serialVersionUID = 3317605093251593984L;

    public static final String ROLE_PREFIX = "ROLE_";

    private Integer roleId;
    private String code;
    private String name;
}
