package com.sinse.restapp.model.board;

import com.sinse.restapp.domain.Board;

import java.util.List;

public interface BoardDAO {
    public List selectAll();
    public Board select(int board_id);
    public void insert(Board board);
    public void update(Board board);
    public void delete(Board board_id);
}
