package org.frank.domain.gateway;


import com.baomidou.mybatisplus.extension.service.IService;
import org.frank.domain.entity.SysUser;

public interface ISysUserGateway extends IService<SysUser> {
    /**
     * select by username.
     */
    SysUser selectByUsername(String username);

    /**
     * Check user name unique.
     */
    boolean checkUserNameUnique(String userName);

    /**
     * Check phone number unique.
     */
    boolean checkPhoneUnique(String phoneNumber);

    /**
     * Check email unique.
     */
    boolean checkEmailUnique(String email);
}
