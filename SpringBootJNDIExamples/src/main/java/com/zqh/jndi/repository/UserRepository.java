package com.zqh.jndi.repository;

import com.zqh.jndi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    @Query("from User where username=?1")
    public User findUserByName(String username);
}
