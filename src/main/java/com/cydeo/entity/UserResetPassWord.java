package com.cydeo.entity;

import com.cydeo.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class UserResetPassWord {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String token;
        @ManyToOne
        private User user;
        private boolean isDeleted;

        @Column(columnDefinition = "TIMESTAMP")
        private LocalDateTime createdDate;

        public UserResetPassWord(User user) {
            this.token = UUID.randomUUID().toString();
            this.user = user;
            this.createdDate = LocalDateTime.now();
        }
}
