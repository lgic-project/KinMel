package com.lagrandee.kinMel.service.implementation;

import com.lagrandee.kinMel.Repository.UsersRepository;
import com.lagrandee.kinMel.bean.UserDetail;
import com.lagrandee.kinMel.entity.Users;
import com.lagrandee.kinMel.service.UserService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    private final UsersRepository usersRepository;

    private final JdbcTemplate jdbcTemplate;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImplementation(UsersRepository usersRepository, JdbcTemplate jdbcTemplate, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Users getSpecificUserById(int id) {
        Optional<Users> result = usersRepository.findById(id);
        if (result.isPresent()){
            return result.get();
        }
        return null; //Throw error if not user
    }

    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }



    @Override
    public Users addOrUpdateUser(Users users) {
       users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
        return usersRepository.save(users);
    }

    @Override
    public void removeUser(int id) {
        usersRepository.deleteById(id);
    }

    @Override
    public UserDetail getUserWithRole(int id) {
       List<Object> argumentList =new ArrayList<>();
       StringBuilder sql=new StringBuilder();
       sql.append("SELECT ").append(" users.user_id,users.first_name,users.last_name,users.email,users.address ,users.phone_number,users.profile_photo ");
       sql.append(" FROM ").append("  users ")
               .append(" where users.user_id = ?");
       argumentList.add(id);
       return jdbcTemplate.queryForObject(sql.toString(),(rs,rowName)->{
          UserDetail usersWithRoles = new UserDetail();
          usersWithRoles.setUserId(rs.getInt("user_id"));
          usersWithRoles.setFirst_name(rs.getString("first_name"));
          usersWithRoles.setLast_name(rs.getString("last_name"));
          usersWithRoles.setRoles(jdbcTemplate.queryForList("select roles.name from roles inner join roles_Assigned on roles_Assigned.role_id=roles.role_id where roles_Assigned.user_id=?",String.class,id));
          usersWithRoles.setEmail(rs.getString("email"));
          usersWithRoles.setEmail(rs.getString("address"));
          usersWithRoles.setPhoneNumber(rs.getString("phone_number"));
          usersWithRoles.setImage(rs.getString("profile_photo"));

          return usersWithRoles;
       },argumentList.toArray());
    }
}
