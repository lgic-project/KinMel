package com.lagrandee.kinMel.service.implementation;

import com.lagrandee.kinMel.KinMelCustomMessage;
import com.lagrandee.kinMel.Repository.UsersRepository;
import com.lagrandee.kinMel.bean.UserDetail;
import com.lagrandee.kinMel.bean.request.PasswordRequest;
import com.lagrandee.kinMel.bean.request.UsersRegisterDTO;
import com.lagrandee.kinMel.bean.response.KhaltiResponse;
import com.lagrandee.kinMel.entity.Role;
import com.lagrandee.kinMel.entity.Users;
import com.lagrandee.kinMel.exception.KhaltiPaymentException;
import com.lagrandee.kinMel.exception.UnableToSendMailException;
import com.lagrandee.kinMel.exception.UserAlreadyExistsException;
import com.lagrandee.kinMel.exception.UserNotVerified;
import com.lagrandee.kinMel.helper.Image.ImageUtils;
import com.lagrandee.kinMel.helper.KhaltiPaymentRequest;
import com.lagrandee.kinMel.helper.KhaltiPaymentService;
import com.lagrandee.kinMel.helper.StaticPaths;
import com.lagrandee.kinMel.helper.emailhelper.EmailUtil;
import com.lagrandee.kinMel.helper.emailhelper.OtpGenerate;
import com.lagrandee.kinMel.security.JwtUtils;
import com.lagrandee.kinMel.security.WebSecurityConfig;
import com.lagrandee.kinMel.security.filter.AuthTokenFilter;
import com.lagrandee.kinMel.service.UserService;
import com.lagrandee.kinMel.service.fileupload.FileUploadService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static com.lagrandee.kinMel.helper.Image.ImageUtils.encodeImageToBase64;

