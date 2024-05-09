package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.UUID;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件接口
 */
@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file){

        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称
            String objectName = UUID.randomUUID().toString() + extension;

            //文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.error(ResultCodeEnum.UPLOAD_FAILED);
    }

    /**
     * wang-editor编辑器文件上传接口
     */
    @PostMapping("/editor/upload")
    public Map<String, Object> wangEditorUpload(MultipartFile file){

        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称
            String objectName = UUID.randomUUID().toString() + extension;

            //文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            // wangEditor上传图片成功后， 需要返回的参数
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("errno", 0);
            resMap.put("data", CollUtil.newArrayList(Dict.create().set("url", filePath)));
            return resMap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

/*    *//**
     * 获取文件
     *
     * @param flag
     * @param response
     *//*
    @GetMapping("/{flag}")   //  1697438073596-avatar.png
    public void avatarPath(@PathVariable String flag, HttpServletResponse response) {
        OutputStream os;
        try {
            if (StrUtil.isNotEmpty(flag)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(flag, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath + flag);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }

    *//**
     * 删除文件
     *
     * @param flag
     *//*
    @DeleteMapping("/{flag}")
    public void delFile(@PathVariable String flag) {
        FileUtil.del(filePath + flag);
        System.out.println("删除文件" + flag + "成功");
    }*/


}
