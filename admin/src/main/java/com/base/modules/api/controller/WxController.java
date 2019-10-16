package com.base.modules.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.base.modules.business.system.activityinfo.entity.ActivityinfoEntity;
import com.base.modules.business.system.activityinfo.service.ActivityinfoService;
import com.base.modules.business.system.wxuser.entity.WxUserEntity;
import com.base.modules.business.system.wxuser.service.WxUserService;
import com.base.utils.HttpUtils;
import com.base.utils.UUIDUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @ClassName WxController
 * @Author zc
 * @Date 2019/10/8 15:38
 * @Version 1.0
 **/
@Controller
@RequestMapping("/wx")
public class WxController {

    public static final String wx_token_url = "https://open.weixin.qq.com/connect/oauth2/authorize?";
    public static final String wx_appid = "wx01fc3b985a70091e";
    public static final String wx_redirect_url  = "http://wx.ffhigh.com/admin/wx/getcode";
    public static final String wx_openid_url = "https://api.weixin.qq.com/sns/oauth2/access_token?";
    public static final String wx_secret = "ab086069a568f7311f6fc9a35f7bc970";
    public static final String wx_userinfo_url = "https://api.weixin.qq.com/sns/userinfo?";

    @Autowired
    private ActivityinfoService activityinfoService;
    @Autowired
    private WxUserService wxUserService;

    /**
     * 配置微信文件
     */
    @GetMapping(value = "/MP_verify_5PHIL3aROj08OBwf.txt")
    @ResponseBody
    public String config() {
        return "5PHIL3aROj08OBwf";
    }

    @GetMapping(value = "/wxLogin")
    public String get(String scope, HttpServletResponse response) throws IOException {
        if(StringUtils.isBlank(scope)){
            scope = "snsapi_base"; //静默授权
        }else{
            scope = "snsapi_userinfo"; // 完全授权
        }
        String url = wx_token_url + "appid=" + wx_appid + "&redirect_uri=" + wx_redirect_url + "&response_type=code&scope=" + scope + "&state=123#wechat_redirect";
//        response.sendRedirect(url);
        return "redirect:" + url;
    }

    /**
     * 获取Code
     * @param code Code
     * @return
     */
    @GetMapping(value = "/getcode")
    public String getCode(String code) {
        // 根据Code获取Openid
        String openidUrl = wx_openid_url + "appid=" + wx_appid + "&secret=" + wx_secret + "&code=" + code + "&grant_type=authorization_code";
        String openidMsg = HttpUtils.doPost(openidUrl, "", "UTF-8");
        System.out.println("返回结果: -->     "+openidMsg);
        // 解析返回信息
        JSONObject result = JSON.parseObject(openidMsg);
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
        EntityWrapper<ActivityinfoEntity> entityWrapper = new EntityWrapper();
        entityWrapper.eq("status",1);
        ActivityinfoEntity activityinfoEntity = activityinfoService.selectOne(entityWrapper);
        if(null !=activityinfoEntity){
            setWxUser(userInfo.getString("openid"),activityinfoEntity.getActivityinfoId(),activityinfoEntity.getName());
        }
        return "redirect:/wx/close.html";
    }

    private void setWxUser(String openId,String activityId,String activityName){
        EntityWrapper<WxUserEntity> entityWrapper = new EntityWrapper();
        entityWrapper.eq("openId",openId);
        entityWrapper.eq("activity_id",activityId);
        WxUserEntity wxUserEntity = wxUserService.selectOne(entityWrapper);
        //只有当用户不存在的时候,才存入用户数据
        if(wxUserEntity==null){
            wxUserEntity = new WxUserEntity();
            wxUserEntity.setOpenId(openId);
            wxUserEntity.setActivityId(activityId);
            wxUserEntity.setActivityName(activityName);
            wxUserEntity.setCreateDate(new Date());
            wxUserEntity.setUserCode(UUIDUtils.getId().toUpperCase());
            wxUserEntity.setNetworkId("");
            wxUserEntity.setNetworkName("");
            wxUserEntity.setId(UUIDUtils.getRandomUUID());
            wxUserEntity.setState(0);
        }
    }

}
