
layui.use(['form','laydate','element','table',],function(){
	var form = layui.form,
		laydate = layui.laydate,
		element = layui.element,
		table = layui.table;

	var tableIns = table.render({
		elem: '#listData',
		method:'POST',
		url : ctx+'shipmentb/list',
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
				{ title: '',field: 'shipmentId',minWidth: 70}, 
				{ title: '日期',field: 'date',minWidth: 70}, 
				{ title: '购货单位',field: 'shopUnit',minWidth: 70}, 
				{ title: '编号',field: 'number',minWidth: 70}, 
				{ title: '销售方式 1：赊销',field: 'saleType',minWidth: 70}, 
				{ title: '发货',field: 'ship',minWidth: 70}, 
				{ title: '保管',field: 'storage',minWidth: 70}, 
				{ title: '销售业务类型',field: 'saleBussinessType',minWidth: 70}, 
				{ title: '产品代码',field: 'productCode',minWidth: 70}, 
				{ title: '产品名称',field: 'productName',minWidth: 70}, 
				{ title: '单位',field: 'unit',minWidth: 70}, 
				{ title: '数量',field: 'amount',minWidth: 70}, 
				{ title: '单位成本',field: 'unitCost',minWidth: 70}, 
				{ title: '成本',field: 'cost',minWidth: 70}, 
				{ title: '备注',field: 'remark',minWidth: 70}, 
				{ title: '发货仓位',field: 'shipWarehouse',minWidth: 70}, 
				{ title: '仓位',field: 'warehouse',minWidth: 70}, 
				{ title: '销售单价',field: 'unitPrice',minWidth: 70}, 
				{ title: '销售金额',field: 'price',minWidth: 70}, 
				{ title: '导入类型 1：京东 2：天猫 3：淘宝 4：拼多多',field: 'type',minWidth: 70}, 
				{ title: '创建时间',field: 'createDate',minWidth: 70}, 
				{ title: '修改时间',field: 'updateDate',minWidth: 70}
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



})


var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q:{},
		shipmentb: {},
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.shipmentb = {};
		},
		update: function (event) {

			var objs = layui.table.checkStatus('listTable');
			if(objs.data.length==0 || objs.data.length>1){
				layer.msg('请选择一条记录进行修改');
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.getInfo(objs.data[0].shipmentbId);

		},
        saveOrUpdate: function (flag) {
			var url = vm.shipmentb.id == null ? "shipmentb/save" : "shipmentb/update";
			var type = vm.shipmentb.id == null ? "post":"put";

			$.ajax({
				type: type,
				url: ctx + url,
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(vm.shipmentb),
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
				ids.push(objs.data[key].shipmentbId)
			}
			layer.confirm('确定删除？', function(){
				$.ajax({
					type: "DELETE",
				    url: ctx + "shipmentb/delete",
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
			$.get(ctx + "shipmentb/info/"+id, function(r){
                vm.shipmentb = r.shipmentb;
            });
		},

		closePanel: function (event) {
			vm.showList = true;
			vm.shipmentb= {};

		},
		
		reload: function () {
			vm.showList = true;
			layui.table.reload("listTable",{
				page: {
					curr: 1
				},
				where: {
//					name: vm.q,

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