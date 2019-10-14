package com.base.modules.customizesys.upload.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.BucketInfo;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;


/**
 * 处理阿里oss上传文件
 * @ClassName:  OssUtils   
 * @Description:处理阿里oss上传文件
 * @author:  huanw
 * @date:   2019年4月17日 下午1:07:15
 */
public class OssUtils {
	protected Logger logger = LoggerFactory.getLogger(getClass());

//    // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
//    // 如果您还没有创建Bucket，endpoint选择请参看文档中心的“开发人员指南 > 基本概念 > 访问域名”，
//    // 链接地址是：https://help.aliyun.com/document_detail/oss/user_guide/oss_concept/endpoint.html?spm=5176.docoss/user_guide/endpoint_region
//    // endpoint的格式形如“http://oss-cn-hangzhou.aliyuncs.com/”，注意http://后不带bucket名称，
//    // 比如“http://bucket-name.oss-cn-hangzhou.aliyuncs.com”，是错误的endpoint，请去掉其中的“bucket-name”。
//    private static String endpoint = "http://oss-cn-beijing.aliyuncs.com";
//
//    // accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
//    // 创建和查看访问密钥的链接地址是：https://ak-console.aliyun.com/#/。
//    // 注意：accessKeyId和accessKeySecret前后都没有空格，从控制台复制时请检查并去除多余的空格。
//    private static String accessKeyId = "LTAIlqJa5Ub8ncCq";
//    private static String accessKeySecret = "ctqlKE6c4E8q40SbEsak2ZLA1FduMa";
//
//    // Bucket用来管理所存储Object的存储空间，详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
//    // Bucket命名规范如下：只能包括小写字母，数字和短横线（-），必须以小写字母或者数字开头，长度必须在3-63字节之间。
//    private static String bucketName = "aimaodou";
//    
    private static OssUtils instance=null;

	public static OssUtils getInstance(){
		if(instance==null){
			instance=new OssUtils();
		}
		return instance;
	}
	/**
	 * 生成OSSClient
	 * @Title: getOssClient
	 * @Description: 生成OSSClient
	 * // 生成OSSClient，您可以指定一些参数，详见“SDK手册 > Java-SDK > 初始化”，
     *  // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/init.html?spm=5176.docoss/sdk/java-sdk/get-start
	 * @author: huanw
	 * @param: @param endpoint
	 * @param: @param accessKeyId
	 * @param: @param accessKeySecret
	 * @param: @return      
	 * @return: OSSClient
	 * @date:   2019年4月17日 下午1:18:06       
	 * @throws
	 */
	public OSSClient getOssClient(String endpoint,String accessKeyId,String accessKeySecret) {
		 OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		 return ossClient;
	}
	/**
	 * 创建存储空间
	 * @Title: createBucket   
	 * @Description: 创建存储空间
	 * @author: huanw
	 * @param: @param ossClient
	 * @param: @param bucketName      
	 * @return: void
	 * @date:   2019年4月17日 下午1:18:59       
	 * @throws
	 */
	public void createBucket(OSSClient ossClient,String bucketName) {
		if(ossClient==null) {return;}
		// 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
        // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
//        if (ossClient.doesBucketExist(bucketName)) {
//            System.out.println("您已经创建Bucket：" + bucketName + "。");
//        } else {
            // 创建Bucket。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
//            Bucket createBucket = ossClient.createBucket(bucketName);
		    //如下创建的是公共读的存储空间
            CreateBucketRequest createBucketRequest =new CreateBucketRequest(bucketName);
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            ossClient.createBucket(createBucketRequest);
//        }
	}
	/**
	 * 获取存储空间信息
	 * @Title: getBucketInfo   
	 * @Description: 获取存储空间信息
	 * @author: huanw
	 * @param: @param ossClient
	 * @param: @param bucketName
	 * @param: @return      
	 * @return: BucketInfo
	 * @date:   2019年4月17日 下午1:21:33       
	 * @throws
	 */
	public BucketInfo getBucketInfo(OSSClient ossClient,String bucketName) {
		if(ossClient==null || "".equals(bucketName)) {return null;}
		// 查看Bucket信息。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
        // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
        BucketInfo info = ossClient.getBucketInfo(bucketName);
        System.out.println("Bucket " + bucketName + "的信息如下：");
        System.out.println("\t数据中心：" + info.getBucket().getLocation());
        System.out.println("\t创建时间：" + info.getBucket().getCreationDate());
        System.out.println("\t用户标志：" + info.getBucket().getOwner());
        return info;
	}
	
