package com.base.modules.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
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
import com.base.modules.sys.shiro.ShiroUtils;
import com.base.utils.HttpUtils;
import com.base.utils.UUIDUtils;
import com.google.code.kaptcha.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
import java.net.URLEncoder;
import java.text.MessageFormat;
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
@Slf4j
public class WxController {
    //    private static final String URL = "http://wx.ffhigh.com/";
    private static final String URL = "http://wx.ticket.42du.net/";


    public static final String wx_token_url = "https://open.weixin.qq.com/connect/oauth2/authorize?";
    //    public static final String wx_appid = "wx01fc3b985a70091e";
    public static final String wx_appid = "wx759b4ad634e6acdc";
    public static final String wx_redirect_url = URL + "admin/wx/getcode?number={0}";
    public static final String wx_openid_url = "https://api.weixin.qq.com/sns/oauth2/access_token?";
    //    public static final String wx_secret = "ab086069a568f7311f6fc9a35f7bc970";
    public static final String wx_secret = "bf593b12274c8f76d631e53c9649f7f7";
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

    @GetMapping(value = "/MP_verify_cWjF0KqOz7Y4hKc2.txt")
    @ResponseBody
    public String txConfig() {
        return "cWjF0KqOz7Y4hKc2";
    }

    @GetMapping(value = "/MP_verify_0dMvLgMEHKVcs2y7.txt")
    @ResponseBody
    public String txConfig1() {
        return "0dMvLgMEHKVcs2y7";
    }

    @GetMapping(value = "/wxLogin")
    public String get(String scope, String number, HttpServletResponse response) throws IOException {
        String enUrl = MessageFormat.format(wx_redirect_url, number);
        log.error("enUrl - >{}", enUrl);
        log.error("编码后的Url -> {}", URLEncoder.encode(enUrl, "GBK"));
        if (StringUtils.isBlank(scope)) {
            scope = "snsapi_base"; //静默授权
        } else {
            scope = "snsapi_userinfo"; // 完全授权
        }
        String url = wx_token_url + "appid=" + wx_appid + "&redirect_uri=" + enUrl + "&response_type=code&scope=" + scope + "&state=123#wechat_redirect";
//        response.sendRedirect(url);
        return "redirect:" + url;
    }

