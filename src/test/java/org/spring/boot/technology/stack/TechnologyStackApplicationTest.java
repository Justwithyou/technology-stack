package org.spring.boot.technology.stack;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spring.boot.technology.stack.controller.HelloController;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * 技术栈测试工具
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/2 15:02
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TechnologyStackApplicationTest {

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
    }

    @Test
    public void getHello() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/hello").accept(MediaType.APPLICATION_JSON));
    }
}
