package com.sinse.restapp.model;

import com.sinse.restapp.domain.Board;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("JpaBoardDAO")
public class JpaBoardDAO implements BoardDAO {

    // Hibernate는 Session 객체가 쿼리 수행객체임
    // JPA에서는 EntityManager를 쓴다.. 최상위 인터페이스이므로 Hibernate에서도 EntityManeger를 사용 가능함.
    @PersistenceContext // 자동 주입(autowired)
    private EntityManager em;


    @Override
    public List selectAll() {
        return em.createQuery("select b from Board b").getResultList();
    }

    @Override
    public Board select(int board_id) {
        return em.find(Board.class, board_id);
    }

    @Override
    public void insert(Board board) {
        em.persist(board);
    }

    // JPA에서는 Update시에도 반환값을 주는것이 좋다 (업데이트 추적되기 때문)
    @Override
    public Board update(Board board) {
        return em.merge(board);
    }

    @Override
    public void delete(Board board_id) {
        //삭제 시 무조건 일치하는 pk를 지우지 말고, 실제적으로 존재하는 데이터인지 먼저 체크한 후 삭제하는게 안전
       Board board = em.find(Board.class, board_id);
       if(board != null) {
           em.remove(board);
       }
    }
}
