package com.decathlon.sample.apisecurity.repository.impl;

import com.decathlon.sample.apisecurity.model.User;
import com.decathlon.sample.apisecurity.repository.CustomUserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomUserRepositoryImpl implements CustomUserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> findByCredentialsUnsafe(String username, String password) {
        String query = "select * from user where username='"+username+"' and password='"+password+"'";
        return em.createNativeQuery(query, User.class).getResultList();
    }
}
