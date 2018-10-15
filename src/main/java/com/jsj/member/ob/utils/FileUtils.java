/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.jsj.member.ob.utils;

import com.jsj.member.ob.dto.FileDto;
import com.jsj.member.ob.exception.TipException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * FileUtils.java
 *
 * @author Administrator
 * @version $Id: FileUtils.java, v 0.1 2016年3月21日 上午9:23:40 Administrator Exp $
 */
public class FileUtils {

    /**
     * 文件格式
     */
    private static String[] array = {"jpg", "png", "jpeg"};


    /**
     * 保存文件
     *
     * @param upfile
     * @param request
     * @return
     * @throws IOException
     */
    public static FileDto uploadFile(MultipartFile upfile, HttpServletRequest request) throws IOException {

        FileDto fileDto = new FileDto();

        String fileName = upfile.getOriginalFilename();
        String fkey = FileUtils.getFileKey(fileName);

        String savePath = request.getSession().getServletContext().getRealPath("/") + fkey;
        savePath = savePath.substring(0, savePath.lastIndexOf("/"));
        if (!new File(savePath).exists()) {
            new File(savePath).mkdirs();
        }

        File file = new File(request.getSession().getServletContext().getRealPath("/") + fkey);
        try {

            FileCopyUtils.copy(upfile.getInputStream(), new FileOutputStream(file));
            fileDto.setFile(file);
            fileDto.setFkey(fkey);
            return fileDto;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getFileKey(String name) {

        String prefix = "/upload/" + DateUtils.dateFormat(new Date(), "yyyy/MM");
        name = StringUtils.trimToNull(name);
        if (name == null) {
            return prefix + "/" +  com.jsj.member.ob.utils.StringUtils.UUID32() + "." + null;
        } else {
            name = name.replace('\\', '/');
            name = name.substring(name.lastIndexOf("/") + 1);
            int index = name.lastIndexOf(".");
            if (index < 0) {
                throw new TipException("上传文件后缀名错误");
            }
            String ext = StringUtils.trimToNull(name.substring(index + 1)).toLowerCase();
            if(StringUtils.isBlank(ext)){
                throw new TipException("上传文件后缀名错误");
            }
            if (!Arrays.asList(array).contains(ext)) {
                throw new TipException("不允许支持的文件类型：" + ext);
            }

            return prefix + "/" +  com.jsj.member.ob.utils.StringUtils.UUID32() + "." + ext;
        }
    }

}
