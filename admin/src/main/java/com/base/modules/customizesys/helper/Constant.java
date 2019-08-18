package com.base.modules.customizesys.helper;

public class Constant {

	public static final String DEFAULT_PWD="123456";
	/**
	 * 状态（0：未发布 1：发布到网站）
	 */
	public static final String STATUS_0 ="0";
	/**
	 * 状态（0：未发布 1：发布到网站）
	 */
	public static final String STATUS_1 ="1";
	
	/**
	 * 是否在首页显示（0：不显示 1：显示）
	 */
	public static final String ENABLED_0 ="0";
	/**
	 * 是否在首页显示（0：不显示 1：显示）
	 */
	public static final String ENABLED_1 ="1";
	
	
	/**
	 * 上传文件状态（0：新增 1：使用 2：临时）
	 */
	public static final String FILE_STATUS_0="0";
	/**
	 * 上传文件状态（0：新增 1：使用 2：临时）
	 */
	public static final String FILE_STATUS_1="1";
	/**
	 * 上传文件状态（0：新增 1：使用 2：临时）
	 */
	public static final String FILE_STATUS_2="2";
	
	
	/**
	 * 表示：   0：新闻
	 * 模块编码 0：新闻 1：课程 2：教授   3：案例 4:历届学友 5：知识库的领域
	 */
	public static final String MODULE_NEWS_0="0";
	/**
	 *  表示：  1：课程 
	 * 模块编码 0：新闻 1：课程 2：教授   3：案例 4:历届学友 5：知识库的领域
	 */
	public static final String MODULE_COURSE_1="1";
	/**
	 * 表示：  2：教授   
	 * 模块编码   0：新闻 1：课程   2：教授    3：案例   4:历届学友   5：知识库的领域
	 */
	public static final String MODULE_PROFESSOR_2="2";
	/**
	 * 表示： 3：案例
	 * 模块编码   0：新闻 1：课程   2：教授    3：案例   4:历届学友   5：知识库的领域  
	 */
	public static final String MODULE_CASES_3="3";
	
	/**
	 * 表示：   4:历届学友     
	 * 模块编码   0：新闻 1：课程   2：教授    3：案例   4:历届学友   5：知识库的领域
	 */
	public static final String MODULE_SUCCESSIVE_ALUMNI_4="4";
	
	/**
	 * 表示：   5：知识库的领域
	 * 模块编码   0：新闻 1：课程   2：教授    3：案例   4:历届学友   5：知识库的领域
	 */
	public static final String MODULE_REALM_5="5";
	/**
	 * 表示：   6:友情链接
	 * 模块编码 0：新闻    1：课程     2：教授   3：案例     4:历届学友     5：知识库的领域    6:友情链接  7:联系合作   8:媒体社交
	 */
	public static final String MODULE_FRIENDLINK_6="6";
	/**
	 * 表示：  7:联系合作
	 * 模块编码 0：新闻    1：课程     2：教授   3：案例     4:历届学友     5：知识库的领域    6:友情链接  7:联系合作   8:媒体社交
	 */
	public static final String MODULE_CONTACT_7="7";
	/**
	 * 表示：  8:媒体社交
	 * 模块编码 0：新闻    1：课程     2：教授   3：案例     4:历届学友     5：知识库的领域    6:友情链接  7:联系合作   8:媒体社交
	 */
	public static final String MODULE_MEDIA_8="8";

	/**
	 * 语种：1：中文  2：英文
	 */
	public static final String LANGUAGE_ZH_1="1";
	/**
	 *  语种：1：中文  2：英文
	 */
	public static final String LANGUAGE_EN_2="2";
	/**
	 * 网站用户类型（0为个人用户，1为企业）
	 */
	public static final String USER_TYPE_0="0";
	/**
	 * 网站用户类型（0为个人用户，1为企业）
	 */
	public static final String USER_TYPE_1="1";
	
	/**
	 * solr查询core名称
	 */
	public static final String XGGLXY_CORE="xgglxy_core";
	
	/**
	 * 2：已取消
	 * 订单状态（0：待付款   1：已付款   2：已取消）
	 */
	public  static final String ORDER_STATUS_CANCEL="2";

	/**
	 * SolrAnnotation不同类型，取参数方式不一致，根据type决定参数的取法
	 * 1.type=updateVo     表示：参数是vo对象，需要从对象中取出solrId，作为删除solr缓存的唯一id
	 * 2.type=updateStatus 表示：需要取出第一个参数作为删除solr缓存的唯一id
	 * 3.type=delete       表示：仅有一个参数，需要出去第一个参数，此参数是数据组，批量删除solr缓存
	 */
	public enum SOLR_ANNOTATION_TYPE {
		updateVo,updateStatus,delete
	}
	/**
	 * CustomizeCacheAnnotation注解type说明：不同类型，取参数方式不一致，根据type决定参数的取法
	 * 1.type=add          表示：新增内容时，保存缓存
	 * 2.type=updateVo     表示：清除原缓存、保存新缓存
	 * 3.type=updateStatus 表示：A当修改状态为发布时，保存缓存，B当修改缓存为未发布时，清除缓存
	 * 4.type=otherUpdate  表示：清除原缓存、保存新缓存
	 * 5.type=delete       表示：清除原缓存
	 */
	public enum CUSTOM_CACHE_ANNOTATION_TYPE {
		add,updateVo,updateStatus,otherUpdate,delete
	}
	/**
	 * 手机验证码的session key为“手机号+VC”
	 */
	public static final String VC="VC";
	/**
	 * 登陆cookies的名称
	 */
	public static final String MHKAOM_MOBLIE="mhkaom_mobile";
	
	/**
	 * 课程中文菜单名称-学院课程。这个名称修改对代码有影响
	 */
	public static final String 	MENU_COURSENAME="學院課程";
	/**
	 * 课程英文菜单名称-学院课程。这个名称修改对代码有影响
	 */
	public static final String 	MENU_COURSENAME_ENGLISH="College course";
	
	/**
	 * 学院师资 中文菜单名称，这个名称修改对代码有影响
	 */
	public static final String 	MENU_PROFESSORNAME="學院師資";
	/**
	 * 学院师资 英文菜单名称，这个名称修改对代码有影响
	 */
	public static final String 	MENU_PROFESSORNAME_ENGLISH="College teachers";
	
	
	/**
	 * 关于我们中文菜单名称-关于我们。这个名称修改对代码有影响
	 */
	public static final String 	MENU_ABOUTUS="關於我們";
	/**
	 * 关于我们英文菜单名称-About Us。这个名称修改对代码有影响
	 */
	public static final String 	MENU_ABOUTUS_ENGLISH="About Us";
	
	/**
	 * 学院课程字典分类名称，中英文都必须叫  学院课程
	 */
	public static final String 	COURSE_CLASSIFICATION="學院課程";
	/**
	 * 学院师资字典分类名称，中英文都必须叫  学院师资
	 */
	public static final String 	PROFESSOR_CLASSIFICATION="學院師資";
	/**
	 * 个人默认图像
	 */
	public static final String DEFAULT_PICTURE="/statics/images/picture.jpg";
	/**
	 * 课程失效时，默认图片
	 */
	public static final String DEFAULT_DELETE_COURSE_PICTURE="/statics/images/obtainedcourse.png";
}
