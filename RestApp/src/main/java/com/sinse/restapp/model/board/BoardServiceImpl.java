package com.sinse.restapp.model.board;

import com.sinse.restapp.domain.Board;
import com.sinse.restapp.exception.BoardException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {
    
    // 스프링에서는 autowired 지양
    // 따라서 생성자를 통해, 정확히 어떤 자료형을 사용할시 명시하는게 좋다..
    private BoardDAO boardDAO;

    public BoardServiceImpl(@Qualifier("JpaBoardDAO") BoardDAO boardDAO) {
        this.boardDAO = boardDAO;
    }

    @Override
    public List selectAll() {
        return boardDAO.selectAll();
    }

    @Override
    public Board select(int board_id) {
        return boardDAO.select(board_id);
    }

    @Override
    public void insert(Board board)throws DataAccessException {
        try{
            boardDAO.insert(board);
            // 이 서비스 객체가 특정 DB 연동 기술에 국한된 것이 아니라,
            // 모든 기술에 중립적이어야 하므로, 예외 객체 조차도 상위 인터페이스를 사용하여야
            // 서비스 계층이 유연해 질 수 있다.
        } catch (DataAccessException e) {
            throw new BoardException("글 등록 실패", e);
        }
    }

    @Override
    public Board update(Board board)throws DataAccessException {
        try {
            return boardDAO.update(board);
        } catch (DataAccessException e) {
            throw new BoardException("수정실패", e);
        }
    }

    @Override
    public void delete(int board_id) throws DataAccessException {
        try{
            boardDAO.delete(board_id);
        } catch (DataAccessException e) {
            throw new BoardException("삭제실패", e);

        }
    }
}
