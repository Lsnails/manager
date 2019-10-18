package com.base.modules.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.base.common.utils.DateUtils;
import com.base.common.utils.R;
import com.base.modules.api.entity.AcItem;
import com.base.modules.api.entity.WxEntityVo;
import com.base.modules.business.system.activityinfo.entity.ActivityinfoEntity;
import com.base.modules.business.system.activityinfo.service.ActivityinfoService;
import com.base.modules.business.system.wxuser.entity.WxUserEntity;
import com.base.modules.business.system.wxuser.service.WxUserService;
import com.base.modules.customizesys.dictionary.entity.DictionaryEntity;
import com.base.modules.customizesys.dictionary.service.DictionaryService;
import com.base.modules.sys.entity.SysDeptEntity;
import com.base.modules.sys.service.SysDeptService;
import com.base.utils.CommonRpc;
import com.base.utils.HttpUtils;
import com.base.utils.UUIDUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public static final String wx_redirect_url = "http://wx.ffhigh.com/admin/wx/getcode";
    public static final String wx_openid_url = "https://api.weixin.qq.com/sns/oauth2/access_token?";
    public static final String wx_secret = "ab086069a568f7311f6fc9a35f7bc970";
    public static final String wx_userinfo_url = "https://api.weixin.qq.com/sns/userinfo?";

    @Autowired
    private ActivityinfoService activityinfoService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private DictionaryService dictionaryService;

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
        if (StringUtils.isBlank(scope)) {
            scope = "snsapi_base"; //静默授权
        } else {
            scope = "snsapi_userinfo"; // 完全授权
        }
        String url = wx_token_url + "appid=" + wx_appid + "&redirect_uri=" + wx_redirect_url + "&response_type=code&scope=" + scope + "&state=123#wechat_redirect";
