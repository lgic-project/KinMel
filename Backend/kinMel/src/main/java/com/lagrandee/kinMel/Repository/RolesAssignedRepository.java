package com.lagrandee.kinMel.Repository;

import com.lagrandee.kinMel.entity.RolesAssigned;
import com.lagrandee.kinMel.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RolesAssignedRepository extends JpaRepository<RolesAssigned,Integer> {

}
