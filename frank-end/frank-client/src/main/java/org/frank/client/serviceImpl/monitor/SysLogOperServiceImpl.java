package org.frank.client.serviceImpl.monitor;

import cn.hutool.core.bean.BeanUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.app.service.monitor.SysLogOperService;
import org.frank.domain.entity.SysLogOper;
import org.frank.domain.gateway.ISysLogOperGateway;
import org.frank.shared.sysLogOper.resp.SysLogOperResp;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SysLogOperServiceImpl implements SysLogOperService {

    @Resource
    private ISysLogOperGateway gateway;


    @Override
    public void deleteByIds(List<Long> operIds) {
        gateway.removeBatchByIds(operIds);
    }

    @Override
    public void cleanAll() {
        gateway.cleanAll();
    }

    @Override
    public SysLogOperResp getInfoById(Long operId) {
        SysLogOper byId = gateway.getById(operId);
        return BeanUtil.copyProperties(byId, SysLogOperResp.class);
    }

}