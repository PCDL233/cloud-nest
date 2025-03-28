package com.cmq.cloudnestbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 7175315434776333917L;

    /**
     * 账号
     */
    private String userAccount;
    /**
     * 密码
     */
    private String userPassword;
}
