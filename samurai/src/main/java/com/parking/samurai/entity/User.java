package com.parking.samurai.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
* JPA entity representing an application user.
* Implements Spring Security's UserDetails interface to integrate with authentication and authorization.
* Stores credentials and basic profile information.
* Designed to be extended later with roles and permissions.
*/

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String username;

    // Encrypted user password (stored as a hash).
    @NotBlank
    private String password;

    // Spring Security authorities.
    // Currently returns an empty list (no roles assigned).
    // TODO: Can be extended later with ROLE_USER, ROLE_ADMIN, etc.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }


    // Account state flags required by Spring Security.
    // All return true for simplicity in this MVP implementation.
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}