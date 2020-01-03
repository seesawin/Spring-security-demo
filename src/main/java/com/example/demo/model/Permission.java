package com.example.demo.model;

import lombok.Data;

@Data
public class Permission {
    //主鍵id
    private int id;
    //許可權名稱
    private String name;
    //許可權描述
    private String descritpion;
    //授權連結
    private String url;
    //父節點id
    private int pid;
    //請求方式
    private String method;
}
