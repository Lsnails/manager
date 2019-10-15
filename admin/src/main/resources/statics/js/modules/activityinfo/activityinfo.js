
layui.use(['form','laydate','element','table',],function(){
	var form = layui.form,
		laydate = layui.laydate,
		element = layui.element,
		table = layui.table;

	var tableIns = table.render({
		elem: '#listData',
		method:'POST',
		url : ctx+'cms/activityinfo/list',
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
			{ title: 'activityinfoId',field: 'activityinfo_id',hide:true },
			{ title: '活动名称',field: 'name',minWidth: 200}, 
			{ title: '活动状态 ',field: 'status',minWidth: 70,templet: function(item){
//				0：关闭 1：启用
				if (item.status == 0) {
					return '<span class="x-text-red">关闭</span>';
				}
				if (item.status == 1) {
					return '<span class="x-text-green">启用</span>';
				}else{
					return '其他';
				}
            }},
			{ title: '创建时间',field: 'createtime',minWidth: 70}, 
			{ title: '最新修改时间',field: 'lastmodifytime',minWidth: 70}
			//{ title:"操作",fixed:'right',toolbar: "#operationBar",width:100}
		]]
	});

	form.on('checkbox()', function(data){

	});
	form.on('radio()', function(data){

	});
	form.on('select()', function(data){

	});

	form.on('select(status)', function(data){
		debugger;
		vm.activityinfo.status = data.value;
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



})


var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q:{name:''},
		activityinfo: {},
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.activityinfo = {};
		},
		update: function (event) {

			var objs = layui.table.checkStatus('listTable');
			if(objs.data.length==0 || objs.data.length>1){
				layer.msg('请选择一条记录进行修改');
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.getInfo(objs.data[0].activityinfoId);

		},
        saveOrUpdate: function (flag) {
			var url = vm.activityinfo.activityinfoId == null ? "cms/activityinfo/save" : "cms/activityinfo/update";
			var type = vm.activityinfo.activityinfoId == null ? "post":"put";

			$.ajax({
				type: type,
				url: ctx + url,
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(vm.activityinfo),
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
				ids.push(objs.data[key].activityinfoId)
			}
			layer.confirm('确定删除？', function(){
				$.ajax({
					type: "DELETE",
				    url: ctx + "cms/activityinfo/delete",
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
		updateStatus0:function (event) {
			var objs = layui.table.checkStatus('listTable');
			if(objs.data.length==0 || objs.data.length>1){
				layer.msg('请选择一条记录');
				return ;
			}
			var id = objs.data[0].activityinfoId;
			if(objs.data[0].status == '0'){
				layer.msg('已关闭！');
				return;
			}
			debugger;
			layer.confirm('确定关闭该活动吗？', function(){
				$.ajax({
					type: "PUT",
				    url: ctx + "cms/activityinfo/updateStatus/"+id+"/"+0,
                    contentType: "application/json",
//				    data: JSON.stringify(ids),
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
		updateStatus1:function (event) {
			var objs = layui.table.checkStatus('listTable');
			if(objs.data.length==0 || objs.data.length>1){
				layer.msg('请选择一条记录');
				return ;
			}
			var id = objs.data[0].activityinfoId;
			if(objs.data[0].status == '1'){
				layer.msg('已启动！');
				return;
			}
			layer.confirm('确定启动该活动吗？', function(){
				$.ajax({
					type: "PUT",
				    url: ctx + "cms/activityinfo/updateStatus/"+id+"/"+1,
                    contentType: "application/json",
//				    data: {},
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
		getInfo: function(activityinfoId){
			$.get(ctx + "cms/activityinfo/info/"+activityinfoId, function(r){
                vm.activityinfo = r.activityinfo;
            });
		},

		closePanel: function (event) {
			vm.showList = true;
			vm.activityinfo= {};

		},
		
		reload: function () {
			vm.showList = true;
			layui.table.reload("listTable",{
				page: {
					curr: 1
				},
				where: {
					name: vm.q.name,

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