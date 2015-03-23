package com.nothing.controller;

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
    /**
     * 获取验证码
     */
    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    void getRandCode() {

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
        return false;
    }
}
