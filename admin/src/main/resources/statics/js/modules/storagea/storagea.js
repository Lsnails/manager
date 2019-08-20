
layui.use(['form','laydate','element','table','upload',],function(){
	var form = layui.form,
		laydate = layui.laydate,
		element = layui.element,
		table = layui.table,
	    upload = layui.upload;

	var tableIns = table.render({
		elem: '#listData',
		method:'POST',
		url : ctx+'cms/storagea/list',
		page: {
			layout: ['count', 'prev', 'page', 'next','limit','skip'] //自定义分页布局
			,first: false
			,last: false
			,groups:5

		},
		height : 'full-80',
		loading:true,
		limit : 20,
		limits : [10,15,20,25,50,100],
		id : "listTable",
//		toolbar: '#toptoolbar',
//		defaultToolbar: ['filter'],
		cols : [[
			{ type: "checkbox", fixed:"left", width:50},
			{ title: 'id',field: 'id',hide:true },
				{ title: '导入日期',field: 'applyDate',minWidth: 70}, 
				{ title: '导入名称',field: 'applyName',minWidth: 70}, 
				{ title: '输出编码',field: 'outCode',minWidth: 70}, 
				{ title: '创建日期',field: 'createDate',minWidth: 70}
//				{ title:"操作",fixed:'right',toolbar: "#operationBar",width:100}
		]]
	});

	form.on('checkbox()', function(data){

	});
	form.on('radio()', function(data){

	});
	form.on('select()', function(data){

	});

	form.on('tool(listData)', function(obj){
		var data = obj.data;
		var layEvent = obj.event;
		if(layEvent == 'look'){


		}
	});

	form.on('submit(*)', function(data){
		vm.saveOrUpdate()
		return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});

	upload.render({ //允许上传的文件后缀
	    elem: '#storageaUpload',
	    url: ctx + 'cms/storagea/uploadFile',
	    accept: 'file', //普通文件
	    before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
//	        layer.load(); //上传loading
	        layer.msg('上传中', {
	        	  icon: 16
	        	  ,shade: 0.01
	        	});
	    },
	    exts: 'xlsx|xls|cvs', //只允许上传压缩文件
	    done: function(res){
//	    	layer.closeAll('loading'); //关闭loading
	    	layer.msg("上传成功");
	    	console.log(res)
	    	vm.reload();
	    },
	    error: function(index, upload){
//	        layer.closeAll('loading'); //关闭loading
	    	layer.msg('服务器出错。联系管理员', {icon: 5});
	    }
	  });
	
	laydate.render({
	    elem: '#searchDate',
	    done: function(value, date){
	    	console.log(date);
	    	console.log(value);
	    	vm.q.searchDate = value;
	    }
	});
	  

})


var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q:{
			searchDate : null
		},
		storagea: {},
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.storagea = {};
		},
		update: function (event) {

			var objs = layui.table.checkStatus('listTable');
			if(objs.data.length==0 || objs.data.length>1){
				layer.msg('请选择一条记录进行修改');
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.getInfo(objs.data[0].id);

		},
        saveOrUpdate: function (flag) {
			var url = vm.storagea.id == null ? "cms/storagea/save" : "cms/storagea/update";
			var type = vm.storagea.id == null ? "post":"put";

			$.ajax({
				type: type,
				url: ctx + url,
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(vm.storagea),
				success: function(r){
					if(r.code == 0){
						layer.msg('操作成功');
						vm.reload();
					}else{
						layer.msg(r.msg);
					}
				}
			});
		},
		exportB:function(event){
			var objs = layui.table.checkStatus('listTable');
			if(objs.data.length ==0 || objs.data.length >1){
				layer.msg('请选择一条要导出的内容');
				return ;
			}
			var ids = [];
			var  storageAId = "";
			for(var key in objs.data){
				storageAId = objs.data[key].id;
			}
			window.open(ctx + "cms/storagea/exportB?storageAId="+storageAId);
		},
		del: function (event) {

			var objs = layui.table.checkStatus('listTable');
			if(objs.data.length==0){
				layer.msg('请选择要删除的内容');
				return ;
			}
			var ids = [];
			for(var key in objs.data){
				ids.push(objs.data[key].id)
			}
			layer.confirm('确定删除？删除数据不可恢复，请慎重！', function(){
				$.ajax({
					type: "DELETE",
				    url: ctx + "cms/storagea/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
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

		getInfo: function(id){
			$.get(ctx + "cms/storagea/info/"+id, function(r){
                vm.storagea = r.storagea;
            });
		},

		closePanel: function (event) {
			vm.showList = true;
			vm.storagea= {};

		},
		
		reload: function () {
			vm.showList = true;
			layui.table.reload("listTable",{
				page: {
					curr: 1
				},
				where: {
					searchDate: vm.q.searchDate,
				}
			})
		}
	},
	updated:function(){
		layui.form.render('select','myForm');
		layui.form.render('checkbox','myForm');
		layui.form.render('radio','myForm');
	}
});