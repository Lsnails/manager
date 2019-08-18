//package com.base.modules.job.task;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.base.modules.customizesys.helper.Constant;
//import com.base.modules.customizesys.upload.service.UploadFilesService;
//import com.base.modules.customizesys.upload.utils.DateUtil;
//
//
///**
// * 处理上传临时文件定时任务
// * testTask为spring bean的名称
// * @ClassName:  FileManagementTask   
// * @Description:处理上传临时文件定时任务
// * @author:  huanw
// * @date:   2019年1月25日 下午3:36:53
// */
//@Component("fileManagementTask")
//public class FileManagementTask {
//	private Logger logger = LoggerFactory.getLogger(getClass());
//	
//	@Autowired
//	private UploadFilesService uploadFilesService;
//	/**
//	 * 1.将据目前24小时之前的状态为0（新增）的数据，状态批量修改为2
//     * 2.将biz_upload_files状态为2（临时）的数据，批量插入到biz_upload_files_del表，然后删除biz_upload_files状态为2的数据
//	 * @Title: test   
//	 * @Description: TODO(这里用一句话描述这个方法的作用)   
//	 * @author: huanw
//	 * @param: @param params      
//	 * @return: void
//	 * @date:   2019年1月25日 下午4:11:32       
//	 * @throws
//	 */
//	public void uploadFileDataManagement(){
//		String startTime = DateUtil.getBeforeByHourTime(24);
//		String nowTime = DateUtil.getNowDate();
//		//为了测试使用当前时间，正式应该是当前时间的24小时之前的数据
////		startTime=nowTime;
//		logger.info("开始跑管理上传文件的定时任务");
//		//1.将据目前24小时之前的状态为0（新增）的数据，状态批量修改为2
//		uploadFilesService.updateFileStatusByTimeandStatus(Constant.FILE_STATUS_2, Constant.FILE_STATUS_0,startTime,nowTime);
//		//2.将biz_upload_files状态为2（临时）的数据，批量插入到biz_upload_files_del表，然后删除biz_upload_files状态为2的数据
//		uploadFilesService.insertUploadFilesDel();
//		//2.删除biz_upload_files表中的在此时间段内状态为2的数据
//		uploadFilesService.deleteUplodFilesByStatus(Constant.FILE_STATUS_2, startTime, nowTime);
//		//3.服务器上所有文件均为素材，素材数量到达一定程度删除
//		
//		//4.删除素材同时删除数据
//		
//		logger.info("管理上传文件的定时任务结束");
//	}
//}
