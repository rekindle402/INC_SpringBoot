package com.sinse.highwayapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinse.highwayapp.model.Highway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
public class HighwayController {
    private String apiKey = "";

    @GetMapping("/info")
    public Highway getList() throws IOException, InterruptedException {
        String baseUrl =  "https://data.ex.co.kr/openapi/odtraffic/trafficAmountByCongest";

        //파라미터 설정
        String url = baseUrl+"?"+
                "key=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8)+
                "&type=" + URLEncoder.encode("json", StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        Highway highway = objectMapper.readValue(response.body(), Highway.class);

        return highway;
    }
}
