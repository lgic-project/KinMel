package com.lagrandee.kinMel.service;

import com.lagrandee.kinMel.entity.RolesAssigned;
import com.lagrandee.kinMel.entity.Users;

import java.util.List;

public interface RolesAssignedService {

List<Users> rolesAssigned(String roleName);

}
