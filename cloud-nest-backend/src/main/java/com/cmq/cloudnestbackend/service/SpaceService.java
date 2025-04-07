package com.cmq.cloudnestbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmq.cloudnestbackend.model.dto.space.SpaceAddRequest;
import com.cmq.cloudnestbackend.model.dto.space.SpaceQueryRequest;
import com.cmq.cloudnestbackend.model.entity.Space;
import com.cmq.cloudnestbackend.model.entity.User;
import com.cmq.cloudnestbackend.model.vo.SpaceVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author CMQ233
 * @description 针对表【space(空间)】的数据库操作Service
 * @createDate 2025-04-07 19:17:13
 */
public interface SpaceService extends IService<Space> {

    /**
     * 创建空间
     *
     * @param spaceAddRequest 空间创建请求
     * @param loginUser       登录用户
     * @return 新增的空间id
     */
    long addSpace(SpaceAddRequest spaceAddRequest, User loginUser);

    /**
     * 校验空间参数
     *
     * @param space 空间对象
     * @param add   是否是在添加的时候校验
     */
    void validSpace(Space space, boolean add);

    /**
     * 获取查询条件
     *
     * @param spaceQueryRequest 空间查询请求
     * @return 空间查询条件
     */
    QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest);


    /**
     * 获取空间信息包装类(单个)
     *
     * @param space   空间对象
     * @param request HttpServletRequest
     * @return 空间信息包装类
     */
    SpaceVO getSpaceVO(Space space, HttpServletRequest request);

    /**
     * 获取空间信息包装类(分页)
     *
     * @param spacePage 空间分页对象
     * @param request   HttpServletRequest
     * @return 空间信息包装类分页对象
     */
    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage, HttpServletRequest request);

    /**
     * 根据空间等级填充空间对象
     *
     * @param space 空间对象
     */
    void fillSpaceBySpaceLevel(Space space);
}
