package com.berry.user.domain.model;

import com.berry.common.auditor.BaseEntity;
import com.berry.common.role.Role;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nickname", nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "point", nullable = false, columnDefinition = "int default 0") // 기본값 0으로 설정
    private int point;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "profile_image")
    private String profileImage;

    public void updateEmail(String email, String userId) {
        this.email = email;
        this.setUpdatedBy(userId);
    }

    public void updatePassword(String encodedNewPassword, String userId) {
        this.password = encodedNewPassword;
        this.setUpdatedBy(userId);
    }
}
