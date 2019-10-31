layui.use(['form','laydate','element','table',],function(){
    var form = layui.form,//引入form
        laydate = layui.laydate,//引入日期时间控件
        element = layui.element,
        table = layui.table;
//列表
    var tableIns = table.render({
        elem: '#listData',//表格元素的id
        method:'POST',
        url : ctx+'sys/user/list',
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
        toolbar: '#toptoolbar',
        defaultToolbar: ['filter'],//开启设置动态表头功能
        cols : [[
            { type: "checkbox", fixed:"left", width:50},
            { title: '用户ID', field: 'userId', minWidth: 70},
            { title: '用户名', field: 'username', minWidth: 50 },
            { title: '所属网点Id', field: 'deptId', minWidth: 70,hide:true },
            { title: '所属网点', field: 'deptName', minWidth: 70 },
            { title: '邮箱', field: 'email', minWidth: 150 },
            { title: '手机号', field: 'mobile', minWidth: 150 },
            { title: '状态', field: 'status', minWidth: 150,templet:function(d){
                    return d.status === 0 ?
                        '<label  class="x-text-red">禁用</label>' :
                        '<label  class="x-text-green">正常</label>';
             }},
            { title: '创建时间', field: 'createTime', minWidth: 80 },
            {title: '操作',fixed:"right",width:120, align:'center',  templet:'#operationBar'}

        ]]
    });


    table.on('tool(listData)', function(obj){
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event;
        if(layEvent == 'resetPassword'){
            layer.confirm('确定要重置密码？', function(index){
                $.get(ctx+"sys/user/resetPwd/"+data.userId,function(r){
                    if(r.code==0){
                        layer.msg('重置成功')
                    }

                })

            })

        }
    })

    form.on('checkbox()', function(data){
    	 if(data.elem.checked){
             vm.user.roleIdList.push(data.value)
         }else{
             vm.user.roleIdList.indexOf(parseInt(data.value))!=-1?vm.user.roleIdList.splice(vm.user.roleIdList.indexOf(data.value),1):null;
         }
    });
    form.on('radio()', function(data){
        vm.user.status = data.value;
    });


    form.on('submit(*)', function(data){
        vm.saveOrUpdate()
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });



})



var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};
var ztree;

var vm = new Vue({
    el:'#rrapp',
    data:{
    	q:{
            username: ''
        },
        showList: true,
        deptFlag:false,
        deptTip:'请先选择网点',
        title:null,
        roleList:{},
        user:{
            status:1,
            deptId:null,
            deptName:null,
            roleIdList:[]
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.roleList = {};
            vm.user = {deptName:null, deptId:null, status:1, roleIdList:[]};
            vm.getDept();
        },
        getDept: function(){
            //加载网点树
            $.get(ctx + "sys/dept/list", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
                var node = ztree.getNodeByParam("deptId", vm.user.deptId);
                if(node != null){
                    ztree.selectNode(node);
                    vm.user.deptName = node.name;
                }
            })
        },
        update: function () {
            var objs = layui.table.checkStatus('listTable');
            if(objs.data.length==0 || objs.data.length>1){
                layer.msg('请选择一条记录进行修改');
                return ;
            }

            vm.showList = false;
            vm.title = "修改";
            vm.getUser(objs.data[0].userId);
            //获取角色信息
            if(objs.data[0].deptId!=null){
                this.getRoleList(objs.data[0].deptId);
                setTimeout(function(){
                    vm.$nextTick(function () {
                        if(vm.roleList.length>0){
                            vm.deptFlag = true;
                        }
                    })
                },100)


            }

        },
        del: function () {
            var objs = layui.table.checkStatus('listTable');
            if(objs.data.length==0){
                layer.msg('请选择要删除的内容');
                return ;
            }
            var ids = [];
            for(var key in objs.data){
                ids.push(objs.data[key].userId)
            }

            layer.confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: ctx + "sys/user/delete",
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
        saveOrUpdate: function () {
            var url = vm.user.userId == null ? "sys/user/save" : "sys/user/update";
            $.ajax({
                type: "POST",
                url: ctx + url,
                contentType: "application/json",
                data: JSON.stringify(vm.user),
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
        getUser: function(userId){
            $.get(ctx + "sys/user/info/"+userId, function(r){
                vm.user = r.user;
                vm.user.password = null;
                vm.getDept();
               
            });

        },
        getRoleList: function(deptId){
            $.get(ctx + "sys/role/getSysRoleList/"+deptId, function(r){
                vm.roleList = r.data;
            });

        },
        deptTree: function(){
            localLayer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择网点",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: $("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级网点
                    vm.user.deptId = node[0].deptId;
                    vm.user.deptName = node[0].name;
                    //获取角色信息
                    vm.getRoleList(node[0].deptId);
                    setTimeout(function(){
                        vm.$nextTick(function () {
                            if(vm.roleList.length>0){
                                vm.deptFlag = true;
                            }else{
                                vm.deptTip = '当前网点下没有角色'
                            }
                        })
                    },100)
                    localLayer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            vm.deptFlag = false;
            vm.roleList = {}
            $('#roleBlock').empty();
            layui.table.reload("listTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    username: vm.q.username,

                }
            })

        }
    },
    updated:function(){

        layui.form.render('checkbox','myForm');
        layui.form.render('radio','myForm');
    }

});