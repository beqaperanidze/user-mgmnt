package com.usermgmnt.service;

import com.usermgmnt.dto.UserDTO;
import com.usermgmnt.dto.UserRegistrationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface for managing user-related operations.
 */
@Service
public interface UserService {

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user
     * @return a {@link UserDTO} object containing the user's details
     */
    UserDTO getUserById(Long id);

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user
     * @return a {@link UserDTO} object containing the user's details
     */
    UserDTO getUserByEmail(String email);

    /**
     * Retrieves a list of all users in the system.
     *
     * @return a list of {@link UserDTO} objects containing details of all users
     */
    List<UserDTO> getAllUsers();

    /**
     * Updates the details of an existing user.
     *
     * @param id          the unique identifier of the user to be updated
     * @param updatedUser a {@link UserRegistrationDTO} object containing the updated user details
     * @return a {@link UserDTO} object containing the updated user's details
     */
    UserDTO updateUser(Long id, UserRegistrationDTO updatedUser);

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the unique identifier of the user to be deleted
     */
    void deleteUser(Long id);

    /**
     * Deletes all users in the system.
     */
    void deleteAllUsers();

    /**
     * Deletes all users who have not been approved.
     */
    void deleteAllNonApproved();
}