package com.exclub.exclub_league.Relationship.entity;

import com.exclub.exclub_league.Team.entity.Team;
import com.exclub.exclub_league.User.entity.Role;
import com.exclub.exclub_league.User.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "user_team_roles")
public class UserTeamRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
