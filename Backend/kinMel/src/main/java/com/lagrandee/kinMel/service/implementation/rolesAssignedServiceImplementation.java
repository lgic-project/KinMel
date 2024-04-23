package com.lagrandee.kinMel.service.implementation;

import com.lagrandee.kinMel.Repository.RolesAssignedRepository;
import com.lagrandee.kinMel.entity.RolesAssigned;
import com.lagrandee.kinMel.entity.Users;
import com.lagrandee.kinMel.service.RolesAssignedService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class rolesAssignedServiceImplementation implements RolesAssignedService {

   private RolesAssignedRepository rolesAssignedRepository;


    @Override
    public List<Users> rolesAssigned(String roleName) {

        return null;//Here I have to write query
    }


}
