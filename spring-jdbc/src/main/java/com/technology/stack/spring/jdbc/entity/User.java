package com.technology.stack.spring.jdbc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 *
 * @author zhoujunhui
 * @version 1.0.0
 * @date 2020/9/18 13:54
 */
@Data
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = -8737185131526310609L;
    /** 用户标识 */
    private long userId;
    /** 用户名称 */
    private String userName;
    /** 中文名称 */
    private String name;
    /** 密码 */
    private String password;
    /** 项目标识 */
    private long projId;
    /** 邮箱 */
    private String email;
    /** 手机号码 */
    private String mobile;
    /**
     * 用户状态
     * 1. 正常
     * 2. 禁用
     * 3. 删除
     */
    private int userStatus;
    /** 用户创建标识 */
    private String createUserName;
    /** 创建时间 */
    private Date createTime;
    /** 修改时间 */
    private Date modifiedTime;
}
