package com.technology.stack.spring.jdbc.dao;

import com.technology.stack.spring.jdbc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 继承SpringDataJpa中的数据访问实现
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/15 14:17
 */
public interface UserDao extends JpaRepository<User, Integer> {
}
