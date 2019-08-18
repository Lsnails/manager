layui.config({
    base : ctx+"/statics/libs/"
}).extend({
    "layTreeTable" : "layTreeTable"
})
var	re;
layui.use(['layTreeTable','form','element'],function(){
    var form = layui.form,
        treeTable = layui.layTreeTable;
    var user = JSON.parse(sessionStorage.getItem('x-user'));
    re = treeTable.render({
        elem: '#tree-table',
        url:ctx + "sys/dept/list",
        top_value: user.userId==1?'0':user.deptId,
        icon_key: 'deptId',
        primary_key: 'deptId',
        parent_key: 'parentId',
        is_checkbox: true,
        cols: [

            {title: '部门ID',key: 'deptId',minWidth: '100px',align: 'center'},
            {title: '部门名称',key: 'name',minWidth: '100px',align: 'center'},
            {title: '上级部门',key: 'parentName',minWidth: '100px',align: 'center'},
            {title: '排序号',key: 'orderNum',minWidth: '100px',align: 'center'}


        ]

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
        showList: true,
        title: null,
        dept:{
            parentName:null,
            parentId:0,
            orderNum:0
        }
    },
    methods: {
        getDept: function(){
            //加载部门树
            $.get(ctx + "sys/dept/select", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r.deptList);
                var node = ztree.getNodeByParam("deptId", vm.dept.parentId);
                ztree.selectNode(node);
                vm.dept.parentName = node.name;
            })
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.dept = {parentName:'',parentId:"",orderNum:0};
            vm.getDept();
        },
        update: function () {
            var ids = layui.layTreeTable.checked(re);
            if(ids.length ==0 || ids.length>1){
                layer.msg('请选择一条记录进行修改');
                return ;
            }

            $.get(ctx + "sys/dept/info/"+ids[0], function(r){
                vm.showList = false;
                vm.title = "修改";
                vm.dept = r.dept;
                vm.getDept();
            });
        },
        del: function () {
            var ids = layui.layTreeTable.checked(re);
            if(ids.length ==0 || ids.length>1){
                layer.msg('请选择一条记录进行刪除');
                return ;
            }

            layer.confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: ctx + "sys/dept/delete",
                    data: "deptId=" + ids[0],
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
        saveOrUpdate: function (event) {

            var url = vm.dept.deptId == null ? "sys/dept/save" : "sys/dept/update";
            $.ajax({
                type: "POST",
                url: ctx + url,
                contentType: "application/json",
                data: JSON.stringify(vm.dept),
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
        deptTree: function(){
            localLayer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级部门
                    vm.dept.parentId = node[0].deptId;
                    vm.dept.parentName = node[0].name;

                    localLayer.close(index);
                }
            });
        },
        reload: function () {
            vm.showList = true;
            layui.layTreeTable.render(re);
        }
    }
});
