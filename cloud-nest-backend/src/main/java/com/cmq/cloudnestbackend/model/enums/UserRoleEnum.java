package com.cmq.cloudnestbackend.model.enums;

import lombok.Getter;
import org.springframework.util.ObjectUtils;

/**
 * 用户角色枚举
 */
@Getter
public enum UserRoleEnum {
    USER("普通用户", "user"),
    ADMIN("管理员", "admin");

    private final String text;
    private final String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据值获取枚举
     *
     * @param value 枚举值
     * @return 枚举对象
     */
    public static UserRoleEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (UserRoleEnum role : UserRoleEnum.values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }
        return null;
    }
}
