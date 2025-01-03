package com.ibat.myblog.Repository;

import com.ibat.myblog.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findFirstByOrderByUserIdAsc();
    // 可以添加自定义查询方法

}
