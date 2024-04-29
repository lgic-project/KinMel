package com.lagrandee.kinMel.service.implementation;

import com.lagrandee.kinMel.KinMelCustomMessage;
import com.lagrandee.kinMel.Repository.UsersRepository;
import com.lagrandee.kinMel.bean.UserDetail;
import com.lagrandee.kinMel.bean.request.UsersRegisterDTO;
import com.lagrandee.kinMel.entity.Role;
import com.lagrandee.kinMel.entity.Users;
import com.lagrandee.kinMel.exception.UnableToSendMailException;
import com.lagrandee.kinMel.exception.UserAlreadyExistsException;
import com.lagrandee.kinMel.helper.Image.ImageUtils;
import com.lagrandee.kinMel.helper.StaticPaths;
import com.lagrandee.kinMel.helper.emailhelper.EmailUtil;
import com.lagrandee.kinMel.helper.emailhelper.OtpGenerate;
import com.lagrandee.kinMel.service.UserService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UsersRepository usersRepository;

    private final JdbcTemplate jdbcTemplate;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final OtpGenerate otpGenerate;

    private EmailUtil emailUtil;



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
    public ResponseEntity<?> registerUser(UsersRegisterDTO usersRegisterDTO)  {
        Users checkEmail = usersRepository.findByEmail(usersRegisterDTO.getEmail());
        if (checkEmail!=null){
            throw new UserAlreadyExistsException("User already exists with email ");
        }
        String otp = otpGenerate.generateOTP();
        String mail=usersRegisterDTO.getEmail();
        try {
            emailUtil.sendOtpInMail(mail,otp);
        } catch (MessagingException e) {
            throw new UnableToSendMailException("Unable to send otp.Please try again");
        }
        Users users=new Users();
        users.setId(0);
        users.setFirstName(usersRegisterDTO.getFirstName());
        users.setLastName(usersRegisterDTO.getLastName());
        users.setEmail(mail);
        users.setPassword(bCryptPasswordEncoder.encode(usersRegisterDTO.getPassword()));
        users.setAddress(usersRegisterDTO.getAddress());
        users.setPhoneNumber(usersRegisterDTO.getPhoneNumber());
        users.setProfilePhoto(usersRegisterDTO.getProfilePhoto());
        String profilePhoto = usersRegisterDTO.getProfilePhoto();
         String imageUploadPath= StaticPaths.profilePath;
        String savedImagePath = null;

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            try {
                savedImagePath = ImageUtils.saveDecodedImage(profilePhoto, imageUploadPath,usersRegisterDTO.getImageFormat());
            } catch (IOException e) {
                throw new RuntimeException("Image cannot be saved");
            }
        }

        if (savedImagePath != null) {
            users.setProfilePhoto(savedImagePath);
        } else {
            users.setProfilePhoto("/images/default.png");
        }
        users.setOtp(otp);
        users.setOtpGeneratedTime(LocalDateTime.now());
        users.setActive(0);
        Set<Role> roles = new HashSet<>();
        Role role=new Role();
        role.setId(usersRegisterDTO.getRole());
        if (usersRegisterDTO.getRole()==1){
            role.setName("Admin");
            role.setDescription("Manages Everything");
        }
        if (usersRegisterDTO.getRole()==2){
            role.setName("Customer");
            role.setDescription("Buys Product");
        }
        if (usersRegisterDTO.getRole()==3){
            role.setName("Seller");
            role.setDescription("Sells Product");
        }
        roles.add(role);
        users.setRoles(roles);
        usersRepository.save(users);
        KinMelCustomMessage customMessage=new KinMelCustomMessage(HttpStatus.OK.value(),"User Registration successful",System.currentTimeMillis());
        return new ResponseEntity<>(customMessage, HttpStatus.OK);
    }

    public ResponseEntity<?> updateUser(UsersRegisterDTO usersRegisterDTO) {
        Optional<Users> usersOptional = usersRepository.findById(usersRegisterDTO.getId());
        if (usersOptional.isPresent()) {
            Users users = usersOptional.get();
            users.setFirstName(usersRegisterDTO.getFirstName());
            users.setLastName(usersRegisterDTO.getLastName());
            users.setPassword(bCryptPasswordEncoder.encode(usersRegisterDTO.getPassword()));
            users.setAddress(usersRegisterDTO.getAddress());
            users.setPhoneNumber(usersRegisterDTO.getPhoneNumber());
            users.setProfilePhoto(usersRegisterDTO.getProfilePhoto());
            String profilePhoto = usersRegisterDTO.getProfilePhoto();
            String imageUploadPath = StaticPaths.profilePath;
            String savedImagePath = null;

            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                try {
                    savedImagePath = ImageUtils.saveDecodedImage(profilePhoto, imageUploadPath, usersRegisterDTO.getImageFormat());
                } catch (IOException e) {
                    throw new RuntimeException("Image cannot be saved");
                }
            }

            if (savedImagePath != null) {
                users.setProfilePhoto(savedImagePath);
            } else {
                users.setProfilePhoto("/images/default.png");
            }
            usersRepository.save(users);
            KinMelCustomMessage customMessage = new KinMelCustomMessage(HttpStatus.OK.value(), "User successfully edited", System.currentTimeMillis());
            return new ResponseEntity<KinMelCustomMessage>(customMessage, HttpStatus.OK);
        }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

    public String verifyAccount(String email, String otp) {
        Users byEmail = usersRepository.findByEmail(email);
        if (byEmail.getOtp().equals(otp) && Duration.between(byEmail.getOtpGeneratedTime(),LocalDateTime.now()).getSeconds()<(5*60)){
            byEmail.setActive(1);
            usersRepository.save(byEmail);
    return "OTP verified.Now you can login";
        }
        return "Please regenerate otp and try again";
    }

    public String regenerateOTP(String email) {
        Users users=usersRepository.findByEmail(email);
        String newOtp = otpGenerate.generateOTP();
        try {
            emailUtil.sendOtpInMail(email,newOtp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp.Please try again");
        }
        users.setOtp(newOtp);
        users.setOtpGeneratedTime(LocalDateTime.now());
        usersRepository.save(users);
        return "New OTP Send Successfully";
    }

}