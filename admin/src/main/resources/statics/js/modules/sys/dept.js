layui.config({
    base : ctx+"/statics/libs/"
}).extend({
    "layTreeTable" : "layTreeTable"
})
var	re;
layui.use(['layTreeTable','form','element','upload','laydate'],function(){
    var form = layui.form,
        treeTable = layui.layTreeTable;
        upload = layui.upload;
        laydate = layui.laydate;
    var user = JSON.parse(sessionStorage.getItem('x-user'));
    re = treeTable.render({
        elem: '#tree-table',
        url:ctx + "sys/dept/list",
        top_value: user.userId==1?'0':user.deptId,
        icon_key: 'deptId',
        primary_key: 'deptId',
        parent_key: 'parentId',
        is_checkbox: true,
        cols: [

            {title: '网点ID',key: 'deptId',minWidth: '100px',align: 'center'},
            {title: '网点名称',key: 'name',minWidth: '100px',align: 'center'},
            {title: '上级网点',key: 'parentName',minWidth: '100px',align: 'center'},
            {title: '二维码',key: 'qrcodeurl',minWidth: '100px',align: 'center',template: function(item){
				return item.qrcodeurl == null ?'无':'<img class="layui-upload-img" style="width: 50px;height: 50px;" src="'+item.imagesHttp+item.qrcodeurl+'">';
            }},
            {title: '展示时间',key: 'showTime',minWidth: '100px',align: 'center',template: function(item){
				return item.showTime == null ?'无':item.showTime;
            }},
            {title: '排序号',key: 'orderNum',minWidth: '100px',align: 'center'}


        ]

    });



    form.on('submit(*)', function(data){
        vm.saveOrUpdate()
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
    
  //普通图片上传
    var uploadInst = upload.render({
      elem: '#qrcodeupload'
//      ,url: '/upload/'
    	  
      ,url:ctx + "oldupload/uploadFile"
      ,before: function(obj){
        //预读本地文件示例，不支持ie8
        obj.preview(function(index, file, result){
          $('#qrcodeimg').attr('src', result); //图片链接（base64）
        });
      }
      ,accept: 'file' //普通文件
      ,done: function(res){
        //如果上传失败
        if(res.code > 0){
          return layer.msg('上传失败');
        }
        //上传成功
        if(res.code == 0 ){
        	vm.dept.qrcodetitle = res.data.oldName;
			vm.dept.qrcodeurl = res.data.wuHttpFilePath;
			vm.imgFlag =true;
        }
      }
      ,error: function(){
        //演示失败状态，并实现重传
        var demoText = $('#demoText');
        demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
        demoText.find('.demo-reload').on('click', function(){
          uploadInst.upload();
        });
      }
    });
    
    //时间范围
    laydate.render({
      elem: '#showTime'
      ,type: 'time'
      ,range: true
      ,done: function(value, date, endDate){
    	    console.log(value); //得到日期生成的值，如：2017-08-18
    	    console.log(date); //得到日期时间对象：{year: 2017, month: 8, date: 18, hours: 0, minutes: 0, seconds: 0}
    	    console.log(endDate); //得结束的日期时间对象，开启范围选择（range: true）才会返回。对象成员同上。
    	    vm.dept.showTime=value;
    	  }
    });

})

var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};
var ztree;

var vm = new Vue({
    el:'#rrapp',
    data:{
        showList: true,
        title: null,
        dept:{
            parentName:null,
            parentId:0,
            orderNum:0,
            showTime:null
        },
        image:'',
        imgFlag:false
    },
    methods: {
        getDept: function(){
            //加载网点树
            $.get(ctx + "sys/dept/select", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r.deptList);
                var node = ztree.getNodeByParam("deptId", vm.dept.parentId);
                ztree.selectNode(node);
                vm.dept.parentName = node.name;
            })
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.image = "";
            vm.imgFlag =false;
            vm.dept.showTime='';
            $("#showTime").val("");
            $('#qrcodeimg').removeAttr("src");
            vm.dept = {parentName:'',parentId:"0",orderNum:0,qrcodetitle:'',qrcodeurl:'',showTime:''};
            vm.getDept();
        },
        update: function () {
            var ids = layui.layTreeTable.checked(re);
            if(ids.length ==0 || ids.length>1){
                layer.msg('请选择一条记录进行修改');
                return ;
            }

            $.get(ctx + "sys/dept/info/"+ids[0], function(r){
            	debugger;
                vm.showList = false;
                vm.title = "修改";
                $('#qrcodeimg').removeAttr("src");
                vm.imgFlag =false;
                vm.dept.showTime='';
                $("#showTime").attr("");
                if(r.dept.qrcodeurl != null && r.dept.qrcodeurl!=''){
                	vm.image = r.dept.imagesHttp+r.dept.qrcodeurl;
                	vm.imgFlag =true;
                	$('#qrcodeimg').attr('src', vm.image);
                }
                $("#showTime").val(r.dept.showTime);
                vm.dept = r.dept;
                vm.getDept();
            });
        },
        del: function () {
            var ids = layui.layTreeTable.checked(re);
            if(ids.length ==0 || ids.length>1){
                layer.msg('请选择一条记录进行刪除');
                return ;
            }

            layer.confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: ctx + "sys/dept/delete",
                    data: "deptId=" + ids[0],
                    success: function(r){
                        if(r.code == 0){
                            layer.msg('操作成功');
                            vm.reload();
                        }else{
                            layer.msg(r.msg);
                        }
                    }
                });
            });
        },
        test: function () {
        	window.location.href=ctx + "wx/test?code=人人";
//                $.ajax({
//                    type: "POST",
//                    url: ctx + "wx/test",
//                    data: {code : '人人'},
//                    success: function(r){
//                    	console.log(r);
////                        if(r.code == 0){
////                            layer.msg('操作成功');
////                            vm.reload();
////                        }else{
////                            layer.msg(r.msg);
////                        }
//                    }
//                });
        },
        saveOrUpdate: function (event) {
            var url = vm.dept.deptId == null ? "sys/dept/save" : "sys/dept/update";
            $.ajax({
                type: "POST",
                url: ctx + url,
                contentType: "application/json",
                data: JSON.stringify(vm.dept),
                success: function(r){
                    if(r.code == 0){
                        layer.msg('操作成功');
                        vm.image = "";
                        vm.reload();
                    }else{
                        layer.msg(r.msg);
                    }
                }
            });
        },
        deptTree: function(){
            localLayer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择网点",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级网点
                    vm.dept.parentId = node[0].deptId;
                    vm.dept.parentName = node[0].name;

                    localLayer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            layui.layTreeTable.render(re);
        }
    }
});
