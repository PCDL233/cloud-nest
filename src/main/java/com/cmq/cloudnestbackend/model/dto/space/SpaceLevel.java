package com.cmq.cloudnestbackend.model.dto.space;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 空间级别
 */
@Data
@AllArgsConstructor
public class SpaceLevel {

    /**
     * 值
     */
    private int value;

    /**
     * 文本
     */
    private String text;

    /**
     * 最大条数
     */
    private long maxCount;

    /**
     * 最大大小
     */
    private long maxSize;
}
