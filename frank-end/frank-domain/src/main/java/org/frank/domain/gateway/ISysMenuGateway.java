package org.frank.domain.gateway;

import com.baomidou.mybatisplus.extension.service.IService;
import org.frank.domain.entity.SysMenu;
import org.frank.domain.entity.SysRole;

import java.util.List;

public interface ISysMenuGateway extends IService<SysMenu> {

    List<SysMenu> selectByIds(List<Long> menuIds);
}