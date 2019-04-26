package com.lexian.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;


/**
 * @author luozidong
 */
public class UploadingImageUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadingImageUtil.class);

    /**
     * 构造一个带指定Zone对象的配置类
     */
    private static Configuration cfg = new Configuration(Zone.zone0());

    /**
     * ...其他参数参考类注释
     */
    private static UploadManager uploadManager = new UploadManager(cfg);
    /**
     * ...生成上传凭证，然后准备上传
     */
    private static final String ACCESS_KEY = "tVPLHCcsp3MW1lmJf06Kn0eXsOojeR23VXrFI4VX";
    private static final String SECRET_KEY = "ssxqrjrY916T5iZt6beYpVkBNMXRbP4wfgbPPUD2";

    private static final String BUCKET = "lexian";// 上传到指定的七牛云存储的一个空间中
    private static String key = null;// 默认不指定key的情况下，以文件内容的hash值作为文件名（上传后的文件名）

    public static String upload(CommonsMultipartFile file) {
        String upToken;
        InputStream is = null;
        try {
            long startTime = System.currentTimeMillis();
            key = startTime + file.getOriginalFilename();

            //把文件转化为字节数组
            is = file.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            byte[] uploadBytes = bos.toByteArray();

            //进行七牛的操作，不懂去七牛的sdk上看
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            upToken = auth.uploadToken(BUCKET);

            //默认上传接口回复对象
            DefaultPutRet putRet;

            //进行上传操作，传入文件的字节数组，文件名，上传空间，得到回复对象
            Response response = uploadManager.put(uploadBytes, key, upToken);
            putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

            //key文件名
            LOGGER.info("putRet.key - {}", putRet.key);
            LOGGER.info("putRet.hash - {}", putRet.hash);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                assert is != null;
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //得到上传文件的文件名，并赋值给key作为七牛存储的文件名
        return key;
    }

}