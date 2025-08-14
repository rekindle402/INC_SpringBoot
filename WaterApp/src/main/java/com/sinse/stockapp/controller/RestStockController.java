package com.sinse.waterapp.controller;

import com.sinse.waterapp.model.member.Water;
import com.sinse.waterapp.model.member.WaterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class RestWaterController {

    private WaterService waterService;

    public RestWaterController(WaterService waterService){
        this.waterService = waterService;
    }

    @GetMapping("/waters")
    public List<Water> waterList() throws Exception{
        log.warn("요청 받음");
        waterService.getXml();
        return  null;
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e){
        return e.getMessage();
    }
}
