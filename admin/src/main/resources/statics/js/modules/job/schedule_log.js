var ctx = sessionStorage.getItem('_ctx');
var layer,localLayer,$;
layui.use(['form','layer','laydate','element','table',],function(){
	layer = top.layer;//在最外层弹出
	localLayer = layui.layer;//在内容区弹出
	$ = layui.jquery;//引入jquery
	var form = layui.form,//引入form
		laydate = layui.laydate,//引入日期时间控件
		element = layui.element,
		table = layui.table;
//列表
	var tableIns = table.render({
		elem: '#listData',//表格元素的id
		method:'POST',
		url : ctx+'sys/scheduleLog/list',
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
			{ title: '日志ID', field: 'logId',hide: true },
			{ title: '任务ID', field: 'jobId', minWidth: 50 },
			{ title: 'bean名称', field: 'beanName', minWidth: 70 },
			{ title: '方法名称', field: 'methodName', minWidth: 70 },
			{ title: '参数', field: 'params', minWidth: 150 },
			{ title: '状态', field: 'status',templet: function(d){
					return d.status === 0 ?
						'<button type="button" class="layui-btn layui-btn-xs  ">成功</button>' :
						'<button type="button" class="layui-btn layui-btn-xs  layui-btn-danger" onclick="vm.showError('+d.logId+')">失败</button>';
			}},
			{ title: '耗时(单位：毫秒)', field: 'times', minWidth: 150 },
			{ title: '执行时间', field: 'createTime', minWidth: 150 }


		]]
	});


	form.on('submit(*)', function(data){
		vm.saveOrUpdate()
		return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});



})


/*


$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'sys/scheduleLog/list',
        datatype: "json",
        colModel: [			
            { label: '日志ID', name: 'logId', width: 50, key: true },
			{ label: '任务ID', name: 'jobId', width: 50},
			{ label: 'bean名称', name: 'beanName', width: 60 },
			{ label: '方法名称', name: 'methodName', width: 60 },
			{ label: '参数', name: 'params', width: 60 },
			{ label: '状态', name: 'status', width: 50, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-success">成功</span>' :
					'<span class="label label-danger pointer" onclick="vm.showError('+row.logId+')">失败</span>';
			}},
			{ label: '耗时(单位：毫秒)', name: 'times', width: 70 },
			{ label: '执行时间', name: 'createTime', width: 80 }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50,100,200],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});


*/





var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			jobId: null
		}
	},
	methods: {
		query: function () {

			layui.table.reload("listTable",{
				page: {
					curr: 1 //重新从第 1 页开始
				},
				where: {
					jobId: vm.q.jobId,

				}
			})



		},
		showError: function(logId) {
			$.get(ctx + "sys/scheduleLog/info/"+logId, function(r){
				parent.layer.open({
				  title:'失败信息',
				  closeBtn:0,
				  content: r.log.error
				});
			});
		},
		back: function (event) {
			history.go(-1);
		}
	}
});

