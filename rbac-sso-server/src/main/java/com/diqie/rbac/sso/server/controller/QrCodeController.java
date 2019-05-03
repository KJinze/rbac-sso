package com.diqie.rbac.sso.server.controller;

import com.diqie.framework.web.util.IpUtil;
import com.diqie.rbac.sso.server.util.QRCodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/qr")
public class QrCodeController {
    @RequestMapping("/")
    public void getQRCode(String pageId, HttpServletRequest request, HttpServletResponse response) throws Exception{
        //String applicatonURL = IpUtil.getApplicatonURL(request);
        String applicatonURL = "http://192.168.1.100:8081/sso-server";
        String content = applicatonURL + "/qrcode/check?pageId="+pageId;
        BufferedImage image = QRCodeUtil.createImage(content,null,false);
        ImageIO.write(image, QRCodeUtil.FORMAT_NAME, response.getOutputStream());
    }

    @RequestMapping("/getQrCodeUrl")
    @ResponseBody
    public Map getQrCodeUrl(HttpServletRequest request) throws Exception {
        String uuid = UUID.randomUUID().toString();
        //String applicatonURL = IpUtil.getApplicatonURL(request);
        String applicatonURL = "http://192.168.1.100:8081/sso-server";
        String url = applicatonURL + "/qr/?pageId="+uuid;
        Map<String,String> map = new HashMap<String,String>();
        map.put("pageId",uuid);
        map.put("url",url);
        return map;
    }
}
