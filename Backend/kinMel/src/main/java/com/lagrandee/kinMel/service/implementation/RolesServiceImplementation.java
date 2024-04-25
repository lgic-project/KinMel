package com.lagrandee.kinMel.service.implementation;

import com.lagrandee.kinMel.Repository.RoleRepository;
import com.lagrandee.kinMel.entity.Role;
import com.lagrandee.kinMel.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolesServiceImplementation implements RolesService {


    private RoleRepository roleRepository;

    @Autowired
    public RolesServiceImplementation(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getSpecificRoleById(int id) {
        Optional<Role> byId = roleRepository.findById(id);
        if (byId.isPresent()){
            return byId.get();
        }
        return null;
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public Role addOrUpdateRole(Role roles) {
        Role role=new Role("Admin","Controls all the system");
        return roleRepository.save(role);
    }
}