@Service
@AllArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UsersRepository usersRepository;

    private final JdbcTemplate jdbcTemplate;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final OtpGenerate otpGenerate;

    private EmailUtil emailUtil;

    private final JwtUtils jwtTokenProvider;

    private final AuthTokenFilter authTokenFilter;
    private final KhaltiPaymentService khaltiPaymentService;




    @Override
    public Users getSpecificUserById(int id) {

        Optional<Users> result = usersRepository.findById(id);
        if (result.isPresent()){
            return result.get();
        }

        throw new UsernameNotFoundException("User doesn't exist");
    }


    public Integer isUserActive(Integer userId) {
        return usersRepository.isUserVerified(userId);
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
        String imageUploadPath= null;
        try {
            imageUploadPath = StaticPaths.getProfilePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            users.setProfilePhoto(StaticPaths.getProfileDefaultPath());
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
        if (usersRegisterDTO.getRole()==3) {
            role.setName("Seller");
            role.setDescription("Sells Product");
        }
        roles.add(role);
        users.setRoles(roles);
        roles.add(role);
        users.setRoles(roles);
        usersRepository.save(users);
        if (usersRegisterDTO.getRole()==3) {

            KhaltiPaymentRequest khaltiRequest = new KhaltiPaymentRequest();
            khaltiRequest.setReturn_url("https://devkotasuman.com.np");
            khaltiRequest.setWebsite_url("https://devkotasuman.com.np");
            khaltiRequest.setAmount(1000);
            khaltiRequest.setPurchase_order_id(String.valueOf(users.getId()));
            khaltiRequest.setPurchase_order_name(users.getFirstName());
            khaltiRequest.setCustomer_info(new KhaltiPaymentRequest.CustomerInfo(users.getFirstName(), "suman.yhhits@gmail.com", users.getPhoneNumber().toString()));
            try {
                KhaltiResponse khaltiResponse = khaltiPaymentService.initiatePayment(khaltiRequest);
                return new ResponseEntity<>(khaltiResponse, HttpStatus.OK);
            } catch (KhaltiPaymentException e) {
                System.out.println("Khalti payment error: " + e.getMessage());
                // Handle Khalti payment error
            }
        }
        KinMelCustomMessage customMessage=new KinMelCustomMessage(HttpStatus.OK.value(),"User Registration successful",System.currentTimeMillis());
        return new ResponseEntity<>(customMessage, HttpStatus.OK);

    }
    public ResponseEntity<?> updateUser(UsersRegisterDTO usersRegisterDTO, HttpServletRequest request) {
        String token = authTokenFilter.parseJwt(request);
        Integer userId = jwtTokenProvider.getUserIdFromJWT(token);
        Optional<Users> usersOptional = usersRepository.findById(userId);
        if (usersOptional.isPresent()) {
            Users users = usersOptional.get();
            users.setFirstName(usersRegisterDTO.getFirstName());
            users.setLastName(usersRegisterDTO.getLastName());
            users.setAddress(usersRegisterDTO.getAddress());
            users.setPhoneNumber(usersRegisterDTO.getPhoneNumber());
            if(usersRegisterDTO.getProfilePhoto()!=null){
                users.setProfilePhoto(usersRegisterDTO.getProfilePhoto());
                String profilePhoto = usersRegisterDTO.getProfilePhoto();
                String imageUploadPath = null;
                try {
                    imageUploadPath = StaticPaths.getProfilePath();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
                    users.setProfilePhoto(StaticPaths.getProfileDefaultPath());
                }
            }
            usersRepository.save(users);
            KinMelCustomMessage customMessage = new KinMelCustomMessage(HttpStatus.OK.value(), "User successfully edited", System.currentTimeMillis());
            return new ResponseEntity<KinMelCustomMessage>(customMessage, HttpStatus.OK);
        }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
          usersWithRoles.setAddress(rs.getString("address"));
          usersWithRoles.setPhoneNumber(rs.getString("phone_number"));
           String imagePath = rs.getString("profile_photo");
           String imageBase64 = null;
           try {
               imageBase64 = encodeImageToBase64(imagePath);
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
           usersWithRoles.setProfilePicture(imageBase64);
//          usersWithRoles.setImage(rs.getString("profile_photo"));

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

    @Override
    public List<UserDetail> getAllUsers() {
        List<UserDetail> userDetails = new ArrayList<>();
        List<Users> users = usersRepository.findAll();

        for (Users user : users) {
            UserDetail userDetail = new UserDetail();
            userDetail.setFirst_name(user.getFirstName());
            userDetail.setLast_name(user.getLastName());
            userDetail.setEmail(user.getEmail());
            userDetail.setUserId(user.getId());
            userDetail.setAddress(user.getAddress());
            userDetail.setPhoneNumber(String.valueOf(user.getPhoneNumber()));
            String profilePhoto = user.getProfilePhoto();
            String imageBase64 ;
            try {
                imageBase64 = encodeImageToBase64(profilePhoto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            userDetail.setProfilePicture(imageBase64);
            List<String> roleNames = new ArrayList<>();

            // Get the set of roles for the user
            Set<Role> roles = user.getRoles();

            // Iterate through the roles and add the role names to the list
            for (Role role : roles) {
                roleNames.add(role.getName());
            }

            // Set the list of role names to the UserDetail object
            userDetail.setRoles(roleNames);
            // Set other fields as needed
            userDetails.add(userDetail);
        }

        return userDetails;
    }

    public UserDetail getUserWithRoleByToken(HttpServletRequest request) {
        String token = authTokenFilter.parseJwt(request);
        Integer id = jwtTokenProvider.getUserIdFromJWT(token);
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
            usersWithRoles.setAddress(rs.getString("address"));
            usersWithRoles.setPhoneNumber(rs.getString("phone_number"));
            String imagePath = rs.getString("profile_photo");
            String imageBase64 = null;
            try {
                imageBase64 = encodeImageToBase64(imagePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            usersWithRoles.setProfilePicture(imageBase64);
//          usersWithRoles.setImage(rs.getString("profile_photo"));

            return usersWithRoles;
        },argumentList.toArray());
    }

    public String updatePassword(PasswordRequest passwordRequest, HttpServletRequest request) {
        String token = authTokenFilter.parseJwt(request);
        Integer userId = jwtTokenProvider.getUserIdFromJWT(token);
        Optional<Users> usersOptional = usersRepository.findById(userId);
        if (usersOptional.isEmpty()) {
            throw new UserNotVerified("User Not Found");
        }
        Users users = usersOptional.get();
        if (bCryptPasswordEncoder.matches(passwordRequest.getOldPassword(), users.getPassword())) {
            users.setPassword(bCryptPasswordEncoder.encode(passwordRequest.getNewPassword()));
            usersRepository.save(users);
            return "Password Changed";
        }
        else {
        return "Password Don't Match";
        }
    }
}
