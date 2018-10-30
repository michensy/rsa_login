package com.sf.rsa.dao;

import com.sf.rsa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Zidong on 2018/10/30 下午2:13
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
}
