package org.frank.common.constant;

/**
 * 用户常量信息
 *
 * @author Frank
 */
public class UserConstants {
    /**
     * 平台内系统用户的唯一标志
     */
    public static final String SYS_USER = "SYS_USER";

    /**
     * 正常状态
     */
    public static final String NORMAL = "0";

    /**
     * 异常状态
     */
    public static final String EXCEPTION = "1";

    /**
     * 用户封禁状态
     */
    public static final String USER_DISABLE = "1";

    /**
     * 角色正常状态
     */
    public static final String ROLE_NORMAL = "0";

    /**
     * 角色封禁状态
     */
    public static final String ROLE_DISABLE = "1";

    /**
     * 部门正常状态
     */
    public static final String DEPT_NORMAL = "0";

    /**
     * 部门停用状态
     */
    public static final String DEPT_DISABLE = "1";

    /**
     * 字典正常状态
     */
    public static final String DICT_NORMAL = "0";

    /**
     * 是否为系统默认（是）
     */
    public static final String YES = "Y";

    /**
     * 是否菜单外链：1-是，0-否
     */
    public static final int YES_FRAME = 1;
    public static final int NO_FRAME = 0;

    /**
     * 菜单缓存状态：1-启用，0-禁用
     */
    public static final int MENU_CACHE_ENABLED = 0;
    public static final int MENU_CACHE_DISABLED = 1;

    /**
     * 菜单类型，M-目录，C-菜单，F-按钮
     */
    public static final String TYPE_DIR = "M";
    public static final String TYPE_MENU = "C";
    public static final String TYPE_BUTTON = "F";

    /**
     * 组件标识：Layout, ParentView, InnerLink
     */
    public final static String LAYOUT = "Layout";
    public final static String PARENT_VIEW = "ParentView";
    public final static String INNER_LINK = "InnerLink";

    /**
     * 菜单显示状态：1，显示，-1-隐藏
     */
    public static final int MENU_VISIBLE_HIDDEN = -1;
    public static final int MENU_VISIBLE_SHOW = 1;

    /**
     * 校验是否唯一的返回标识
     */
    public final static boolean UNIQUE = true;
    public final static boolean NOT_UNIQUE = false;

    /**
     * 用户名长度限制
     */
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;

    /**
     * 密码长度限制
     */
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 20;


}
