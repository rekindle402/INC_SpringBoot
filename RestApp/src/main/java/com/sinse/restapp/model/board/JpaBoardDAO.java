package com.sinse.restapp.model.board;

import com.sinse.restapp.domain.Board;
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
    public void delete(int board_id) {
        em.remove(em.find(Board.class, board_id));
    }
}
