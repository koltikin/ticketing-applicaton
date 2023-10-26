package com.cydeo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Where(clause = "is_Deleted = false")
public class AccountConfirmation{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @ManyToOne
    private User user;
    private boolean isDeleted;

    public AccountConfirmation(User user) {
        this.token = UUID.randomUUID().toString();
        this.user = user;
    }
}
