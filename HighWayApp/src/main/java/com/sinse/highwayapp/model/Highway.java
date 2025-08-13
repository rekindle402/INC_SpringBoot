package com.sinse.highwayapp.model;

import lombok.Data;

import java.util.List;

@Data
public class Highway {
    private int count;                  // 전체 결과 수
    private String message;             // 결과 메시지
    private String code;                // 결과
    private List<HWList> list;

    @Data
    public static class HWList {
        private String stdHour;             // 수집시각
        private String routeNo;             // 노선번호
        private String routeName;           // 도로명
        private String updownTypeCode;      // 방향(S:기점/E:종점)
        private String vdsId;               // VDS_ID
        private String trafficAmout;        // 교통량
        private String shareRatio;          // 점유율
        private String conzoneId;           // 콘존ID
        private String conzoneName;         // 콘존명
        private String stdDate;             // 수집일자
        private String speed;               // 속도
        private String timeAvg;             // 통행시간
        private String grade;               // 소통등급
    }
}
