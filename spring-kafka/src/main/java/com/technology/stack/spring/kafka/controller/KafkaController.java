package com.technology.stack.spring.kafka.controller;

import com.technology.stack.spring.kafka.producer.LogProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 *
 * @author zhoujunhui-a
 * @version 1.0.0
 * @date 2020/12/25 16:14
 */
@RestController
public class KafkaController {

    @Autowired
    LogProducer logProducer;

    @RequestMapping("/message/send/{msg}")
    public String send(@PathVariable("msg") String msg){

        logProducer.sendMessage("spring-kafka", msg);
        return "success";
    }

}
