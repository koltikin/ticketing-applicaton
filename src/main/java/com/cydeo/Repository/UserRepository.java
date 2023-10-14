package com.cydeo.Repository;

import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findAllByIsDeletedOrderByFirstName(Boolean deleted);

//    User findByUserName(String username);
    User findByUserNameAndIsDeleted(String username,Boolean deleted);
    @Transactional
    void deleteByUserName(String username);

    List<User> findByRole_DescriptionIgnoreCaseAndIsDeleted(String role_description,Boolean deleted);

    Boolean existsByUserNameAndIsDeleted(String userName, Boolean isDeleted);
}
