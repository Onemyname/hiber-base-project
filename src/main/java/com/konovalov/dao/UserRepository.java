package com.konovalov.dao;

import com.konovalov.entities.User;
import jakarta.persistence.EntityManager;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.context.spi.CurrentSessionContext;


@Getter
public class UserRepository extends RepositoryBase<Long, User> {
    private final EntityManager entityManager;
    public UserRepository(EntityManager entityManager) {
        super(entityManager, User.class);
        this.entityManager = entityManager;
    }
    //todo
}
