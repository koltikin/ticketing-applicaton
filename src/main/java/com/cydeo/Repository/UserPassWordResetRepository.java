package com.cydeo.Repository;

import com.cydeo.entity.UserResetPassWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPassWordResetRepository extends JpaRepository<UserResetPassWord,Long> {
    Boolean existsByToken(String token);

    UserResetPassWord findByToken(String token);

//    @Query("SELECT rp FROM UserResetPassWord rp WHERE rp.user.userName = ?1 LIMIT 1")
//    UserResetPassWord findByUser_UserName(String username);
    UserResetPassWord findTopByUser_UserName(String username);

    Boolean existsByUser_UserName(String email);
}
