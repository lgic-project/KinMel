package com.lagrandee.kinMel.Repository;

import com.lagrandee.kinMel.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

}
