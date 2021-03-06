package com.technology.stack.spring.jdbc.service.impl;

import com.technology.stack.spring.jdbc.entity.User;
import com.technology.stack.spring.jdbc.service.UserService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * TODO
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/7 11:29
 */
@CacheConfig(cacheNames = "users")
public class UserServiceImpl implements UserService {

    private final JdbcTemplate jdbcTemplate;

    public UserServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int create(String name, String password) {
        return jdbcTemplate.update("insert into user (NAME, PASSWORD) values (?, ?)", name, password);
    }

    @Cacheable
    @Override
    public List<User> getByName(String name) {

        List<User> users = jdbcTemplate.query("select NAME, PASSWORD from USER where NAME = ?", (resultSet, i) -> {
           User user = new User();
           user.setName(resultSet.getString("NAME"));
           user.setPassword(resultSet.getString("PASSWORD"));
           return user;
        });
        return null;
    }

    @Override
    public int deleteByName(String name) {
        return jdbcTemplate.update("delete from USER where NAME = ?", name);
    }

    @Override
    public int getAllUsers() {

        Integer integer = jdbcTemplate.queryForObject("select count(*) from USER", Integer.class);
        if (integer != null) {
            return integer;
        }
        return 0;
    }

    @Override
    public int deleteAllUsers() {
        return jdbcTemplate.update("delete from USER");
    }
}
