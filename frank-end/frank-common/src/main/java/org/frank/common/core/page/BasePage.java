package org.frank.common.core.page;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.frank.common.util.StringUtil;

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

    /**
     * 分页参数合理化
     */
    private Boolean reasonable = true;


    // set default value.
    public BasePage() {
        this.pageNum = 1;
        this.pageSize = 10;
    }

    public String getOrderBy() {
        if (StringUtil.isEmpty(orderByColumn)) {
            return "";
        }
        return StringUtil.toUnderScoreCase(orderByColumn) + " " + isAsc;
    }

    public void setIsAsc(String isAsc) {
        if (StringUtil.isNotEmpty(isAsc)) {
            // 兼容前端排序类型
            if ("ascending".equals(isAsc)) {
                isAsc = "asc";
            } else if ("descending".equals(isAsc)) {
                isAsc = "desc";
            }
            this.isAsc = isAsc;
        }
    }

    public Boolean getReasonable() {
        if (StringUtil.isNull(reasonable)) {
            return Boolean.TRUE;
        }
        return reasonable;
    }
}
