package com.cmq.cloudnestbackend.aop;


import com.cmq.cloudnestbackend.annotation.AuthCheck;
import com.cmq.cloudnestbackend.exception.BusinessException;
import com.cmq.cloudnestbackend.exception.ErrorCode;
import com.cmq.cloudnestbackend.model.entity.User;
import com.cmq.cloudnestbackend.model.enums.UserRoleEnum;
import com.cmq.cloudnestbackend.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 权限校验拦截器
     *
     * @param joinPoint 连接点
     * @param authCheck 权限注解
     * @return 连接点执行结果
     * @throws Throwable 异常
     */
    @Around(value = "@annotation(authCheck)")
    public Object doIntercept(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
        //不需要权限，放行
        if (mustRoleEnum == null) {
            return joinPoint.proceed();
        }
        //必须具有某个角色才能访问
        UserRoleEnum userEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        if (userEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //要求必须有管理员权限
        if (UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //放行
        return joinPoint.proceed();
    }
}