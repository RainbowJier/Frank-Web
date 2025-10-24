package org.frank.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MenuEnum {

    MENU("M", "目录"),
    CAIDAN("C", "菜单"),
    FUNCTION("F", "按钮");

    private final String code;
    private final String description;
}
