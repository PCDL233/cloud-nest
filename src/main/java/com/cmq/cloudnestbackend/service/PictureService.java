package com.cmq.cloudnestbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmq.cloudnestbackend.model.dto.picture.PictureUploadRequest;
import com.cmq.cloudnestbackend.model.entity.Picture;
import com.cmq.cloudnestbackend.model.entity.User;
import com.cmq.cloudnestbackend.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

public interface PictureService extends IService<Picture> {

    /**
     * 上传图片
     *
     * @param multipartFile        图片文件
     * @param pictureUploadRequest 图片上传请求
     * @param loginUser            登录用户
     * @return 上传结果
     */
    PictureVO uploadPicture(MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest, User loginUser);
}
