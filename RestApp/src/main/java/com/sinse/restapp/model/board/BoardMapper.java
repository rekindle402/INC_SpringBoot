package com.sinse.restapp.model.board;

import com.sinse.restapp.domain.Board;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    List selectAll();
    Board select(int board_id);
    Board insert(Board board);
    void update(Board board);
    void delete(int board_id);

}
