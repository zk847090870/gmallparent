package com.mytx.gmall.gmalllogger.controller;

import com.mytx.gmall.common.constants.GmallConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
public class LoggerController {
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;
    @PostMapping("/log")
    public String log(@RequestParam("logString") String logString){
        JSONObject jsonObject = JSON.parseObject(logString);
        jsonObject.put("ts",System.currentTimeMillis());
        log.info(logString);
        if("startup".equals( jsonObject.getString("type"))){
            kafkaTemplate.send(GmallConstants.KAFKA_STARTUP,jsonObject.toJSONString());
        }else{
            kafkaTemplate.send(GmallConstants.KAFKA_EVENT,jsonObject.toJSONString());
        }

        return "success";
    }
}