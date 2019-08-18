var ctx = sessionStorage.getItem('_ctx');
layui.use(['form','layer','laydate','element','table',],function(){
    var form = layui.form,//引入form
        laydate = layui.laydate,//引入日期时间控件
        element = layui.element,
        table = layui.table;
//列表
    var tableIns = table.render({
        elem: '#listData',//表格元素的id
        method:'POST',
        url : ctx+'sys/log/list',
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
            { title: 'id', field: 'id',hide: true },
            { title: '用户名', field: 'username', minWidth: 50 },
            { title: '用户操作', field: 'operation', minWidth: 70 },
            { title: '请求方法', field: 'method', minWidth: 150 },
            { title: '请求参数', field: 'params', minWidth: 80 },
            { title: '执行时长(毫秒)', field: 'time', minWidth: 80 },
            { title: 'IP地址', field: 'ip', minWidth: 70 },
            { title: '创建时间', field: 'createDate',sort:true, minWidth: 90 }

        ]]
    });




})



var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			key: null
		},
	},
	methods: {
		query: function () {
			vm.reload();
		},
		reload: function (event) {
            layui.table.reload("listTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    key: vm.q.key,

                }
            })
		}
	}
});