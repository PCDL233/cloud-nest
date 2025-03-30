package com.cmq.cloudnestbackend.controller;

import com.cmq.cloudnestbackend.annotation.AuthCheck;
import com.cmq.cloudnestbackend.common.BaseResponse;
import com.cmq.cloudnestbackend.common.ResultUtils;
import com.cmq.cloudnestbackend.constant.UserConstant;
import com.cmq.cloudnestbackend.model.dto.picture.PictureUploadRequest;
import com.cmq.cloudnestbackend.model.entity.User;
import com.cmq.cloudnestbackend.model.vo.PictureVO;
import com.cmq.cloudnestbackend.service.PictureService;
import com.cmq.cloudnestbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/picture")
public class PictureController {
    @Resource
    private UserService userService;

    @Resource
    private PictureService pictureService;

    /**
     * 上传图片(可重复上传)
     *
     * @param multipartFile        图片文件
     * @param pictureUploadRequest 图片上传请求（图片id）
     * @param request              HttpServletRequest
     * @return 上传结果
     */
    @PostMapping("/upload")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<PictureVO> uploadPicture(@RequestParam("file") MultipartFile multipartFile,
                                                 PictureUploadRequest pictureUploadRequest,
                                                 HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        PictureVO pictureVO = pictureService.uploadPicture(multipartFile, pictureUploadRequest, loginUser);
        return ResultUtils.success(pictureVO);
    }
}
