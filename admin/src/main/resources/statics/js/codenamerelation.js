
layui.use(['form','laydate','element','table',],function(){
	var form = layui.form,
		laydate = layui.laydate,
		element = layui.element,
		table = layui.table;

	var tableIns = table.render({
		elem: '#listData',
		method:'POST',
		url : ctx+'cms/codenamerelation/list',
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
		toolbar: '#toptoolbar',
		defaultToolbar: ['filter'],
		cols : [[
			{ type: "checkbox", fixed:"left", width:50},
												{ title: 'id',field: 'id',hidden:true },
																{ title: 'code',field: 'code',minWidth: 70}, 
																{ title: 'name',field: 'name',minWidth: 70}, 
																{ title: 'type',field: 'type',minWidth: 70}, 
																{ title: 'fullName',field: 'full_name',minWidth: 70}, 
																{ title: 'createDate',field: 'create_date',minWidth: 70}
									]]
	});

	form.on('checkbox()', function(data){

	});
	form.on('radio()', function(data){

	});
	form.on('select()', function(data){

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
		q:{},
		codeNameRelation: {},
	},
	methods: {
		query: function () {
			vm.reload(language);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.codeNameRelation = {};
		},
		update: function (event) {

			var objs = layui.table.checkStatus('listTable');
			if(objs.data.length==0 || objs.data.length>1){
				layer.msg('请选择一条记录进行修改');
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.getInfo(objs.data[0].codeNameRelationId);

		},
        saveOrUpdate: function (flag) {
			var url = vm.codeNameRelation.id == null ? "cms/codenamerelation/save" : "cms/codenamerelation/update";
			var type = vm.codeNameRelation.id == null ? "post":"put";

			$.ajax({
				type: type,
				url: ctx + url,
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(vm.codeNameRelation),
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
				ids.push(objs.data[key].codeNameRelationId)
			}
			layer.confirm(tips, function(){
				$.ajax({
					type: "DELETE",
				    url: ctx + "cms/codenamerelation/delete",
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
			$.get(ctx + "cms/codenamerelation/info/"+id, function(r){
                vm.codeNameRelation = r.codeNameRelation;
            });
		},

		closePanel: function (event) {
			vm.showList = true;
			vm.codeNameRelation= {};

		},
		
		reload: function () {
			vm.showList = true;
			layui.table.reload("listTable",{
				page: {
					curr: 1
				},
				where: {
					username: vm.q.,

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