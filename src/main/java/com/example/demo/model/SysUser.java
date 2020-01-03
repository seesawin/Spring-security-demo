package com.example.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class SysUser {
    //主鍵id
    private Integer id;
    //使用者名稱
    private String username;
    //登入密碼
    private String password;

    private List<SysRole> roles;
}
