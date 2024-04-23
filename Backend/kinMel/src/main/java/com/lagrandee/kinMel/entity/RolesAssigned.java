package com.lagrandee.kinMel.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "roles_Assigned")
@Getter
@Setter
public class RolesAssigned {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roles_assigned_id", nullable = false)
    private Integer id;

//    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "user_id")
    private Integer user_iD;

//    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "role_id")
    private Integer role_Id;

}