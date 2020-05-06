package org.example.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    private Integer id;

    private String userSerial;

    private String username;

    private String userPassword;

    private String roleName;

    private String userPictureIndex;

}

