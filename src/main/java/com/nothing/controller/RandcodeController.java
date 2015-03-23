package com.nothing.controller;

import com.nothing.TDHttpClient;
import com.nothing.service.RandcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zyan.zhang on 2015/3/23.
 * 验证码
 */
@Controller
@RequestMapping("/randcode")
public class RandcodeController {
    @Autowired
    RandcodeService randcodeService;

    /**
     * 获取验证码
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public
    @ResponseBody
    byte[] getRandCode() {
        return randcodeService.getRandcodeImage();
    }

    /**
     * 校验验证码
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    boolean checkRandCode() {
        return randcodeService.checkRandcode();
    }
}