	/**
	 * 上传文件到oss的某个object下
	 * @Title: putObjectFile   
	 * @Description: 上传文件到oss的某个object下  
	 * @author: huanw
	 * @param: @param ossClient
	 * @param: @param bucketName
	 * @param: @param localFilePath    原文件路径   例如：C:/Users/huanw/Desktop/0.jpg
	 * @param: @param ossFilePath      上传到oss的文件路径   例如：amd/33333.jpg
	 * @param: @param endpointNotWithHttp  endpoint去掉前面的http剩下的地址，适用于返回文件http访问路径，endpoint是访问OSS的域名。
	 * 									 例如endpoint=http://oss-cn-beijing.aliyuncs.com  那么endpointNotWithHttp=oss-cn-beijing.aliyuncs.com
	 * @return: String url  文件可访问的http连接
	 * @date:   2019年4月17日 下午1:24:50       
	 * @throws
	 */
	public String putObjectFile(OSSClient ossClient,String bucketName,String localFilePath,String ossFilePath,String endpointNotWithHttp) {
		 // 文件存储入OSS，Object的名称为fileKey。详细请参看“SDK手册 > Java-SDK > 上传文件”。
        // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/upload_object.html?spm=5176.docoss/user_guide/upload_object
        //上传参考代码：https://help.aliyun.com/document_detail/84781.html?spm=a2c4g.11186623.6.670.1c9659aa6hH0kT
        ossClient.putObject(bucketName,ossFilePath, new File(localFilePath));
        logger.info("Object：" + ossFilePath + "存入OSS成功。");
        String url=bucketName+"."+endpointNotWithHttp+"/"+ossFilePath;//例如：https://aimaodou.oss-cn-beijing.aliyuncs.com/amd/aaaaa.jpg
        return url;
	}
	/**
	 * 上传文件流
	 * @Title: putObject   
	 * @Description: 上传文件流
	 * @author: huanw
	 * @param: @param ossClient  
	 * @param: @param bucketName  
	 * @param: @param ossFilePath  上传到oss的路径
	 * @param: @param inputStream  待上传的文件流
	 * @param: @param endpointNotWithHttp endpoint去掉前面的http剩下的地址，适用于返回文件http访问路径，endpoint是访问OSS的域名。
	 * 									 例如endpoint=http://oss-cn-beijing.aliyuncs.com  那么endpointNotWithHttp=oss-cn-beijing.aliyuncs.com
	 * @param: @return      
	 * @return: String
	 * @date:   2019年4月17日 下午3:37:48       
	 * @throws
	 */
	public String putObjectInputStream(OSSClient ossClient,String bucketName,String ossFilePath,InputStream inputStream,String endpointNotWithHttp) {
		ossClient.putObject(bucketName, ossFilePath, inputStream);
		logger.info("Object：" + ossFilePath + "存入OSS成功。");
        String url=bucketName+"."+endpointNotWithHttp+"/"+ossFilePath;//例如：https://aimaodou.oss-cn-beijing.aliyuncs.com/amd/aaaaa.jpg
        return url;
	}
	public String putObjectByteArray(OSSClient ossClient,String bucketName,String ossFilePath,byte[] base64DecodeToByte,String endpointNotWithHttp) {
		ossClient.putObject(bucketName,ossFilePath, new ByteArrayInputStream(base64DecodeToByte));
		logger.info("Object：" + ossFilePath + "存入OSS成功。");
        String url=bucketName+"."+endpointNotWithHttp+"/"+ossFilePath;//例如：https://aimaodou.oss-cn-beijing.aliyuncs.com/amd/aaaaa.jpg
        return url;
	}
	/**
	 * 查看Bucket中的Object
	 * @Title: listObjects   
	 * @Description: 查看Bucket中的Object
	 * @author: huanw
	 * @param: @param ossClient
	 * @param: @param bucketName
	 * @param: @return      
	 * @return: List<OSSObjectSummary>
	 * @date:   2019年4月17日 下午1:35:19       
	 * @throws
	 */
	public List<OSSObjectSummary> listObjects(OSSClient ossClient,String bucketName) {
		// 查看Bucket中的Object。详细请参看“SDK手册 > Java-SDK > 管理文件”。
	    // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_object.html?spm=5176.docoss/sdk/java-sdk/manage_bucket
	    ObjectListing objectListing = ossClient.listObjects(bucketName);
	    List<OSSObjectSummary> objectSummary = objectListing.getObjectSummaries();
//	    System.out.println("您有以下Object：");
//	    for (OSSObjectSummary object : objectSummary) {
//	        System.out.println("\t" + object.getKey());
//	    }
	    return objectSummary;
	}
	
	/**
	 * 删除Object
	 * @Title: deleteObject   
	 * @Description: 删除Object
	 * @author: huanw
	 * @param: @param ossClient
	 * @param: @param bucketName
	 * @param: @param ossFilePath     oss上传后的路径 
	 * @return: void
	 * @date:   2019年4月17日 下午1:37:33       
	 * @throws
	 */
	public void deleteObject(OSSClient ossClient,String bucketName,String ossFilePath) {
	    // 删除Object。详细请参看“SDK手册 > Java-SDK > 管理文件”。
	    // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_object.html?spm=5176.docoss/sdk/java-sdk/manage_bucket
	    ossClient.deleteObject(bucketName,ossFilePath); //ossFilePath = "files/0.jpg"
	    System.out.println("删除Object：" + ossFilePath+ "成功。");
	}
	public void shutdown(OSSClient ossClient){
           ossClient.shutdown();
	}
	public static void main(String[] args) {
		String bucketName="hkaom-oss";
		String endpoint="http://oss-cn-hongkong.aliyuncs.com";
		String accessKeyId="LTAIStB1CY0pRUEK";String accessKeySecret="ToV6LyW9s2pyVyvKuFpYQCWhk464JS";
		String endpointNotWithHttp="oss-cn-hongkong.aliyuncs.com";
		OssUtils instance = OssUtils.getInstance();
		OSSClient ossClient = instance.getOssClient(endpoint, accessKeyId, accessKeySecret);
//		instance.createBucket(ossClient, bucketName);
//		instance.getBucketInfo(ossClient, bucketName);
		
		String url = instance.putObjectFile(ossClient, bucketName, "C:/Users/huanw/Desktop/0.jpg", "122222.jpg", endpointNotWithHttp);
		System.out.println(url);
//		instance.listObjects(ossClient, bucketName);
//		instance.deleteObject(ossClient, bucketName, "测试/19.jpg");
		instance.shutdown(ossClient);
	}
}
