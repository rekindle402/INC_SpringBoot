package com.sinse.stockapp.model.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class WaterService {
    private String apiKey = "U4bvIZWcxnyUhOn9qHa%2FZuGXfGlT6ZNRsmN2iBymLwemRHgYToSQ2ApHPRfXKjTEfSYohcMZltpI8n9J8J8mUA%3D%3D";

    public void getXml() throws IOException, InterruptedException {
        log.warn("서비스 작동");
        String baseUrl = "http://apis.data.go.kr/6460000/jnYaksoo/getYakSooList";
        

        //파라미터 설정
        String url = baseUrl + "?" +
                "ServiceKey=" + apiKey+
                "&title=" + URLEncoder.encode("꽃무릇", StandardCharsets.UTF_8) +
                "&pageSize=" + 10 +
                "&startPage=" + 0;

        log.warn("요청 http : " + url);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type","application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.warn(response.toString());
    }
}
