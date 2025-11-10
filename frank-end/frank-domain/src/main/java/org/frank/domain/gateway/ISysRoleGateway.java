package org.frank.domain.gateway;

import com.baomidou.mybatisplus.extension.service.IService;
import org.frank.domain.entity.SysRole;

import java.util.List;

public interface ISysRoleGateway extends IService<SysRole> {

    List<SysRole> selectListByIds(List<Long> roleIds);

    /**
     * Check if role name is unique
     */
    boolean checkRoleNameUnique(String roleName);

    /**
     * Check if role key is unique
     */
    boolean checkRoleKeyUnique(String roleKey);
}