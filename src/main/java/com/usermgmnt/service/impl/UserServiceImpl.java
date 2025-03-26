package com.usermgmnt.service.impl;

import com.usermgmnt.dto.UserDTO;
import com.usermgmnt.dto.UserRegistrationDTO;
import com.usermgmnt.enums.Role;
import com.usermgmnt.exceptions.EmailAlreadyExistsException;
import com.usermgmnt.model.User;
import com.usermgmnt.repository.UserRepository;
import com.usermgmnt.service.UserService;
import com.usermgmnt.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public UserDTO createUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use: %s".formatted(userRegistrationDTO.getEmail()));
        }

        User user = new User();
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPassword(userRegistrationDTO.getPassword());
        user.setFirstName(userRegistrationDTO.getFirstName());
        user.setLastName(userRegistrationDTO.getLastName());
        user.setAge(userRegistrationDTO.getAge());
        user.setApproved(false);
        user.setRole(Role.USER);

        return mapToUserDto(userRepository.save(user));
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: %d".formatted(id)));
        return mapToUserDto(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with Email: %s".formatted(email)));
        return mapToUserDto(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToUserDto).toList();
    }

    @Transactional
    @Override
    public UserDTO updateUser(Long id, UserRegistrationDTO updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: %d".formatted(id)));

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new EmailAlreadyExistsException("Email already in use: %s".formatted(updatedUser.getEmail()));
            }
            user.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getFirstName() != null) {
            user.setFirstName(updatedUser.getFirstName());
        }

        if (updatedUser.getLastName() != null) {
            user.setLastName(updatedUser.getLastName());
        }

        if (updatedUser.getAge() != null) {
            user.setAge(updatedUser.getAge());
        }

        if (updatedUser.getPassword() != null) {
            user.setPassword(updatedUser.getPassword());
        }

        return mapToUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: %d".formatted(id)));
        userRepository.deleteById(id);
    }

    public UserDTO mapToUserDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setAge(user.getAge());
        userDTO.setApproved(user.getApproved());
        userDTO.setRole(user.getRole());
        return userDTO;

    }
}
