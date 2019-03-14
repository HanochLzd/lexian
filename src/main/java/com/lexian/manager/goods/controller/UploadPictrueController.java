package com.lexian.manager.goods.controller;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.lexian.utils.Constant;
import com.lexian.utils.UrlContant;
import com.lexian.web.ResultHelper;
import com.lexian.web.UploadingImageUtil;

/**
 * @author luozidong
 */
@Controller
@RequestMapping("uploadPicture")
public class UploadPictrueController {

    @ResponseBody
    @RequestMapping("uploadSinglePic.do")
    public ResultHelper uploadMainPic(HttpServletRequest request) throws IOException {
        String newPictureUrl = null;

        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> ite = multiRequest.getFileNames();
            while (ite.hasNext()) {
                MultipartFile file = multiRequest.getFile(ite.next());
                CommonsMultipartFile fi = (CommonsMultipartFile) file;
                String newName = UploadingImageUtil.upload(fi);
                newPictureUrl = UrlContant.qiNiuUrl + "/" + newName;
            }
        }

        return new ResultHelper(Constant.CODE_SUCCESS, newPictureUrl);
    }
}
