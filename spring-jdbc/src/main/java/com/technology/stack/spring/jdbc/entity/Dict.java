package com.technology.stack.spring.jdbc.entity;

import javax.persistence.*;

/**
 * 定义字典实体类
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/14 19:12
 */
@Table(name = "dict")
@Entity
public class Dict {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
}
