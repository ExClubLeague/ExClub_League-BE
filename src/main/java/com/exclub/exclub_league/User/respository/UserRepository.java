package com.exclub.exclub_league.User.respository;
import com.exclub.exclub_league.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // email로 사용자 정보를 가지고 옴.

    boolean existsByUsername(String userName);
}
