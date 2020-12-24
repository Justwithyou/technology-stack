package com.technology.stack.spring.jdbc.dao;

import com.technology.stack.spring.jdbc.entity.Dict;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 继承SpringDataJpa中的内置分页实现
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/15 10:11
 */
public interface DictDao extends PagingAndSortingRepository<Dict, Integer> {


    Dict findByName(String name);
}
