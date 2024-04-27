package com.lagrandee.kinMel.Repository;

import com.lagrandee.kinMel.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {

    @Query(value = "select * from users where email=?1",nativeQuery = true)
   Users findByEmail(String email);
}
