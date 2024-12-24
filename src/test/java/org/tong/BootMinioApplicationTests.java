package org.tong;

import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.tong.config.MinioConfig;
import org.tong.util.MinioUtil;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@SpringBootTest
class BootMinioApplicationTests {
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private MinioConfig prop;

    @Test
    void contextLoads() {
    }

    /**
     * 查看存储bucket是否存在
     *
     * @param bucketName
     * @return
     */
    public Boolean bucketExists(String bucketName) {
        Boolean flag = minioUtil.bucketExists(bucketName);
        log.info("桶{}:{}",bucketName,flag?"存在":"不存在");
        return flag;
    }

    /**
     * 创建存储bucket
     *
     * @param bucketName
     * @return
     */
    public Boolean makeBucket(String bucketName) {
        Boolean flag = minioUtil.makeBucket(bucketName);
        log.info("桶{}:创建{}",bucketName,flag?"成功":"失败");
        return flag;
    }

    /**
     * 删除存储bucket
     *
     * @param bucketName
     * @return
     */
    public Boolean removeBucket(String bucketName) {
        Boolean flag = minioUtil.removeBucket(bucketName);
        log.info("桶{}:删除{}",bucketName,flag?"成功":"失败");
        return flag;
    }

    /**
     * 获取全部bucket
     *
     * @return
     */
    public List<Bucket> getAllBuckets() {
        List<Bucket> allBuckets = minioUtil.getAllBuckets();
        allBuckets.forEach(item->{
            log.info("桶名：{}" , item.name());
        });
        return allBuckets;
    }

    /**
     * 文件上传返回url
     *
     * @param file
     * @return
     */
    public String upload(MultipartFile file) {
        String objectName = minioUtil.upload(file);
        String url="";
        if (null != objectName) {
            url=prop.getEndpoint() + "/" + prop.getBucketName() + "/" + objectName;
        }
        log.info("url:{}",url);
        return url;
    }

    /**
     * 图片/视频预览
     *
     * @param fileName
     * @return
     */
    public String preview(String fileName) {
        String preview = minioUtil.preview(fileName);
        log.info("url:{}",preview);
        return preview;
    }

    /**
     * 根据url地址删除文件
     * @param url
     * @return
     */
    public void remove(String url) {
        String objName = url.substring(url.lastIndexOf(prop.getBucketName() + "/") + prop.getBucketName().length() + 1);
        boolean flag = minioUtil.remove(objName);
        log.info("删除{}",flag?"成功":"失败");
    }
}
