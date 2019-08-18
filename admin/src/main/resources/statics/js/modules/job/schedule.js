layui.use(['form','layer','laydate','element','table',],function(){
	var form = layui.form,//引入form
		laydate = layui.laydate,//引入日期时间控件
		element = layui.element,
		table = layui.table;
//列表
	var tableIns = table.render({
		elem: '#listData',//表格元素的id
		method:'POST',
		url : ctx+'sys/schedule/list',
		page: {
			layout: ['count', 'prev', 'page', 'next','limit','skip'] //自定义分页布局
			,first: false //不显示首页
			,last: false //不显示尾页
			,groups:5

		},
		height : 'full-80',//设置高度
		loading:true,
		limit : 20,//默认显示条数
		limits : [10,15,20,25,50,100],//显示条数可选数量设置
		id : "listTable",//当前实例Id
		defaultToolbar: ['filter'],//开启设置动态表头功能
		cols : [[
			{ type: "checkbox", fixed:"left", width:50},
			{ title: '任务ID', field: 'jobId',hide: true },
			{ title: 'bean名称', field: 'beanName', minWidth: 50 },
			{ title: '方法名称', field: 'methodName', minWidth: 70 },
			{ title: '参数', field: 'params', minWidth: 150 },
			{ title: 'cron表达式', field: 'cronExpression', minWidth: 80 },
			{ title: '备注', field: 'remark', minWidth: 80 },
			{ title: '状态', field: 'status',templet: function(d){
					return d.status === 0 ?
						'<span class="x-text-green">正常</span>' :
						'<span class="x-text-red">暂停</span>';
			}}


		]]
	});



	form.on('submit(*)', function(data){
		vm.saveOrUpdate()
		return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});



})

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			beanName: null
		},
		showList: true,
		title: null,
		schedule: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.schedule = {};
		},
		update: function () {
			var objs = layui.table.checkStatus('listTable');
			if(objs.data.length==0 || objs.data.length>1){
				layer.msg('请选择一条记录进行修改');
				return ;
			}

			
			$.get(ctx + "sys/schedule/info/"+objs.data[0].jobId, function(r){
				vm.showList = false;
                vm.title = "修改";
				vm.schedule = r.schedule;
			});
		},
		saveOrUpdate: function (event) {
			var url = vm.schedule.jobId == null ? "sys/schedule/save" : "sys/schedule/update";
			$.ajax({
				type: "POST",
			    url: ctx + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.schedule),
			    success: function(r){
			    	if(r.code === 0){
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
				ids.push(objs.data[key].jobId)
			}

			layer.confirm('确认删除?', function(index){
				$.ajax({
					type: "POST",
					url: ctx + "sys/schedule/delete",
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

				layer.close(index);
			});


		},
		pause: function (event) {
			var objs = layui.table.checkStatus('listTable');
			if(objs.data.length==0){
				layer.msg('请选择要暂停的内容');
				return ;
			}
			var ids = [];
			for(var key in objs.data){
				ids.push(objs.data[key].jobId)
			}
			layer.confirm('确定要暂停选中的记录？', function(index){
				$.ajax({
					type: "POST",
					url: ctx + "sys/schedule/pause",
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
				layer.close(index);
			});
		},
		resume: function (event) {
			var objs = layui.table.checkStatus('listTable');
			if(objs.data.length==0){
				layer.msg('请选择要要恢复的内容');
				return ;
			}
			var ids = [];
			for(var key in objs.data){
				ids.push(objs.data[key].jobId)
			}

			layer.confirm('确定要恢复选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: ctx + "sys/schedule/resume",
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
		runOnce: function (event) {
			var objs = layui.table.checkStatus('listTable');
			if(objs.data.length==0){
				layer.msg('请选择要立即执行的内容');
				return ;
			}
			var ids = [];
			for(var key in objs.data){
				ids.push(objs.data[key].jobId)
			}

			layer.confirm('确定要立即执行选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: ctx + "sys/schedule/run",
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
		reload: function (event) {
			vm.showList = true;
			layui.table.reload("listTable",{
				page: {
					curr: 1 //重新从第 1 页开始
				},
				where: {
					beanName: vm.q.beanName,

				}
			})
		}
	}
});