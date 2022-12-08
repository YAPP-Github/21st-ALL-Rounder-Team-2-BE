package com.yapp.archiveServer.demo.repository;

import com.yapp.archiveServer.demo.domain.DemoMember;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class DemoRepository {

    @PersistenceContext
    EntityManager em;

    public void save(DemoMember demoMember) {
        em.persist(demoMember);
    }

    public DemoMember find(Long id) {
        return em.find(DemoMember.class, id);
    }

    public List<DemoMember> findByName(String name) {
        return em.createQuery("select m from DemoMember m where m.name =:name", DemoMember.class)
                .setParameter("name", name)
                .getResultList();
    }
}
