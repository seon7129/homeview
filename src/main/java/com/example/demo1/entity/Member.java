package com.example.demo1.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "member")
public class Member{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false, length = 30)
    private String name; //사용자 이름

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false, length = 30, unique = true)
    private String email; //로그인 ID. 중복가입 안되게 고유하게

    @Column(nullable = false, length = 100)
    private String password;

//    @Enumerated(EnumType.STRING)
    private String role;


    @Builder
    public Member(Long id, String name, String nickname, String email, String password, String role){
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void update(String password, String nickname){
        this.password = password;
        this.nickname = nickname;
    }


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Set<GrantedAuthority> roles = new HashSet<>();
//        roles.add(new SimpleGrantedAuthority(role.getKey()));
//        return roles;
//    }
//
//    @Override
//    public String getUsername() {
//        return this.email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
