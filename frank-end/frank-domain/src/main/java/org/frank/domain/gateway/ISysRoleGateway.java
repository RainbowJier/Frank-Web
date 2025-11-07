package org.frank.domain.gateway;

import com.baomidou.mybatisplus.extension.service.IService;
import org.frank.domain.entity.SysRole;

import java.util.List;

public interface ISysRoleGateway extends IService<SysRole> {

    List<SysRole> selectListByIds(List<Long> roleIds);
}