//        response.sendRedirect(url);
        return "redirect:" + url;
    }

    /**
     * 获取Code
     *
     * @param code Code
     * @return
     */
    @GetMapping(value = "/getcode")
    public String getCode(String code, RedirectAttributes redirectAttributes) {
        // 根据Code获取Openid
        String openidUrl = wx_openid_url + "appid=" + wx_appid + "&secret=" + wx_secret + "&code=" + code + "&grant_type=authorization_code";
        String openidMsg = HttpUtils.doPost(openidUrl, "", "UTF-8");
        System.out.println("返回结果: -->     " + openidMsg);
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

        /*// 拉取用户信息
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
        System.err.println("unionid:" + userInfo.getString("unionid"));*/
        redirectInfo(redirectAttributes, openid);
        return "redirect:/wx/index.html";
    }

    private void redirectInfo(RedirectAttributes redirectAttributes, String openid) {
        EntityWrapper<ActivityinfoEntity> entityWrapper = new EntityWrapper();
        entityWrapper.eq("status", 1);
        ActivityinfoEntity activityinfoEntity = activityinfoService.selectOne(entityWrapper);
        if (null != activityinfoEntity) {
            setWxUser(openid, activityinfoEntity.getActivityinfoId(), activityinfoEntity.getName());
        }
        WxEntityVo wxEntityVo = new WxEntityVo();
        WxUserEntity wxUser = wxUserService.getUserInfo(openid, activityinfoEntity.getActivityinfoId());
        wxEntityVo.setWxUserEntity(wxUser);
        wxEntityVo.setQrUrl("http://wx.ffhigh.com" + sysDeptService.getWdInfo(wxUser.getNetworkId()).getQrcodeurl());
        redirectAttributes.addFlashAttribute("wxEntity", wxEntityVo);
        DictionaryEntity info = dictionaryService.getInfoByCode("user_type"); // 获取当前是哪种获取用户方式
        if(null == info){
            redirectAttributes.addFlashAttribute("type","1");//默认1方法 通过微信获取
        }else{
            redirectAttributes.addFlashAttribute("type",info.getDescription());
        }
    }

    /**
     * 跳转页面
     * @return
     */
    @GetMapping("/phone")
    public String redirectPhone(String openId,RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("openId",openId);
        return "redirect:/wx/phone.html";
    }

    @GetMapping("/index")
    public String redirectIndex(String openId,RedirectAttributes redirectAttributes){
        redirectInfo(redirectAttributes,openId);
        return "redirect:/wx/index.html";
    }

    private void setWxUser(String openId, String activityId, String activityName) {
        EntityWrapper<WxUserEntity> entityWrapper = new EntityWrapper();
        entityWrapper.eq("open_id", openId);
        entityWrapper.eq("activity_id", activityId);
        WxUserEntity wxUserEntity = wxUserService.selectOne(entityWrapper);
        SysDeptEntity qrCode = getQrCode();
        //只有当用户不存在的时候,才存入用户数据
        if (wxUserEntity == null) {
            wxUserEntity = new WxUserEntity();
            wxUserEntity.setOpenId(openId);
            wxUserEntity.setActivityId(activityId);
            wxUserEntity.setActivityName(activityName);
            wxUserEntity.setCreateDate(new Date());
            wxUserEntity.setUserCode(UUIDUtils.getId().toUpperCase());
            wxUserEntity.setNetworkId(qrCode.getDeptId().toString());
            wxUserEntity.setNetworkName(qrCode.getName());
            wxUserEntity.setId(UUIDUtils.getRandomUUID());
            wxUserEntity.setState(0);
            wxUserService.insert(wxUserEntity);
        }
    }

    private SysDeptEntity getQrCode() {
        long now = DateUtils.dateToLong(new Date(), "HH:mm:ss");//取当前的时分秒
        List<SysDeptEntity> list = sysDeptService.getList();
        SysDeptEntity rBean = null;
        for (SysDeptEntity entity : list) {
            String showTime = entity.getShowTime();
            if (StringUtils.isNotBlank(showTime)) {
                String[] split = showTime.split(" - ");
                long start = DateUtils.strToLong(split[0], "HH:mm:ss");
                long end = DateUtils.strToLong(split[1], "HH:mm:ss");
                if (now >= start && now <= end) {
                    rBean = entity;
                    break;
                }
            }
        }
        return rBean;
    }

    @GetMapping(value = "/test")
    public String test(String code, RedirectAttributes attr) {
        // 根据Code获取Openid
        attr.addAttribute("att1", code);
        attr.addAttribute("att2", code + "5555");
        attr.addFlashAttribute("msg", "添加出错!错误码为：");
        return "redirect:/wx/test.html";
    }

    @PostMapping(value = "/getUser")
    @ResponseBody
    public R getUser(String txt,String activityId) {
        WxUserEntity user = wxUserService.getUserByParam(txt,activityId);
        R r = new R();
        r.put("isData", user == null ? false : true);
        r.put("user", user);
        return r;
    }

    @RequestMapping(value = "/getHdData")
    @ResponseBody
    public R getHdData(){
        R r =new R();
        List<AcItem> acList = new ArrayList<AcItem>();
        List<ActivityinfoEntity> listByStates = activityinfoService.getListByStates();
        for (ActivityinfoEntity entity : listByStates) {
            AcItem acItem = new AcItem();
            acItem.setActivityId(entity.getActivityinfoId());
            acItem.setActivityName(entity.getName());
            acList.add(acItem);
        }
        r.put("acSize",acList.size());
        r.put("acList",acList);
        return r;
    }

    @RequestMapping(value = "/hxUser")
    @ResponseBody
    public R hxUser(String openId,String activityId){
        R r =new R();
        WxUserEntity userInfo = wxUserService.getUserInfo(openId, activityId);
        userInfo.setState(1);
        boolean b = wxUserService.updateById(userInfo);
        r.put("success",b);
        return r;
    }

    @RequestMapping(value = "/getBowser")
    @ResponseBody
    public R getBowser(HttpServletRequest request){
        R r =new R();
        boolean mobileDevice = JudgeIsMoblie(request);
        r.put("isMobile",mobileDevice);
        return r;
    }

    @RequestMapping(value = "/sendSms")
    @ResponseBody
    public R sendSms(String phone,HttpServletRequest request){
        R r =new R();
        boolean isExist = false;
        boolean isSend = false;
        String sessionYzm = (String)request.getSession().getAttribute(phone);
        if(StringUtils.isNotBlank(sessionYzm)){
            isExist = true;
        }else{
            String yzm = RandomStringUtils.randomNumeric(6);
            boolean b = CommonRpc.getInstance().sendSms("1", phone, yzm);
            if(b){
                request.getSession().setAttribute("15300097795",yzm); //存储验证码
                isSend = true;
            }
        }
        r.put("isSend", isExist);
        r.put("isExist",isSend);
        return r;
    }

    @RequestMapping(value = "/commit")
    @ResponseBody
    public R commit(String phone,String yzm,HttpServletRequest request,String openId){
        R r =new R();
        boolean pass = false;
        String sessionYzm = (String)request.getSession().getAttribute(phone);
        if(sessionYzm.equals(yzm)){
            pass = true;
            request.getSession().removeAttribute(phone); //移除session
        }
        r.put("isPass",pass);
        return r;
    }

    public boolean JudgeIsMoblie(HttpServletRequest request) {
        boolean isMoblie = false;
        String[] mobileAgents = { "iphone", "android", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
                "opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
                "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
                "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
                "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
                "wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
                "pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
                "240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
                "blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
                "kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
                "mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
                "prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
                "smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
                "voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
                "Googlebot-Mobile" };
        if (request.getHeader("User-Agent") != null) {
            for (String mobileAgent : mobileAgents) {
                if (request.getHeader("User-Agent").toLowerCase().indexOf(mobileAgent) >= 0) {
                    isMoblie = true;
                    break;
                }
            }
        }
        return isMoblie;
    }

}
