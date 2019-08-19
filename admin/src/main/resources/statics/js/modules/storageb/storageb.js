
layui.use(['form','laydate','element','table',],function(){
	var form = layui.form,
		laydate = layui.laydate,
		element = layui.element,
		table = layui.table;

	var tableIns = table.render({
		elem: '#listData',
		method:'POST',
		url : ctx+'storageb/list',
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
			{ title: 'id',field: 'id',hide:true },
				{ title: '',field: 'storageId',minWidth: 70}, 
				{ title: '日期',field: 'date',minWidth: 70}, 
				{ title: '供应商',field: 'supplier',minWidth: 70}, 
				{ title: '编号',field: 'number',minWidth: 70}, 
				{ title: '验收',field: 'accept',minWidth: 70}, 
				{ title: '保管',field: 'storage',minWidth: 70}, 
				{ title: '采购方式  1 : 赊购',field: 'buyType',minWidth: 70}, 
				{ title: '物料编码',field: 'materNumber',minWidth: 70}, 
				{ title: '物料名称',field: 'materName',minWidth: 70}, 
				{ title: '单位 1：台',field: 'unit',minWidth: 70}, 
				{ title: '数量',field: 'amount',minWidth: 70}, 
				{ title: '单价',field: 'unitPrice',minWidth: 70}, 
				{ title: '金额',field: 'price',minWidth: 70}, 
				{ title: '收料仓库',field: 'warehouse',minWidth: 70}, 
				{ title: '仓位',field: 'position',minWidth: 70}, 
				{ title: '创建时间',field: 'createDate',minWidth: 70}, 
				{ title: '修改时间',field: 'updateDate',minWidth: 70}
				{ title:"操作",fixed:'right',toolbar: "#operationBar",width:100}
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



})


var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q:{},
		storageb: {},
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.storageb = {};
		},
		update: function (event) {

			var objs = layui.table.checkStatus('listTable');
			if(objs.data.length==0 || objs.data.length>1){
				layer.msg('请选择一条记录进行修改');
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.getInfo(objs.data[0].storagebId);

		},
        saveOrUpdate: function (flag) {
			var url = vm.storageb.id == null ? "storageb/save" : "storageb/update";
			var type = vm.storageb.id == null ? "post":"put";

			$.ajax({
				type: type,
				url: ctx + url,
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(vm.storageb),
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
				ids.push(objs.data[key].storagebId)
			}
			layer.confirm('确定删除？', function(){
				$.ajax({
					type: "DELETE",
				    url: ctx + "storageb/delete",
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
			$.get(ctx + "storageb/info/"+id, function(r){
                vm.storageb = r.storageb;
            });
		},

		closePanel: function (event) {
			vm.showList = true;
			vm.storageb= {};

		},
		
		reload: function () {
			vm.showList = true;
			layui.table.reload("listTable",{
				page: {
					curr: 1
				},
				where: {
					name: vm.q,

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