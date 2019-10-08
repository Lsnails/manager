package com.base.modules.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.base.utils.HttpUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName WxController
 * @Author zc
 * @Date 2019/10/8 15:38
 * @Version 1.0
 **/
@RestController
@RequestMapping("/wx")
public class WxController {

    @Value("XF.wx_token_url")
    private String wx_token_url;
    @Value("XF.wx_appid")
    private String wx_appid;
    @Value("XF.wx_redirect_url")
    private String wx_redirect_url;
    @Value("XF.wx_openid_url")
    private String wx_openid_url;
    @Value("XF.wx_secret")
    private String wx_secret;
    @Value("XF.wx_userinfo_url")
    private String wx_userinfo_url;

    /**
     * 配置微信文件
     */
    @GetMapping(value = "/MP_verify_5PHIL3aROj08OBwf.txt")
    @ResponseBody
    public String config() {
        return "5PHIL3aROj08OBwf";
    }

    @GetMapping(value = "/wxLogin")
    public String get(String scope) {
        if(StringUtils.isBlank(scope)){
            scope = "snsapi_userinfo"; //完全授权
        }else{
            scope = "snsapi_base"; // 静默授权
        }
        String url = wx_token_url + "appid=" + wx_appid + "&redirect_uri=" + wx_redirect_url + "&response_type=code&scope=" + scope + "&state=123#wechat_redirect";
        return "redirect:" + url;
    }

    /**
     * 获取Code
     * @param code Code
     * @return
     */
    @RequestMapping(value = "/getcode")
    @ResponseBody
    public String getCode(String code) {
        // 根据Code获取Openid
        String openidUrl = wx_openid_url + "appid=" + wx_appid + "&secret=" + wx_secret + "&code=" + code + "&grant_type=authorization_code";
        String openidMsg = HttpUtils.doPost(openidUrl, "", "UTF-8");
        // 解析返回信息
        JSONObject result = JSON.parseObject(openidUrl);
        // 网页授权接口调用凭证
        String access_token = result.getString("access_token");
        // 用户刷新access_token
        String refresh_token = result.getString("refresh_token");
        // 用户唯一标识
        String openid = result.getString("openid");

        System.err.println("code:" + code);
        System.err.println("网页授权接口调用凭证:" + access_token);
        System.err.println("用户刷新access_token:" + refresh_token);
        System.err.println("用户唯一标识:" + openid);

        // 拉取用户信息
        String userInfoUrl = wx_userinfo_url + "access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
        String userInfoMsg = HttpUtils.doPost(userInfoUrl, "", "UTF-8");
        // 解析返回信息
        JSONObject userInfo = JSON.parseObject(userInfoMsg);

        System.err.println("用户openid:" + userInfo.getString("openid"));
        System.err.println("名字:" + userInfo.getString("nickname"));
        // 值为1时是男性，值为2时是女性，值为0时是未知
        System.err.println("性别:" + userInfo.getString("sex"));
        System.err.println("省份:" + userInfo.getString("province"));
        System.err.println("城市:" + userInfo.getString("city"));
        System.err.println("国家:" + userInfo.getString("country"));
        System.err.println("头像:" + userInfo.getString("headimgurl"));
        System.err.println("特权:" + userInfo.getString("privilege"));
        System.err.println("unionid:" + userInfo.getString("unionid"));
        return code;
    }

}
