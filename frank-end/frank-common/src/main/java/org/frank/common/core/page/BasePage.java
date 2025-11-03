package org.frank.common.core.page;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasePage {

    /**
     * 当前记录起始索引
     */
    private Integer pageNum;

    /**
     * 每页显示记录数
     */
    private Integer pageSize;

    /**
     * 排序列
     */
    private String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    private String isAsc = "asc";

    public BasePage() {
        this.pageNum = 1;
        this.pageSize = 10;
        this.orderByColumn = "";
    }
}
