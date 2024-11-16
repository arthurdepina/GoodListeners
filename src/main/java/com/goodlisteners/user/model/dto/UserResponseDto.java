package com.goodlisteners.user.model.dto;

import com.goodlisteners.user.model.User;

public class UserResponseDto {

    private Integer userId;

    private String name;

    private String password;

    private String email;

    public UserResponseDto() {
    }

    public UserResponseDto(Integer userId, String name, String password, String email) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static UserResponseDto fromEntity(User user){
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setUserId(user.getUserId());
        responseDto.setName(user.getName());
        responseDto.setPassword(user.getPassword());
        responseDto.setEmail(user.getEmail());
        return responseDto;
    }
}
