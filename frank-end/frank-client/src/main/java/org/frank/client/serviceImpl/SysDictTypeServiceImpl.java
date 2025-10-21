package org.frank.client.serviceImpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.frank.app.service.SysDictTypeService;
import org.frank.common.core.page.TableDataInfo;
import org.frank.domain.entity.SysDictType;
import org.frank.domain.gateway.ISysDictTypeGateway;
import org.frank.shared.sysDictType.req.PageQuery;
import org.frank.shared.sysDictType.resp.SysDictTypeResp;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class SysDictTypeServiceImpl implements SysDictTypeService {

    @Resource
    private ISysDictTypeGateway gateway;

    @Override
    public TableDataInfo selectDictTypeList(PageQuery params) {
        IPage<SysDictType> page = new Page<>(params.getPageNum(), params.getPageSize());

        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(params.getDictName()), SysDictType::getDictName, params.getDictName())
                .eq(params.getStatus() != null, SysDictType::getStatus, params.getStatus())
                .like(StringUtils.hasText(params.getDictType()), SysDictType::getDictType, params.getDictType())
                .orderByDesc(SysDictType::getCreateTime);
        // 创建时间范围查询
        if (StringUtils.hasText(params.getBeginTime()) && StringUtils.hasText(params.getEndTime())) {
            wrapper.ge(StringUtils.hasText(params.getBeginTime()), SysDictType::getCreateTime, params.getBeginTime())
                    .le(StringUtils.hasText(params.getEndTime()), SysDictType::getCreateTime, params.getEndTime());
        }
        
        List<SysDictType> list = gateway.list(page, wrapper);
        List<SysDictTypeResp> respList = BeanUtil.copyToList(list, SysDictTypeResp.class);

        return new TableDataInfo(respList, list.size());
    }
}