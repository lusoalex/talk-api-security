package com.github.lusoalex.talkapisecurity.repository.impl;

import com.github.lusoalex.talkapisecurity.model.User;
import com.github.lusoalex.talkapisecurity.repository.CustomUserRepository;

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
