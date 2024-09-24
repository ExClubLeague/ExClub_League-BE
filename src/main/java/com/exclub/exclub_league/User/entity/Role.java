package com.exclub.exclub_league.User.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 역할의 고유 식별

    @Column(unique = true, nullable = false)
    private String name; // e.g., ROLE_USER, ROLE_ADMIN, ROLE_CAPTAIN자, ROLE_COORDINATOR, ROLE_MEMBER

    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "roles")
    private Set<User> users;
}