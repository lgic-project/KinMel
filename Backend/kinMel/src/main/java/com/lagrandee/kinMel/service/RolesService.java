package com.lagrandee.kinMel.service;


import com.lagrandee.kinMel.entity.Role;

import java.util.List;


public interface RolesService {
    Role getSpecificRoleById(int id);

    List<Role> getAllRole();

    Role addOrUpdateRole(Role roles);

}
