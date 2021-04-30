package com.genesislab.videoservice.domain.member.entity;

import com.genesislab.videoservice.domain.model.*;
import com.genesislab.videoservice.global.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"email", "name", "phoneNumber"})
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Embedded
    private Name name;

    @Embedded
    private PhoneNumber phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Role role;

    @Builder
    public Member(Email email, Password password,  Name name, PhoneNumber phoneNumber, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public void encodePassword(String encodedPassword) {
        this.password = Password.of(encodedPassword);
    }
}
