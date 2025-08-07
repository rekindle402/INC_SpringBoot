package com.sinse.restapp;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController()
public class BoardController {

    @GetMapping("/test")
    public String test() {
        return "JH";
    }

    @GetMapping("/boards")
    public List selectAll(){
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