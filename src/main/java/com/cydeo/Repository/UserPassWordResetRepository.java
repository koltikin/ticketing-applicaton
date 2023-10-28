package com.cydeo.Repository;

import com.cydeo.entity.UserResetPassWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPassWordResetRepository extends JpaRepository<UserResetPassWord,Long> {
    Boolean existsByToken(String token);

    UserResetPassWord findByToken(String token);
}
