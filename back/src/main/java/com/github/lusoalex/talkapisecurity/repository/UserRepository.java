package com.github.lusoalex.talkapisecurity.repository;

import com.github.lusoalex.talkapisecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    /*
    @Query(value = "SELECT * FROM USER WHERE USERNAME = :username AND PASSWORD = :password", nativeQuery = true)
    User findByCredentials(@Param("username") String username, @Param("password") String password);
    */
    @Query(value = "SELECT * FROM USER WHERE ID = :id", nativeQuery = true)
    User findBySubject(@Param("id") String id);
}
