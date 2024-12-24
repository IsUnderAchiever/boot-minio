package org.tong.controller;

import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tong.config.MinioConfig;
import org.tong.util.JsonData;
import org.tong.util.MinioUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Auther: Administrator
 * @Date: 2024/12/24/22:28
 * @Description:
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private MinioConfig prop;

    /**
     * 查看存储bucket是否存在
     *
     * @param bucketName
     * @return
     */
    @GetMapping("/bucketExists")
    public JsonData bucketExists(@RequestParam("bucketName") String bucketName) {
        return JsonData.buildSuccess(minioUtil.bucketExists(bucketName));
    }

    /**
     * 创建存储bucket
     *
     * @param bucketName
     * @return
     */
    @GetMapping("/makeBucket")
    public JsonData makeBucket(String bucketName) {
        return JsonData.buildSuccess(minioUtil.makeBucket(bucketName));
    }

    /**
     * 删除存储bucket
     *
     * @param bucketName
     * @return
     */
    @GetMapping("/removeBucket")
    public JsonData removeBucket(String bucketName) {
        return JsonData.buildSuccess(minioUtil.removeBucket(bucketName));
    }

    /**
     * 获取全部bucket
     *
     * @return
     */
    @GetMapping("/getAllBuckets")
    public JsonData getAllBuckets() {
        Set<String> collect = minioUtil.getAllBuckets().stream().map(Bucket::name).collect(Collectors.toSet());
        return JsonData.buildSuccess(collect);
    }

    /**
     * 文件上传返回url
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public JsonData upload(@RequestParam("file") MultipartFile file) {
        String objectName = minioUtil.upload(file);
        if (null != objectName) {
            return JsonData.buildSuccess((prop.getEndpoint() + "/" + prop.getBucketName() + "/" + objectName));
        }
        return JsonData.buildError("上传失败");
    }

    /**
     * 图片/视频预览
     *
     * @param fileName
     * @return
     */
    @GetMapping("/preview")
    public JsonData preview(@RequestParam("fileName") String fileName) {
        return JsonData.buildSuccess(minioUtil.preview(fileName));
    }

    /**
     * 文件下载
     *
     * @param fileName
     * @param res
     * @return
     */
    @GetMapping("/download")
    public JsonData download(@RequestParam("fileName") String fileName, HttpServletResponse res) {
        minioUtil.download(fileName, res);
        return JsonData.buildSuccess();
    }

    /**
     * 根据url地址删除文件
     *
     * @param url
     * @return
     */
    @PostMapping("/delete")
    public JsonData remove(String url) {
        boolean remove = minioUtil.remove(url);
        return JsonData.buildSuccess(remove);
    }
}
