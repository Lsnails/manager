
layui.use(['form','laydate','element','table',],function(){
	var form = layui.form,
		laydate = layui.laydate,
		element = layui.element,
		table = layui.table;

	var tableIns = table.render({
		elem: '#listData',
		method:'POST',
		url : ctx+'cms/wxuser/list',
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
		totalRow: true
		,toolbar: true,
//		toolbar: '#toptoolbar',
//		defaultToolbar: ['filter'],
		cols : [[
			{ type: "checkbox", fixed:"left", width:50},
			{ title: 'id',field: 'id',hide:true },
				{ title: 'wx openId唯一标识',field: 'openId',minWidth: 120}, 
//				{ title: '活动id',field: 'activityId',minWidth: 70}, 
				{ title: '活动名称',field: 'activityName',minWidth: 70}, 
//				{ title: '网点id',field: 'networkId',minWidth: 70}, 
				{ title: '网点名称',field: 'networkName',minWidth: 70}, 
				{ title: '是否核销',field: 'state',minWidth: 60,templet: function(item){
//					0 未核销 1已核销
					if (item.state == 0) {
						return '<span class="x-text-red">未核销</span>';
					}
					if (item.state == 1) {
						return '<span class="x-text-green">已核销</span>';
					}else{
						return '其他';
					}
	            }}, 
				{ title: '手机号',field: 'phone',minWidth: 50}, 
				{ title: '用户唯一编码',field: 'userCode',minWidth: 120}, 
				{ title: '自定义数据1',field: 'remark',minWidth: 120}, 
				{ title: '自定义数据2',field: 'remark2',minWidth: 120}, 
				{ title: '自定义数据3',field: 'remark3',minWidth: 120}, 
				{ title: '自定义数据4',field: 'remark4',minWidth: 120}, 
				{ title: '自定义数据5',field: 'remark5',minWidth: 120}, 
				{ title: '创建时间',field: 'createDate',minWidth: 100}, 
				{ title: '修改时间',field: 'updateDate',minWidth: 100}
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
	form.on('select(searchActivityList)', function(data){
		vm.q.activityId = data.value;
    });
	
	layui.form.render('checkbox','myForm');
	layui.form.render('radio','myForm');

})


var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q:{
			activityId: null,
			networkName: null,
			phone: null,
			userCode: null
		},
		wxUser: {},
		activityList:[]
	},
	methods: {
		query: function () {
			vm.reload();
		},
		clearQuery: function () {
			vm.q.activityId = ''
			vm.q.networkName = ''
			vm.q.phone = ''
			vm.q.userCode = ''
			vm.reload();
		},
        selectActivityList: function () {
			$.ajax({
				type: "post",
				contentType: "application/json; charset=utf-8",
				url: ctx + "cms/activityinfo/allList",
				data: {},
				success: function(r){
					if(r.code == 0){
						vm.activityList = r.data;
						//layui.form.render('select','searchForm');
					}
				}
			});
		},
		hexiao:function(event){
			var objs = layui.table.checkStatus('listTable');
			if(objs.data.length==0){
				layer.msg('请选择要核销的内容');
				return ;
			}
			var ids = [];
			for(var key in objs.data){
				if(objs.data[key].state==1){
					continue;
				}
				ids.push(objs.data[key].id)
			}
			if(ids.length==0){
				layer.msg('您选择的均为已核销的，请选择未核销内容');
				return ;
			}
			layer.confirm('确定要核销？', function(){
				$.ajax({
					type: "post",
				    url: ctx + "cms/wxuser/hexiao",
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
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.wxUser = {};
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
			var url = vm.wxUser.id == null ? "cms/wxuser/save" : "cms/wxuser/update";
			var type = vm.wxUser.id == null ? "post":"put";
			$.ajax({
				type: type,
				url: ctx + url,
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(vm.wxUser),
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
				ids.push(objs.data[key].id)
			}
			layer.confirm('确定删除？', function(){
				$.ajax({
					type: "DELETE",
				    url: ctx + "cms/wxuser/delete",
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
			$.get(ctx + "cms/wxuser/info/"+id, function(r){
                vm.wxUser = r.wxUser;
            });
		},

		closePanel: function (event) {
			vm.showList = true;
			vm.wxUser= {};

		},
		
		reload: function () {
			vm.showList = true;
			layui.table.reload("listTable",{
				page: {
					curr: 1
				},
				where: {
					//name: vm.q,
					activityId: vm.q.activityId,
					networkName: vm.q.networkName,
					phone: vm.q.phone,
					userCode: vm.q.userCode

				}
			})
		}
	},
	updated:function(){
		
	},
	mounted:function(){
		this.selectActivityList();
	},
	watch:{
		activityList:function(){
//			this.$nextTrick(function(){
				layui.form.render('select','searchForm');
//			})
			
		}
	}
});