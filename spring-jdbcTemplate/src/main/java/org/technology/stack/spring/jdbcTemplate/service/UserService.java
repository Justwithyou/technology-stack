package org.technology.stack.spring.jdbcTemplate.service;

import org.technology.stack.spring.jdbcTemplate.entity.User;

import java.util.List;

/**
 * 用户接口
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/7 11:28
 */
public interface UserService {

    /**
     * 新增一个用户
     *
     * @param name 用户名称
     * @param password 用户密码
     * @return 记录更新条数
     */
    int create(String name, String password);

    /**
     *根据name查询用户
     *
     * @param name 用户名称
     * @return 用户列表
     */
    List<User> getByName(String name);

    /**
     * 根据name删除用户
     *
     * @param name 用户名称
     * @return int
     */
    int deleteByName(String name);

    /**
     * 获取用户总量
     *
     * @return 用户总量
     */
    int getAllUsers();

    /**
     * 删除所有用户
     *
     * @return int
     */
    int deleteAllUsers();
}
