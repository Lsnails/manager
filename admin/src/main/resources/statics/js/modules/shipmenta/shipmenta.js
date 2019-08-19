
layui.use(['form','laydate','element','table','upload','layer',],function(){
	var form = layui.form,
		laydate = layui.laydate,
		element = layui.element,
		table = layui.table,
		localLayer = layui.layer,//在内容区弹出
		upload = layui.upload;

	var tableIns = table.render({
		elem: '#listData',
		method:'POST',
		url : ctx+'cms/shipmenta/list',
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
				{ title: '导入日期',field: 'impDate',minWidth: 70}, 
				{ title: '导入名称',field: 'impName',minWidth: 70}, 
				{ title: '类型',field: 'impType',minWidth: 70,templet: function(item){
//					1：京东 2：天猫 3：淘宝 4：拼多多
					if (item.impType == 1) {
						return '<span class="x-text-normal">京东</span>';
					}
					if (item.impType == 2) {
						return '<span class="x-text-green">天猫</span>';
					}
					if (item.impType == 3) {
						return '<span class="x-text-green">淘宝</span>';
					}
					if (item.impType == 4) {
						return '<span class="x-text-orange">拼多多</span>';
					}else{
						return '其他';
					}
					
				}}, 
				{ title: '输出编码',field: 'outCode',minWidth: 70}, 
				{ title: '创建时间',field: 'createDate',minWidth: 70}
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
	    elem: '#shipmentaUpload',
	    url: ctx + 'cms/storagea/uploadFile',
	    accept: 'file', //普通文件
	    before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
	    	 layer.msg('上传中', {
        	   icon: 16
        	   ,shade: 0.01
        	  });
	    },
	    exts: 'xlsx|xls|cvs', //只允许上传压缩文件
	    done: function(res){
	    	layer.msg("上传成功");
	    	console.log(res)
	    	vm.reload();
	    },
	    error: function(index, upload){
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
		shipmenta: {},
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.shipmenta = {};
		},
		showExpression:function(){
			layui.layer.open({
	            title :"出库导入",
	            type : 2,
	            area: ['50%', '50%'],
	            shade:0,//不要遮罩
	            content :ctx+"modules/shipmenta/importPage.html",
	            yes: function(index, layero){
	                localLayer.close(index)
	            }

	        })
		},
		update: function (event) {

			var objs = layui.table.checkStatus('listTable');
			if(objs.data.length==0 || objs.data.length>1){
				layer.msg('请选择一条记录进行修改');
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.getInfo(objs.data[0].shipmentaId);

		},
        saveOrUpdate: function (flag) {
			var url = vm.shipmenta.id == null ? "cms/shipmenta/save" : "cms/shipmenta/update";
			var type = vm.shipmenta.id == null ? "post":"put";

			$.ajax({
				type: type,
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
		},
		del: function (event) {

			var objs = layui.table.checkStatus('listTable');
			if(objs.data.length==0){
				layer.msg('请选择要删除的内容');
				return ;
			}
			var ids = [];
			for(var key in objs.data){
				ids.push(objs.data[key].shipmentaId)
			}
			layer.confirm('确定删除？', function(){
				$.ajax({
					type: "DELETE",
				    url: ctx + "cms/shipmenta/delete",
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
			$.get(ctx + "cms/shipmenta/info/"+id, function(r){
                vm.shipmenta = r.shipmenta;
            });
		},

		closePanel: function (event) {
			vm.showList = true;
			vm.shipmenta= {};

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