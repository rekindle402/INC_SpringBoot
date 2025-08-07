package com.sinse.restapp;


import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class BoardController {

    @GetMapping("/test")
    public String test() {
        return "JH";
    }

    @GetMapping("/boards")
    public List selectAll(){
        log.debug("목록요청받음");
        List list = new ArrayList();
        list.add("apple");
        list.add("orange");
        list.add("banana");

        return list;
    }

    @PostMapping("/boards")
    public ResponseEntity<String> regist(){
        return  ResponseEntity.ok("success");
    }
}