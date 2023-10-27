package com.cydeo.Repository;

import com.cydeo.entity.AccountConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountConfirmationRepository extends JpaRepository<AccountConfirmation,String> {
    @Query("SELECT ac.token FROM AccountConfirmation ac where ac.user.userName = :userName")
     String findTokenByUserName(String userName);
    @Query("SELECT ac.user.userName FROM AccountConfirmation ac where ac.token = :token")
     String findUserNameByToken(String token);

     Boolean existsByToken(String token);

     AccountConfirmation findByToken(String token);

}
