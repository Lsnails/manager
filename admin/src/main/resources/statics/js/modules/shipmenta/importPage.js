
layui.use(['form','laydate','element','table','upload','layer',],function(){
	var form = layui.form,
		laydate = layui.laydate,
		element = layui.element,
		table = layui.table,
		localLayer = layui.layer,//在内容区弹出
		upload = layui.upload;


	form.on('submit(*)', function(data){
		vm.saveOrUpdate()
		return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});

	
	form.on('select(impTypeSelect)', function(data){
        vm.q.impType = data.value;
    });
	
	form.on('select(shopUnitSelect)', function(data){
        vm.q.shopUnit = data.value;
        $("#chooseDiv").css('display', 'block');
        $("#btnDiv").css('display', 'block');
    });
	
	
//	upload.render({ //允许上传的文件后缀
//	    elem: '#shipmentaUpload',
//	    url: ctx + 'cms/storagea/uploadFile',
//	    accept: 'file', //普通文件
//	    before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
//	    	 layer.msg('上传中', {
//        	   icon: 16
//        	   ,shade: 0.01
//        	  });
//	    },
//	    exts: 'xlsx|xls|cvs', //只允许上传压缩文件
//	    done: function(res){
//	    	layer.msg("上传成功");
//	    	console.log(res)
//	    	vm.reload();
//	    },
//	    error: function(index, upload){
//	    	layer.msg('服务器出错。联系管理员', {icon: 5});
//	    }
//	  })
	  
	  //选完文件后不自动上传
	  upload.render({
	    elem: '#chooseFile',
	    url: ctx + 'cms/shipmenta/uploadFile',
	    data: {"impType": function(){return vm.q.impType;},
	    		"shopUnit":function(){return vm.q.shopUnit;}
	    },
	    auto: false,
	    exts: 'xlsx|xls|cvs|csv', //只允许上传压缩文件
	    accept: 'file', //普通文件
	    before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
//	    	return false;
	    	 layer.msg('上传中', {
        	   icon: 16
        	   ,shade: 0.01
        	  });
	    },
//	    choose: function(obj){
//	    	debugger;
//	    	var impType = vm.q.impType;
//	    	console.log(impType);
//	     },
	    //,multiple: true
	    bindAction: '#shipmentaUpload',
	    done: function(res){
	    	layer.msg("上传成功");
	    	vm.cancle();
	    	window.parent.location.reload();//刷新父页面
//	    	console.log(res)
//	    	vm.reload();
	    },
	    error: function(index, upload){
	    	layer.msg('服务器出错。联系管理员', {icon: 5});
	    }
	  });

})


var vm = new Vue({
	el:'#rrapp2',
	data:{
		showList: true,
		title: null,
		q:{
			impType:'',
			shopUnit:''
		},
		shipmenta: {},
	},
	methods: {
		cancle:function(){
			//假设这是iframe页
			var index = parent.layui.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layui.layer.close(index); //再执行关闭   
		},
		saveOrUpdate: function () {
			var url = "cms/shipmenta/save";
			$.ajax({
				type: "post",
				url: ctx + url,
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(vm.shipmenta),
				success: function(r){
					if(r.code == 0){
						layer.msg('操作成功');
						vm.reload();
					}else{
						layer.msg(r.msg);
					}
				}
			});
		}
	},
	updated:function(){
		layui.form.render('select','myForm');
		layui.form.render('checkbox','myForm');
		layui.form.render('radio','myForm');
	}
});