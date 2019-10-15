layui.use(['form','laydate','element','table',],function(){
    var form = layui.form,//引入form
        laydate = layui.laydate,//引入日期时间控件
        element = layui.element,
        table = layui.table;
//列表
    var tableIns = table.render({
        elem: '#listData',//表格元素的id
        method:'POST',
        url : ctx+'sys/role/list',
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
            { title: '角色ID', field: 'roleId', minWidth: 70},
            { title: '角色名称', field: 'roleName', minWidth: 50 },
            { title: '所属网点', field: 'deptName', minWidth: 70 },
            { title: '备注', field: 'remark' },
            { title: '创建时间', field: 'createTime'}

        ]]
    });



    form.on('submit(*)', function(data){
        vm.saveOrUpdate()
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });



})



//菜单树
var menu_ztree;
var menu_setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "menuId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    },
    check:{
        enable:true,
        nocheckInherit:true
    }
};

//网点结构树
var dept_ztree;
var dept_setting = {
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

//数据树
var data_ztree;
var data_setting = {
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
    },
    check:{
        enable:true,
        nocheckInherit:true,
        chkboxType:{ "Y" : "", "N" : "" }
    }
};

var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            roleName: null
        },
        showList: true,
        title:null,
        role:{
            deptId:null,
            deptName:null
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.role = {deptName:null, deptId:null};
            vm.getMenuTree(null);
            vm.getDept();
            //vm.getDataTree();

        },
        update: function () {

            var objs = layui.table.checkStatus('listTable');
            if(objs.data.length==0 || objs.data.length>1){
                layer.msg('请选择一条记录进行修改');
                return ;
            }

            vm.showList = false;
            vm.title = "修改";
            //vm.getDataTree();
            vm.getMenuTree(objs.data[0].roleId);
            vm.getDept();

        },
        del: function () {
            var objs = layui.table.checkStatus('listTable');
            if(objs.data.length==0){
                layer.msg('请选择要删除的内容');
                return ;
            }
            var ids = [];
            for(var key in objs.data){
                ids.push(objs.data[key].roleId)
            }

            layer.confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: ctx + "sys/role/delete",
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
        getRole: function(roleId){
            $.get(ctx + "sys/role/info/"+roleId, function(r){
                vm.role = r.role;

                //勾选角色所拥有的菜单
                var menuIds = vm.role.menuIdList;
                for(var i=0; i<menuIds.length; i++) {
                    var node = menu_ztree.getNodeByParam("menuId", menuIds[i]);
                    menu_ztree.checkNode(node, true, false);
                }

               /* //勾选角色所拥有的网点数据权限
                var deptIds = vm.role.deptIdList;
                for(var i=0; i<deptIds.length; i++) {
                    var node = data_ztree.getNodeByParam("deptId", deptIds[i]);
                    data_ztree.checkNode(node, true, false);
                }*/

                vm.getDept();
            });
        },
        saveOrUpdate: function () {
            //获取选择的菜单
            var nodes = menu_ztree.getCheckedNodes(true);
            var menuIdList = new Array();
            for(var i=0; i<nodes.length; i++) {
                menuIdList.push(nodes[i].menuId);
            }
            vm.role.menuIdList = menuIdList;

            /*//获取选择的数据

            var nodes = data_ztree.getCheckedNodes(true);
            var deptIdList = new Array();
            for(var i=0; i<nodes.length; i++) {
                deptIdList.push(nodes[i].deptId);
            }
            vm.role.deptIdList = deptIdList;*/

            var url = vm.role.roleId == null ? "sys/role/save" : "sys/role/update";
            $.ajax({
                type: "POST",
                url: ctx + url,
                contentType: "application/json",
                data: JSON.stringify(vm.role),
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
        getMenuTree: function(roleId) {
            //加载菜单树
            //alert("授权树");
            $.get(ctx + "sys/menu/getUserMenuList", function(r){
                menu_ztree = $.fn.zTree.init($("#menuTree"), menu_setting, r);
                //展开所有节点
                var expandFlag = menu_ztree.expandAll(true);
                if(roleId != null){
                    vm.getRole(roleId);
                }
            });
        },
        getDataTree: function(roleId) {
            //加载菜单树
            $.get(ctx + "sys/dept/list", function(r){
                data_ztree = $.fn.zTree.init($("#dataTree"), data_setting, r);
                //展开所有节点
                data_ztree.expandAll(true);
            });

        },
        getDept: function(){
            //加载网点树
            $.get(ctx + "sys/dept/list", function(r){
                dept_ztree = $.fn.zTree.init($("#deptTree"), dept_setting, r);
                var node = dept_ztree.getNodeByParam("deptId", vm.role.deptId);
                if(node != null){
                    dept_ztree.selectNode(node);
                    vm.role.deptName = node.name;
                }
            })
        },
        deptTree: function(){
            localLayer.open({
                type: 1,
                offset: '30px',
                skin: 'layui-layer-molv',
                title: "选择网点",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: $("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = dept_ztree.getSelectedNodes();
                    //选择上级网点
                    vm.role.deptId = node[0].deptId;
                    vm.role.deptName = node[0].name;

                    localLayer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;

            layui.table.reload("listTable",{
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                where: {
                    roleName: vm.q.roleName,

                }
            })

        }
    }
});