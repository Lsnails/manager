
layui.use(['form','laydate','element','table','upload'],function(){
	var form = layui.form,
		laydate = layui.laydate,
		element = layui.element,
		table = layui.table;
		upload = layui.upload;

	var tableIns = table.render({
		elem: '#listData',
		method:'POST',
		url : ctx+'buyinfo/list',
		page: {
			layout: ['count', 'prev', 'page', 'next','limit','skip'] //自定义分页布局
			,first: false
			,last: false
			,groups:5

		},
		height : 'full-150',
		loading:true,
		limit : 20,
		limits : [10,15,20,25,50,100],
		id : "listTable",
		toolbar: '#toptoolbar',
		defaultToolbar: ['filter'],
		cols : [[
			{ type: "checkbox", fixed:"left", width:50},
			{ title: 'id',field: 'id',hide:true },
				{ title: '姓名',field: 'name',minWidth: 100}, 
				{ title: '电话号码',field: 'phone',minWidth: 125}, 
				{ title: '产品型号',field: 'proType',minWidth: 140}, 
				{ title: '购买时间',field: 'buyTime',minWidth: 180}, 
				{ title: '购买价格',field: 'buyPrice',minWidth: 100}, 
				{ title: '购买数量',field: 'buyNumber',minWidth: 100}, 
				{ title: '购买渠道',field: 'buyChannel',minWidth: 100}, 
				{ title: '收货地址（购买地址）',field: 'buyAddress',minWidth: 100}, 
				{ title: '省市缩写',field: 'address',minWidth: 140}, 
				{ title: '备注',field: 'remark',minWidth: 70}, 
				{ title: '星标',field: 'star',minWidth: 70}, 
				{ title: '回访记录',field: 'returnInfo',minWidth: 100}, 
				{ title: '创建时间',field: 'createDate',minWidth: 180}, 
				{ title: '修改时间',field: 'updateDate',minWidth: 180}
				//{ title:"操作",fixed:'right',toolbar: "#operationBar",width:100}
		]]
	});

	form.on('checkbox()', function(data){

	});
	form.on('radio()', function(data){

	});
	form.on('select()', function(data){

	});
	
	upload.render({ //允许上传的文件后缀
	    elem: '#shipmentaUpload',
	    url: ctx + 'buyinfo/uploadFile',
	    accept: 'file', //普通文件
	    before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
	    	 layer.msg('上传中', {
        	   icon: 16
        	   ,shade: 0.01
        	  });
	    },
	    exts: 'xlsx|xls|cvs|csv', //只允许上传压缩文件
	    done: function(res){
	    	layer.msg("上传成功");
	    	console.log(res)
	    	vm.reload();
	    },
	    error: function(index, upload){
	    	layer.msg('服务器出错。联系管理员', {icon: 5});
	    }
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


	laydate.render({
	    elem: '#searchDate',
	    done: function(value, date){
	    	console.log(date);
	    	console.log(value);
	    	vm.q.buyTime = value;
	    }
	});
	
	form.on('select(searchStar)', function(data){
        vm.q.star=data.value;
        console.log(vm.q.star);
    });

})


var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q:{buyTime: '',name: '',buyPriceMin: '',buyPriceMax: '',star: '',proType: ''},
		buyInfo: {},
	},
	methods: {
		query: function () {
			vm.reload();
		},
		resetSearch:function(){
			this.q.buyTime = ''
			this.q.name = ''
			this.q.buyPriceMin = ''
			this.q.buyPriceMax = ''
			this.q.star = ''
			this.q.proType = ''
			layui.form.val("searchForm", { //formTest 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
				"star":""
			});
			this.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.buyInfo = {};
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
		showExpression:function(){
			layui.layer.open({
	            title :"出库导入",
	            type : 2,
	            area: ['60%', '60%'],
	            shade:0,//不要遮罩
	            content :ctx+"modules/datainfo/importPage.html",
	            yes: function(index, layero){
	                localLayer.close(index)
	            }

	        })
		},
        saveOrUpdate: function (flag) {
			var url = vm.buyInfo.id == null ? "buyinfo/save" : "buyinfo/update";
			var type = vm.buyInfo.id == null ? "post":"put";

			$.ajax({
				type: type,
				url: ctx + url,
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify(vm.buyInfo),
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
				    url: ctx + "buyinfo/delete",
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
			$.get(ctx + "buyinfo/info/"+id, function(r){
                vm.buyInfo = r.buyInfo;
            });
		},

		closePanel: function (event) {
			vm.showList = true;
			vm.buyInfo= {};

		},
		
		reload: function () {
			vm.showList = true;
			layui.table.reload("listTable",{
				page: {
					curr: 1
				}, 
				where: {
					buyTime: vm.q.buyTime,
					name: vm.q.name,
					buyPriceMin: vm.q.buyPriceMin,
					buyPriceMax: vm.q.buyPriceMax,
					star: vm.q.star,
					proType: vm.q.proType
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