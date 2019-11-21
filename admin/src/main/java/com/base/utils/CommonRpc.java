package com.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
/*
pom.xml
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>4.0.3</version>
</dependency>
*/
public class CommonRpc {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	private static CommonRpc commonRpc = new CommonRpc();
    //将构造器设置为private禁止通过new进行实例化
    private CommonRpc() {

    }
	
	public static CommonRpc getInstance() {
        return commonRpc;
    }
	/**
	 * 发送短信，调用阿里短信服务
	 * @Title: sendSms   
	 * @Description:发送短信，调用阿里短信服务
	 * @author: huanw
	 * @param: @param mobile
	 * @param: @param SignName
	 * @param: @param TemplateCode
	 * @param: @param accessKeyId
	 * @param: @param secret      
	 * @return: void
	 * @date:   2019年3月25日 下午4:40:51       
	 * @throws
	 */
	public  boolean sendSms(String mobile,String SignName,String TemplateCode,String TemplateParam,String accessKeyId,String secret) {
		logger.info("准备给"+mobile+"手机号发送验证码："+TemplateParam);
		boolean flag=false;
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",accessKeyId,secret);
		IAcsClient client = new DefaultAcsClient(profile);
		// SendSms
		CommonRequest request = new CommonRequest();
		// request.setProtocol(ProtocolType.HTTPS);
		request.putQueryParameter("PhoneNumbers",mobile); // 接收短信的手机号码。
		request.putQueryParameter("SignName",SignName);// 短信签名名称。请在控制台签名管理页面签
		request.putQueryParameter("TemplateCode",TemplateCode);// 短信模板ID。请在控制台模板管理页面模板CODE一列查看。
		request.setAction("SendSms"); // 系统规定参数。取值：SendSms。
		request.setVersion("2017-05-25");
		// 否 OutId 外部流水扩展字段。
		request.putQueryParameter("RegionId", "cn-hangzhou");
		request.setMethod(MethodType.POST);
		request.setDomain("dysmsapi.aliyuncs.com");
		// request.putQueryParameter("AccessKeyId", "LTAIZyAgeboCPmbY");// 否
		// 主账号AccessKey的ID。 eg：LTAIP00vvvvvvvvv
		request.putQueryParameter("TemplateParam",TemplateParam);// 否 短信模板变量对应的实际值，JSON格式。 {"code":"1111"}
		try {
			CommonResponse response = client.getCommonResponse(request);
			JSONObject object = (JSONObject) JSONObject.parse(response.getData());
			String code = (String)object.get("Code");
			if("OK".equals(code)) {
				logger.info(mobile+"手机号发送验证码成功,返回json为"+response.getData());
				flag = true;
			}else {
				logger.error(mobile+"手机号发送验证码失败,返回json为"+response.getData());
			}
		} catch (Exception e) {
			logger.error(mobile+"手机号发送验证码错误：",e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 发送短信，调用阿里短信服务
	 * @Title: sendSms   
	 * @Description: 发送短信，调用阿里短信服务
	 * @author: huanw
	 * @param: @param mobileType  手机号类型：  1-内地  2-香港       
	 * @param: @param mobile    手机号
	 * @param: @param param     发送的动态内容，例如验证码
	 * @param: @return      
	 * @return: boolean
	 * @date:   2019年4月15日 下午1:03:40       
	 * @throws
	 */
	public  boolean sendSms(String mobileType,String mobile,String param) {
		logger.info("准备给"+mobile+"手机号发送验证码："+param);
		boolean flag=false;
		IAcsClient client = null;
		String regionId="";//地区id
		String SignName="";//短信签名名称。请在控制台签名管理页面签
		String TemplateCode="";// 短信模板ID。请在控制台模板管理页面模板CODE一列查看。
		String accessKeyId="";//主账号AccessKey的ID。 eg：LTAIP00vvvvvvvvv
		String secret="";
		String TemplateParam="";
		// SendSms
		CommonRequest request = new CommonRequest();
		accessKeyId = ConstantSms.accessKeyId;
		secret = ConstantSms.AccessKeySecret;
		//手机号类型：  1-内地  2-香港       待写
		switch (mobileType) {
			case  "1":
				//1-内地
				regionId= ConstantSms.regionId_HangZhou_PRC;
				SignName = ConstantSms.SignName;
				TemplateCode = ConstantSms.TemplateCode_PRC;
				//验证码
				TemplateParam ="{\"code\":\""+param+"\"}";
				client = this.getClient(regionId, accessKeyId, secret);
				break;
			case "2":
				// 2-香港 
				regionId= ConstantSms.regionId_HangZhou_HK;
				SignName = ConstantSms.SignName;
				TemplateCode = ConstantSms.TemplateCode_HK;
				//验证码
				TemplateParam ="{\"code\":\""+param+"\"}";
				client = this.getClient(regionId, accessKeyId, secret);
				break;
			default:
				break;
		}
		this.setCommonRequest(regionId,request, mobile, SignName, TemplateCode, TemplateParam);
		if(client==null) {
			logger.error(mobile+"手机号发送验证码失败,client为空");
			return flag;
		}
		try {
			CommonResponse response = client.getCommonResponse(request);
			JSONObject object = (JSONObject) JSONObject.parse(response.getData());
			String code = (String)object.get("Code");
			if("OK".equals(code)) {
				logger.info(mobile+"手机号发送验证码成功,返回json为"+response.getData());
				flag = true;
			}else {
				logger.error(mobile+"手机号发送验证码失败,发送内容为"+param+"---返回json为"+response.getData());
			}
		} catch (Exception e) {
			logger.error(mobile+"手机号发送验证码错误：",e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 获取IAcsClient
	 * @Title: getClient   
	 * @Description: TODO(这里用一句话描述这个方法的作用)   
	 * @author: huanw
	 * @param: @param regionId
	 * @param: @param accessKeyId
	 * @param: @param secret
	 * @param: @return      
	 * @return: IAcsClient
	 * @date:   2019年4月15日 上午11:37:59       
	 * @throws
	 */
	public IAcsClient getClient(String regionId, String accessKeyId, String secret) {
		DefaultProfile profile = DefaultProfile.getProfile(regionId,accessKeyId,secret);
		IAcsClient client = new DefaultAcsClient(profile);
		return client;
	}
	/**
	 * 设置发短信参数
	 * @Title: setCommonRequest   
	 * @Description: 设置发短信参数
	 * @author: huanw
	 * @param: @param request
	 * @param: @param mobile
	 * @param: @param SignName
	 * @param: @param TemplateCode
	 * @param: @param TemplateParam      
	 * @return: void
	 * @date:   2019年4月15日 上午11:38:16       
	 * @throws
	 */
	public void setCommonRequest(String regionId,CommonRequest request,String mobile,String SignName,String TemplateCode,String TemplateParam) {
		// request.setProtocol(ProtocolType.HTTPS);
		request.putQueryParameter("PhoneNumbers",mobile); // 接收短信的手机号码。
		request.putQueryParameter("SignName",SignName);// 短信签名名称。请在控制台签名管理页面签
		request.putQueryParameter("TemplateCode",TemplateCode);// 短信模板ID。请在控制台模板管理页面模板CODE一列查看。
		request.setAction("SendSms"); // 系统规定参数。取值：SendSms。
		request.setVersion("2017-05-25");
		// 否 OutId 外部流水扩展字段。
//		request.putQueryParameter("RegionId", "cn-hangzhou");
		request.putQueryParameter("RegionId", regionId);
		request.setMethod(MethodType.POST);
		request.setDomain("dysmsapi.aliyuncs.com");
		// request.putQueryParameter("AccessKeyId", "LTAIZyAgeboCPmbY");// 否
		// 主账号AccessKey的ID。 eg：LTAIP00vvvvvvvvv
		request.putQueryParameter("TemplateParam",TemplateParam);// 否 短信模板变量对应的实际值，JSON格式。 {"code":"1111"}
	}
    public static void main(String[] args) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "123456", "123456");
        IAcsClient client = new DefaultAcsClient(profile);
        //SendSms
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.putQueryParameter("PhoneNumbers", "15321772160"); //接收短信的手机号码。
        request.putQueryParameter("SignName", "hkaom网站");//短信签名名称。请在控制台签名管理页面签
        request.putQueryParameter("TemplateCode", "SMS_164505745");//短信模板ID。请在控制台模板管理页面模板CODE一列查看。
        request.setAction("SendSms"); //系统规定参数。取值：SendSms。
        request.setVersion("2017-05-25");
        //否 OutId 外部流水扩展字段。
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
//        request.putQueryParameter("AccessKeyId", "LTAIZyAgeboCPmbY");// 否 主账号AccessKey的ID。 eg：LTAIP00vvvvvvvvv
        request.putQueryParameter("TemplateParam", "{\"code\":\"55555\"}");//否 短信模板变量对应的实际值，JSON格式。 	{"code":"1111"}
       
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}