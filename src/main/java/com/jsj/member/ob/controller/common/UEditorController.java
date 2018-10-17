package com.jsj.member.ob.controller.common;

import com.google.gson.JsonObject;
import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.utils.HttpUtils;
import com.jsj.member.ob.utils.TaleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

@ApiIgnore
@Controller
@RequestMapping("/ueditor")
public class UEditorController {


    @Autowired
    Webconfig webconfig;

    /**
     * ueditor编辑器
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/config")
    @ResponseBody
    public String config(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        ClassPathResource resource = new ClassPathResource("ueditor.config.json");
        InputStream inputStream = resource.getInputStream();

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();


    }

    /**
     * UEditor上传图片
     *
     * @param files
     * @param request
     * @return
     * @date: 2018年3月26日 下午12:16:47
     * @author zhn
     */
    @RequestMapping(value = "/uploadimage")
    @ResponseBody
    public String uploadimage(@RequestParam("upfile") MultipartFile[] files, HttpServletRequest request) throws IOException {

        JsonObject obj = new JsonObject();
        if (files.length <= 0) {
            obj.addProperty("state", "没有上传文件");
            return obj.toString();
        }

        MultipartFile upfile = files[0];
        String fname = upfile.getOriginalFilename();

        //判断是否上传到图片服务器
        if (webconfig.isImgServerSwitch()) {

            InputStream is = upfile.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();

            int len;
            byte[] bs = new byte[1024];
            while ((len = is.read(bs)) != -1) {
                bout.write(bs, 0, len);
            }

            byte[] bytes = bout.toByteArray();
            is.close();

            Map<String, Object> map = HttpUtils.uploadImg(bytes);

            if (Boolean.valueOf(map.get("Issuccess").toString())) {

                obj.addProperty("state", "SUCCESS");
                obj.addProperty("url", map.get("Url").toString());
                obj.addProperty("title", "");
                obj.addProperty("original", fname);
                obj.addProperty("fkey", map.get("MD5").toString());
            } else {
                obj.addProperty("state", map.get("ErrorMessage").toString());
            }

        } else {

            try {

                String fkey = TaleUtils.getFileKey(fname);
                String ftype = TaleUtils.isImage(upfile.getInputStream()) ? "image" : "file";

                File file = new File(TaleUtils.getUplodFilePath() + fkey);
                try {
                    FileCopyUtils.copy(upfile.getInputStream(), new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String domain = request.getScheme() + "://" + request.getServerName();
                if (request.getLocalPort() != 80) {
                    domain += ":" + request.getLocalPort();
                }

                obj.addProperty("state", "SUCCESS");
                obj.addProperty("url", domain + fkey);
                obj.addProperty("title", "");
                obj.addProperty("original", fname);
                obj.addProperty("fkey", fkey);

            } catch (Exception e) {
                obj.addProperty("state", e.getMessage());
            }
        }

        return obj.toString();
    }

}
