package org.frank.common.core.page;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 *
 * @author Frank
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PageResult implements Serializable {
    private Long total;

    private Long pageNum;

    private Long pageSize;

    private List<?> rows;

    public PageResult(List<?> list, long total) {
        this.rows = list;
        this.total = total;
    }

    public static <T> PageResult ok(IPage<T> page) {
        return new PageResult(
                page.getTotal(),
                page.getCurrent(),
                page.getSize(),
                page.getRecords()
        );
    }

    public static <E, V> PageResult ok(IPage<E> page, Class<V> voClass) {
        List<V> voList = BeanUtil.copyToList(page.getRecords(), voClass);

        return new PageResult(
                page.getTotal(),
                page.getCurrent(),
                page.getSize(),
                voList
        );
    }

}