    @GetMapping(value = "/wxLogin1")
    public String get1(String scope, HttpServletResponse response) throws IOException {
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
    public String getCode(String code, String number, RedirectAttributes redirectAttributes) {
        // 根据Code获取Openid
        log.error("getCode - > 参数 : {}", number);
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
        redirectAttributes.addFlashAttribute("openId", openid);//  openId 页面上获取
        redirectInfo(redirectAttributes, openid ,number);
        String x = (String) redirectAttributes.getFlashAttributes().get("isNew"); // 是否为新用户  0 新 1老
        String o = (String) redirectAttributes.getFlashAttributes().get("over"); // 0 没有一个进行中的活动
        String type = (String) redirectAttributes.getFlashAttributes().get("type"); // 1 直接获取 2 通过手机号 3 自定义获取
        WxEntityVo vo = (WxEntityVo) redirectAttributes.getFlashAttributes().get("wxEntity");

        if (("1").equals(o)) {
            //活动结束跳转页面
            return "redirect:/wx/index4.html";
        }

        if (("1").equals(type)) { //直接跳转 首页
            return returnUrl(x);
        } else if (("2").equals(type)) {
            if (StringUtils.isNotBlank(vo.getWxUserEntity().getPhone())) {
                return returnUrl(x);
            } else {
                return "redirect:/wx/phone.html";
            }
        } else if (("3").equals(type)) {
            if (StringUtils.isNotBlank(vo.getWxUserEntity().getPhone())) {
                return returnUrl(x);
            } else {
                return "redirect:/wx/customer.html";
            }
        }
        return "redirect:/wx/index4.html";
    }

    private String returnUrl(String x) {
        if (("0").equals(x)) {
            return "redirect:/wx/index3.html";
        } else {
            return "redirect:/wx/index2.html";
        }
    }

    /**
     * 跳转
     *
     * @param redirectAttributes
     * @param openid
     */
    private void redirectInfo(RedirectAttributes redirectAttributes, String openid , String number) {
        WxUserEntity wxUser = getWxUserEntity(openid, redirectAttributes , number);
        if (wxUser != null) {
            WxEntityVo wxEntityVo = new WxEntityVo();
            wxEntityVo.setWxUserEntity(wxUser);
            if (sysDeptService.getWdInfo(wxUser.getNetworkId()) != null) {
                wxEntityVo.setQrUrl(URL + sysDeptService.getWdInfo(wxUser.getNetworkId()).getQrcodeurl());
            }
            redirectAttributes.addFlashAttribute("wxEntity", wxEntityVo);
            redirectAttributes.addFlashAttribute("phone", StringUtils.isNotBlank(wxEntityVo.getWxUserEntity().getPhone()) == true ? wxEntityVo.getWxUserEntity().getPhone() : "");
            DictionaryEntity info = dictionaryService.getInfoByCode("user_type"); // 获取当前是哪种获取用户方式
            if (null == info) {
                redirectAttributes.addFlashAttribute("type", "1");//默认1方法 通过微信获取
            } else {
                redirectAttributes.addFlashAttribute("type", info.getDescription());
            }
            redirectAttributes.addFlashAttribute("over", "0"); //新用户
        } else {
            redirectAttributes.addFlashAttribute("over", "1"); //老用户
        }
    }

    /**
     * 生成wx用户
     *
     * @param openid
     * @return
     */
    private WxUserEntity getWxUserEntity(String openid, RedirectAttributes redirectAttributes,String number) {
        EntityWrapper<ActivityinfoEntity> entityWrapper = new EntityWrapper();
        entityWrapper.eq("status", 1);
        entityWrapper.eq("create_by", number);
        ActivityinfoEntity activityinfoEntity = activityinfoService.selectOne(entityWrapper);
        if (null != activityinfoEntity) {
            WxUserEntity entity = wxUserService.getUserInfo(openid, activityinfoEntity.getActivityinfoId());
            if (null == entity) {
                setWxUser(openid, activityinfoEntity.getActivityinfoId(), activityinfoEntity.getName() , number);
                redirectAttributes.addFlashAttribute("isNew", "0");
            } else {
                redirectAttributes.addFlashAttribute("isNew", "1");
            }
            return wxUserService.getUserInfo(openid, activityinfoEntity.getActivityinfoId());
        } else {
            return null;
        }

    }


    /**
     * openId 获取用户信息
     *
     * @param openid
     * @return
     */
    private WxUserEntity getWxUserInfo(String openid) {
        EntityWrapper<ActivityinfoEntity> entityWrapper = new EntityWrapper();
        entityWrapper.eq("status", 1);
        ActivityinfoEntity activityinfoEntity = activityinfoService.selectOne(entityWrapper);
        return wxUserService.getUserInfo(openid, activityinfoEntity.getActivityinfoId());
    }

    /*    *//**
     * 根据手机号获取用户信息
     * @param phone
     * @return
     *//*
    private WxUserEntity getWxUserInfoByPhone(String phone) {
    	EntityWrapper<ActivityinfoEntity> entityWrapper = new EntityWrapper();
        entityWrapper.eq("status", 1);
        ActivityinfoEntity activityinfoEntity = activityinfoService.selectOne(entityWrapper);
        return wxUserService.getUserByParam(phone, activityinfoEntity.getActivityinfoId());
    }*/

    /**
     * 跳转页面
     *
     * @return
     */
    @GetMapping("/phone")
    public String redirectPhone(String openId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("openId", openId);
        return "redirect:/wx/phone.html";
    }

    /**
     * 跳转页面
     *
     * @return
     */
    @GetMapping("/customer")
    public String customer(String openId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("openId", openId);
        return "redirect:/wx/customer.html";
    }

    @GetMapping("/index")
    public String redirectIndex(String openId, RedirectAttributes redirectAttributes) {
        redirectInfo(redirectAttributes, openId , "");
        return returnUrl("0");
    }

    private void setWxUser(String openId, String activityId, String activityName , String number) {
        EntityWrapper<WxUserEntity> entityWrapper = new EntityWrapper();
        entityWrapper.eq("open_id", openId);
        entityWrapper.eq("activity_id", activityId);
        WxUserEntity wxUserEntity = wxUserService.selectOne(entityWrapper);
        SysDeptEntity qrCode = sysDeptService.getQrCode(number);
        //只有当用户不存在的时候,才存入用户数据
        if (wxUserEntity == null) {
            wxUserEntity = new WxUserEntity();
            wxUserEntity.setOpenId(openId);
            wxUserEntity.setActivityId(activityId);
            wxUserEntity.setActivityName(activityName);
            wxUserEntity.setCreateDate(new Date());
            wxUserEntity.setUserCode(UUIDUtils.getRandom(8, wxUserService));
            wxUserEntity.setNetworkId(qrCode.getDeptId().toString());
            wxUserEntity.setNetworkName(qrCode.getName());
            wxUserEntity.setId(UUIDUtils.getRandomUUID());
            wxUserEntity.setState(0);
            wxUserEntity.setCreateBy(number);
            wxUserService.insert(wxUserEntity);
        }
    }

  /*  private SysDeptEntity getQrCode() {
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
    }*/

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
    public R getUser(String txt, String activityId) {
        WxUserEntity user = wxUserService.getUserByParam(txt, activityId, null);
        R r = new R();
        r.put("isData", user == null ? false : true);
        r.put("user", user);
        return r;
    }

    @RequestMapping(value = "/getHdData")
    @ResponseBody
    public R getHdData() {
        R r = new R();
        List<AcItem> acList = new ArrayList<AcItem>();
        List<ActivityinfoEntity> listByStates = activityinfoService.getListByStates();
        for (ActivityinfoEntity entity : listByStates) {
            AcItem acItem = new AcItem();
            acItem.setActivityId(entity.getActivityinfoId());
            acItem.setActivityName(entity.getName());
            acList.add(acItem);
        }
        r.put("acSize", acList.size());
        r.put("acList", acList);
        return r;
    }

    @RequestMapping(value = "/hxUser")
    @ResponseBody
    public R hxUser(String openId, String activityId, String wdCode) {
        R r = new R();
        WxUserEntity userInfo = wxUserService.getUserInfo(openId, activityId);
        userInfo.setState(1);
        userInfo.setWdCode(wdCode);
        boolean b = wxUserService.updateById(userInfo);
        r.put("success", b);
        return r;
    }

    @RequestMapping(value = "/getBowser")
    @ResponseBody
    public R getBowser(HttpServletRequest request) {
        R r = new R();
        boolean mobileDevice = JudgeIsMoblie(request);
        r.put("isMobile", mobileDevice);
        return r;
    }

   /* @RequestMapping(value = "/sendSms")
    @ResponseBody
    public R sendSms(String phone, HttpServletRequest request) {
        R r = new R();
        boolean isExist = false;
        boolean isSend = false;
        boolean isLimit = false;
        boolean phoneExist = false;

        WxUserEntity wxUserInfoByPhone = getWxUserInfoByPhone(phone);
        if(wxUserInfoByPhone == null) {
        	String sessionYzm = (String) request.getSession().getAttribute(phone);
            Integer yzmNumber = (Integer) request.getSession().getAttribute("yzmNumber");//验证码次数验证 ... 默认3次 超过3次不在发送
            if (null != yzmNumber && yzmNumber >= 3) {
                isLimit = true;
            } else {
                if (StringUtils.isNotBlank(sessionYzm)) {
                    isExist = true;
                } else {
                    String yzm = RandomStringUtils.randomNumeric(6);
                    boolean b = CommonRpc.getInstance().sendSms("1", phone, yzm);
                    if (b) {
                        request.getSession().setAttribute(phone, yzm); //存储验证码
                        request.getSession().setAttribute("yzmNumber", yzmNumber == null ? 1 : yzmNumber + 1);
                        isSend = true;
                    }
                }
            }
        }else {
        	phoneExist = true;
        }
        r.put("isLimit", isLimit);
        r.put("isSend", isSend);
        r.put("isExist", isExist);
        r.put("phoneExist", phoneExist);
        return r;
    }*/

    @RequestMapping(value = "/commit")
    @ResponseBody
    public R commit(String phone, String yzm, HttpServletRequest request, String openId) {
        R r = new R();
        boolean pass = false;
        boolean isExsit = false;
        if (StringUtils.isBlank(openId)) {
            isExsit = true;
        } else {
            String sessionYzm = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
            if (sessionYzm.equals(yzm)) {
                pass = true;
//                request.getSession().removeAttribute(phone); //移除session
                //更新用户数据,更新手机号
                WxUserEntity wxUserEntity = getWxUserInfo(openId);
                if (null == wxUserEntity) {
                    isExsit = true;
                } else {
                    wxUserEntity.setPhone(phone);
                    wxUserService.updateById(wxUserEntity);
                }
            }
        }
        r.put("isPass", pass);
        r.put("isExsit", isExsit);
        return r;
    }

    @RequestMapping(value = "/customerCommit")
    @ResponseBody
    public R customerCommit(String phone, String yzm, HttpServletRequest request, String openId) {
        R r = new R();
        boolean pass = false;
        boolean isExsit = false;
        if (StringUtils.isBlank(openId)) {
            isExsit = true;
        } else {
            String sessionYzm = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
            if (sessionYzm.equals(yzm)) {
                pass = true;
//                request.getSession().removeAttribute(phone); //移除session
                //更新用户数据,更新手机号
                WxUserEntity wxUserEntity = getWxUserInfo(openId);
                if (null == wxUserEntity) {
                    isExsit = true;
                } else {
                    wxUserEntity.setPhone(phone);
                    List<DictionaryEntity> list = dictionaryService.getInfoListLikeCode("input");
                    for (int i = 0; i < list.size(); i++) {
                        String value = request.getParameter(list.get(i).getCode());
                        setWxData(i, value, wxUserEntity);
                    }
                    wxUserService.updateById(wxUserEntity);
                }
            }
        }
        r.put("isPass", pass);
        r.put("isExsit", isExsit);
        return r;
    }

    private void setWxData(int i, String value, WxUserEntity wxUserEntity) {
        switch (i) {
            case 0:
                wxUserEntity.setRemark(value);
                break;
            case 1:
                wxUserEntity.setRemark2(value);
                break;
            case 2:
                wxUserEntity.setRemark3(value);
                break;
            case 3:
                wxUserEntity.setRemark4(value);
                break;
            case 4:
                wxUserEntity.setRemark5(value);
                break;
        }
    }

    public boolean JudgeIsMoblie(HttpServletRequest request) {
        boolean isMoblie = false;
        String[] mobileAgents = {"iphone", "android", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
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
                "smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tsm-", "upg1", "upsi", "vk-v",
                "voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
                "Googlebot-Mobile"};  //tosh
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


    /**
     * @return
     */
    @RequestMapping(value = "/getInputList")
    @ResponseBody
    public R getInputList() {
        R r = new R();
        List<DictionaryEntity> list = dictionaryService.getInfoListLikeCode("input");
        r.put("inputList", list);
        return r;
    }

}
