var baseURL = "../../";
var ctx = "";
var localPath = "";
var proName = "";


function getRootPath(){
	//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
	var currentPath=window.document.location.href;
	//获取主机地址之后的目录，如： /uimcardprj/share/meun.jsp
	var pathName=window.document.location.pathname;
	var pos=currentPath.indexOf(pathName);
	//获取主机地址，如： http://localhost:8083
	var localhostPath=currentPath.substring(0,pos);
	//获取带"/"的项目名，如：/uimcardprj
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	localPath = localhostPath;
}

function getPath(){
	$.ajax({
		type:"GET",
		async: false,
	    url: "hkaom/getContextPath",
	    success: function(r){
	    	if(r.code === 0){
	    		proName = r.contextPath
	    		ctx = localPath+proName;
	    		sessionStorage.setItem('_ctx',ctx+'/');
			}
		}
	});
}

getRootPath();
getPath();

//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost:8080/index.html?id=123
// T.p('id') --> 123;
var url = function(name) {
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)return  unescape(r[2]); return null;
};
T.p = url;

//全局配置
$.ajaxSetup({
	dataType: "json",
	cache: false,
    complete:function(XMLHttpRequest,textStatus){
//    	 console.log("------------------------------");
//    	 console.log(textStatus);
         //通过XMLHttpRequest取得响应结果
         var res = XMLHttpRequest.responseText;
         try{
           var jsonData = JSON.parse(res);
           if(jsonData.code == 999){
        	   //跳转网站登录页面
        	   window.location.href = ctx + '/hkaom/signin.html';
           }else{
               //正常请求不需要处理
           }
         }catch(e){
        	 //ajax后台跳转登录
        	 if(textStatus=="parsererror"){
                 //alert('提示信息', "登陸超時！請重新登陸！");
                 window.location.href = ctx + '/hkaoMLogin.html';
             } else if(textStatus=="error"){
                 //alert('提示信息', "請求超時！請稍後再試！", 'info');
             }
         }
   }
});



//判断是否为空
function isBlank(value) {
    return !value || !/\S/.test(value)
}


function getStrLenght(message,MaxLenght){
    var strlenght = 0; //初始定义长度为0
    var txtval = $.trim(message);
    for (var i = 0; i < txtval.length; i++) {
        if (isCN(txtval.charAt(i)) == true) {
            strlenght = strlenght + 2; //中文为2个字符
        } else {
            strlenght = strlenght + 1; //英文一个字符
        }
    }
    return strlenght > MaxLenght ? false : true;
}

function isCN(str) { //判断是不是中文
	var regexCh = /[u00-uff]/;
	return !regexCh.test(str);
}