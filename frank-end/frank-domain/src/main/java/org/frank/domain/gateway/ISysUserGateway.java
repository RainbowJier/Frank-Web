package org.frank.domain.gateway;


import com.baomidou.mybatisplus.extension.service.IService;
import org.frank.domain.entity.SysUser;

public interface ISysUserGateway extends IService<SysUser> {


    /**
     * select by username.
     * @param username
     * @return SysUser.
     */
    SysUser selectByUsername(String username);
}
