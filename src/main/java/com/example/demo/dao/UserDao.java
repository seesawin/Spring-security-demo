package com.example.demo.dao;

import com.example.demo.model.SysUser;

public interface UserDao {
    public SysUser findByUserName(String username);
}
