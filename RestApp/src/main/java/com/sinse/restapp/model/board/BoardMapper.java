package com.sinse.restapp.model.board;

import com.sinse.restapp.domain.Board;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    public List selectAll();
    public Board select(int board_id);
    public Board insert(Board board);
    public void update(Board board);
    public void delete(int board_id);

}
