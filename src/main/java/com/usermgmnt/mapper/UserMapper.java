package com.usermgmnt.mapper;

import com.usermgmnt.dto.UserDTO;
import com.usermgmnt.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
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
