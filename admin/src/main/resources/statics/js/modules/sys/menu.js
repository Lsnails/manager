layui.config({
    base : ctx+"/statics/libs/"
}).extend({
    "layTreeTable" : "layTreeTable"
})
var	re;
layui.use(['layTreeTable','form','element'],function(){
    var form = layui.form,
        treeTable = layui.layTreeTable;
    	re = treeTable.render({
        elem: '#tree-table',
        url:ctx + "sys/menu/list",
        icon_key: 'menuId',
        primary_key: 'menuId',
        parent_key: 'parentId',
        is_checkbox: true,
        cols: [
            {title: '菜单ID',key: 'menuId',minWidth: '100px',align: 'center'},
            {title: '菜单名称',key: 'name',minWidth: '100px',align: 'center'},
            {title: '上级菜单',key: 'parentName',minWidth: '100px',align: 'center',template: function(item){
                    return item.parentName == null ?'无':item.parentName;
             }},
            {title: '图标',key: 'icon',minWidth: '100px',align: 'center',template: function(item){
                    return item.icon == null ? '' : '<i class="'+item.icon+' fa-lg"></i>';
            }},
            {title: '类型',key: 'type',minWidth: '100px',align: 'center',template: function(item){
                    if(item.type == 0){
                        return '<span class="x-text-normal">目录</span>';
                    }
                    if(item.type == 1){
                        return '<span class="x-text-green">菜单</span>';
                    }
                    if(item.type == 2){
                        return '<span class="x-text-orange">按钮</span>';
                    }
            }},
            {title: '排序号',key: 'orderNum',minWidth: '100px',align: 'center'},
            {title: '菜单URL',key: 'url',minWidth: '100px',align: 'center',template: function(item){
                    return item.url == null ?'无':item.url;
                }},
            {title: '授权标识',key: 'perms',minWidth: '100px',align: 'center',template: function(item){
                    return item.perms == null ?'无':item.perms;
                }},
            {title: '菜单类型',key: 'kind',minWidth: '100px',align: 'center',template: function(item){
                    if(item.kind == 0){
                        return '后台管理';
                    }
                    if(item.kind == 1){
                        return '网站';
                    }
            }},
            {title: '语言',key: 'language',minWidth: '100px',align: 'center',template: function(item){
                    if(item.language == 1){
                        return '中文';
                    }else if(item.language == 2){
                        return '英文';
                    }else{
                        return '';
                    }

                }}
        ]

    },{});

    form.on('select(searchKind)', function(data){
        vm.q.kind = data.value;

        if(data.value == 1){
            vm.q.language = '1';
        }else{
            vm.q.language = '';
        }

    });
    form.on('select(searchLan)', function(data){
        vm.q.language = data.value;
    });

    form.on('radio(mType)', function(data){
        vm.menu.type = data.value;
        vm.menu.parentName = '一级菜单';
        vm.menu.parentId = 0;

    });
    form.on('radio(mKind)', function(data){
        vm.menu.kind = data.value;
        if(data.value == 0){
            vm.menu.language = '';
        }
        vm.menu.parentName = '一级菜单';
        vm.menu.parentId = 0;

    });
    form.on('radio(mLanguage)', function(data){
        vm.menu.language = data.value;
        vm.menu.parentName = '一级菜单';
        vm.menu.parentId = 0;
    });

    form.on('submit(*)', function(data){
        vm.saveOrUpdate()
        return false;
    });

})



var setting = {
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
    }
};
var ztree;
var vm = new Vue({
    el:'#rrapp',
    data:{
        showList: true,
        title: null,
        q:{name:'',kind:'',language:''},
        menu:{
            parentName:null,
            parentId:0,
            type:1,
            orderNum:0,
            kind:0,
            language:''
        }
    },
    methods: {
        query:function(){
            vm.reload();
        },
        getMenu: function(menuId){

            //加载菜单树
            $.get(ctx + "sys/menu/select?kind="+vm.menu.kind+'&language='+vm.menu.language, function(r){
                ztree = $.fn.zTree.init($("#menuTree"), setting, r.menuList);
                var node = ztree.getNodeByParam("menuId", vm.menu.parentId);
                ztree.selectNode(node);
                vm.menu.parentName = node.name;
            })
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.menu = {parentName:null,parentId:0,type:1,orderNum:0,kind:0,language:''};
            //vm.getMenu();
        },
        update: function () {
            var ids = layui.layTreeTable.checked(re);
            if(ids.length ==0 || ids.length>1){
                layer.msg('请选择一条记录进行修改');
                return ;
            }

            $.get(ctx + "sys/menu/info/"+ids[0], function(r){
                vm.showList = false;
                vm.title = "修改";
                vm.menu = r.menu;
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
                    url: ctx + "sys/menu/delete",
                    data: "menuId=" + ids[0],
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
            var url = vm.menu.menuId == null ? "sys/menu/save" : "sys/menu/update";
            $.ajax({
                type: "POST",
                url:  ctx + url,
                contentType: "application/json",
                data: JSON.stringify(vm.menu),
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
        menuTree: function(){
            if(vm.menu.kind==1 && (vm.menu.language!=1 && vm.menu.language!=2)){
                layer.msg("请选择菜单语言")
                return;
            }


            localLayer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择菜单",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#menuLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级菜单
                    vm.menu.parentId = node[0].menuId;
                    vm.menu.parentName = node[0].name;

                    localLayer.close(index);
                },
                success:function(){
                    vm.getMenu()
                }
            });
        },
        reload: function () {
            vm.showList = true;
            layui.layTreeTable.render(re,{name:vm.q.name,kind:vm.q.kind,language:vm.q.language});
        }
    },
    updated:function(){
        layui.form.render('radio','myForm');

        if(vm.q.kind == 1){
            layui.form.render('select','searchForm');
        }

        setTimeout(function(){
            layui.form.val('myForm',{
                'language':vm.menu.language
            })
        },100)


    }
});

