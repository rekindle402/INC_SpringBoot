package com.sinse.restapp.controller;


import com.sinse.restapp.domain.Board;
import com.sinse.restapp.model.board.BoardMapper;
import com.sinse.restapp.model.board.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class BoardController {

    private BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/boards")
    public List selectAll() {
        log.debug("목록요청받음");
        List list = new ArrayList();
        list.add("apple");
        list.add("orange");
        list.add("banana");

        return list;
    }

    @GetMapping("/boards/{board_id}")
    public Board selectOne(@PathVariable int board_id) {
        Board board = boardService.select(board_id);
        return board;
    }

    @PostMapping("/boards")
    // Json 문자열로 전송된 파라미터와 서버측의 모델과의 자동 매핑 (스프링 레거시도 지원)
    public ResponseEntity<String> regist(@RequestBody Board board) {
        boardService.insert(board);
        return ResponseEntity.ok("success");
    }
    // 수정 요청 처리
    @PutMapping("/boards/{board_id}")
    public ResponseEntity<String> update(@PathVariable("board_id") int board_id, @RequestBody Board board) {
        board.setBoard_id(board_id); // 경로로 전송된 파라미터를 다시 한번 확인 차 모델의 대입
        boardService.update(board);
        return ResponseEntity.ok("success");
    }

    // 삭제 요청 처리
    @DeleteMapping("/boards/{board_id}")
    public ResponseEntity<String> delete(@PathVariable("board_id") int board_id) {
        boardService.delete(board_id);
        return ResponseEntity.ok("success");
    }
}