package com.petprojects.mymoneyproject.repository;

import com.petprojects.mymoneyproject.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User>{

    User findUserByLogin(String login);

    User findUserByEmail(String email);
}
