package com.usermgmnt.dto;

import com.usermgmnt.enums.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long Id;
    private String email;
    private String firstName;
    private String lastName;
    private Integer age;
    private Boolean approved;
    private Role role;
}
