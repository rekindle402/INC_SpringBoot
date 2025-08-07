package com.sinse.restapp.domain;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA 자체가 아닌, MySql의 pk를 넣기
    private int board_id;

    private String title;
    private String writer;
    private String content;

    @Column(name = "regdate", insertable = false, updatable = false)
    private String regdate;
    private int hit;
}
