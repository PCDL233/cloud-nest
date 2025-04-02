package com.cmq.cloudnestbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmq.cloudnestbackend.model.dto.picture.PictureQueryRequest;
import com.cmq.cloudnestbackend.model.dto.picture.PictureReviewRequest;
import com.cmq.cloudnestbackend.model.dto.picture.PictureUploadByBatchRequest;
import com.cmq.cloudnestbackend.model.dto.picture.PictureUploadRequest;
import com.cmq.cloudnestbackend.model.entity.Picture;
import com.cmq.cloudnestbackend.model.entity.User;
import com.cmq.cloudnestbackend.model.vo.PictureVO;

import javax.servlet.http.HttpServletRequest;

public interface PictureService extends IService<Picture> {

    /**
     * 校验图片参数
     *
     * @param picture 图片对象
     */
    void validPicture(Picture picture);

    /**
     * 上传图片
     *
     * @param inputSource          文件输入源
     * @param pictureUploadRequest 图片上传请求
     * @param loginUser            登录用户
     * @return 上传结果
     */
    PictureVO uploadPicture(Object inputSource, PictureUploadRequest pictureUploadRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param pictureQueryRequest 图片查询请求
     * @return 图片查询条件
     */
    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);


    /**
     * 获取图片信息包装类(单个)
     *
     * @param picture 图片对象
     * @param request HttpServletRequest
     * @return 图片信息包装类
     */
    PictureVO getPictureVO(Picture picture, HttpServletRequest request);

    /**
     * 获取图片信息包装类(分页)
     *
     * @param picturePage 图片分页对象
     * @param request     HttpServletRequest
     * @return 图片信息包装类分页对象
     */
    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request);

    /**
     * 图片审核(仅管理员)
     *
     * @param pictureReviewRequest 图片审核请求
     * @param loginUser            登录用户
     */
    void doPictureReview(PictureReviewRequest pictureReviewRequest, User loginUser);

    /**
     * 填充审核参数
     *
     * @param picture   图片对象
     * @param loginUser 登录用户
     */
    void fillReviewParams(Picture picture, User loginUser);


    /**
     * 批量抓取和创建图片
     *
     * @param pictureUploadByBatchRequest 图片批量抓取图片请求
     * @param loginUser                   登录用户
     * @return 抓取的数量
     */
    Integer uploadPictureByBatch(PictureUploadByBatchRequest pictureUploadByBatchRequest, User loginUser);
}
