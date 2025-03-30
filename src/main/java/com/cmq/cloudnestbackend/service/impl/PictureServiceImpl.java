package com.cmq.cloudnestbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmq.cloudnestbackend.exception.ErrorCode;
import com.cmq.cloudnestbackend.exception.ThrowUtils;
import com.cmq.cloudnestbackend.manager.FileManager;
import com.cmq.cloudnestbackend.mapper.PictureMapper;
import com.cmq.cloudnestbackend.model.dto.file.UploadPictureResult;
import com.cmq.cloudnestbackend.model.dto.picture.PictureUploadRequest;
import com.cmq.cloudnestbackend.model.entity.Picture;
import com.cmq.cloudnestbackend.model.entity.User;
import com.cmq.cloudnestbackend.model.vo.PictureVO;
import com.cmq.cloudnestbackend.service.PictureService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
        implements PictureService {

    @Resource
    private FileManager fileManager;

    @Override
    public PictureVO uploadPicture(MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest, User loginUser) {
        //1.校验参数
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NO_AUTH_ERROR);
        //2.判断操作是新增还是更新
        Long pictureId = null;
        if (pictureUploadRequest != null) {
            pictureId = pictureUploadRequest.getId();
        }
        //更新操作，判断图片是否存在
        if (pictureId != null) {
            boolean exists = this.lambdaQuery()
                    .eq(Picture::getId, pictureId)
                    .exists();
            ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "图片不存在");
        }
        //3.上传图片
        //按照用户ID分文件夹
        String uploadPathPrefix = String.format("public/%s", loginUser.getId());
        UploadPictureResult uploadPictureResult = fileManager.uploadPicture(multipartFile, uploadPathPrefix);
        //构造图片对象
        Picture picture = new Picture();
        picture.setUrl(uploadPictureResult.getUrl());
        picture.setName(uploadPictureResult.getPicName());
        picture.setPicSize(uploadPictureResult.getPicSize());
        picture.setPicWidth(uploadPictureResult.getPicWidth());
        picture.setPicHeight(uploadPictureResult.getPicHeight());
        picture.setPicScale(uploadPictureResult.getPicScale());
        picture.setPicFormat(uploadPictureResult.getPicFormat());
        picture.setUserId(loginUser.getId());
        //保存图片信息到数据库
        if (pictureId != null) {
            //新增操作:补充图片id和修改时间
            picture.setId(pictureId);
            picture.setUpdateTime(new Date());
        }
        boolean result = this.saveOrUpdate(picture);
        ThrowUtils.throwIf(!result, ErrorCode.SYSTEM_ERROR, "图片上传失败");
        //4.返回结果
        return PictureVO.objToVo(picture);
    }
}




