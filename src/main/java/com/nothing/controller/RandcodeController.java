package com.nothing.controller;

import com.nothing.TDHttpClient;
import com.nothing.service.RandcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    void checkRandCode(@RequestParam String positionStr) {
        randcodeService.checkRandcode(positionStr);
    }
}
