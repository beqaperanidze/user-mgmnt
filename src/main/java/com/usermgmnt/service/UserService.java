package com.usermgmnt.service;

import com.usermgmnt.dto.UserDTO;
import com.usermgmnt.dto.UserRegistrationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

//    UserDTO createUser(UserRegistrationDTO userRegistrationDTO);

    UserDTO getUserById(Long id);

    UserDTO getUserByEmail(String email);

    List<UserDTO> getAllUsers();

    UserDTO updateUser(Long id, UserRegistrationDTO updatedUser);

    void deleteUser(Long id);

    void deleteAllUsers();

    void deleteAllNonApproved();
}