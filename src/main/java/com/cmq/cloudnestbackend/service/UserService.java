package com.cmq.cloudnestbackend.service;

import com.cmq.cloudnestbackend.model.dto.UserRegisterRequest;
import com.cmq.cloudnestbackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmq.cloudnestbackend.model.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CMQ233
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2025-03-28 20:47:28
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求体
     * @return 注册成功的用户ID
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @return 登录成功的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取加密后的密码
     *
     * @param password 明文密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String password);

    /**
     * 获取当前登录用户
     *
     * @param request HttpServletRequest
     * @return 当前登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取脱敏类的用户信息
     *
     * @param user 用户
     * @return 用户脱敏后的信息(脱敏)
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 用户注销
     *
     * @param request HttpServletRequest
     * @return 是否注销成功
     */
    Boolean userLogout(HttpServletRequest request);
}
