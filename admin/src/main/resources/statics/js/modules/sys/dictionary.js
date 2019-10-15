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
		url:ctx + "cms/dictionary/list",
		top_value: null,
		icon_key: 'name',
		primary_key: 'dictId',
		parent_key: 'parentId',
		is_checkbox: true,
		cols: [
			{title: '字典code',key: 'code',minWidth: '100px',align: 'center'},
			{title: '字典名称',key: 'name',minWidth: '100px',align: 'center'},
			{title: '字典Id',key: 'dictId',width: '0px',align: 'center',hide:true},
			{title: '上级分类',key: 'parentName',minWidth: '100px',align: 'center',template: function(item){
					return item.parentName ? item.parentName : '';
				}},
			{title: 'URL',key: 'url',minWidth: '100px',align: 'center',template: function(item){
					return item.url ? item.url : '';
				}},
			{title: '排序号',key: 'orderNum',minWidth: '100px',align: 'center',template: function(item){
					return item.orderNum ? item.orderNum : '';
				}},
			{title: '描述',key: 'description',minWidth: '100px',align: 'center',template: function(item){
				return item.description ? item.description : '';
			}},
			{title: '语言',key: 'language',minWidth: '100px',align: 'center',template:function(item){
					if(item.language == 1){
						return'中文';
					}else if(item.language == 2){
						return'英文';
					}
			}}

		]

	},{language:vm.language});

	form.on('select(searchLan)', function(data){
		vm.language = data.value;
	});
	form.on('select(languageSelect)', function(data){
		vm.language = data.value;
		vm.dictionary.language = data.value;
		vm.dictionary.parentId = null;
		vm.dictionary.parentName = '一级分类';
	});

	form.val("myForm", {
		"language": vm.dictionary.language
	})

	form.on('submit(*)', function(data){
		vm.saveOrUpdate()
		return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
	});

})


var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "dictId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    },
    view: {
		dblClickExpand: false
	},
	callback: {
		onDblClick: function(event, treeId, treeNode){
                if(treeNode.isParent){
                	layer.msg('请选择最末级分类');
					return;
                }else{
                	vm.dictionary.parentId = treeNode.dictId;
                    vm.dictionary.parentName = treeNode.name;
                    layer.close(layer.index);
                }
			}
	}
};
var ztree;


var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dictName:"",
		language:'1',
		dictionary:{
			parentName:null,
            parentId:null,
            type:1,
            orderNum:0,
			language:1
		}
	},
	methods: {
		getDictionaryTree: function(menuId){
			var language = vm.dictionary.language;
            //加载菜单树
            $.get(ctx + "cms/dictionary/allselect?language="+language, function(r){
                ztree = $.fn.zTree.init($("#dictTree"), setting, r.dictionaryList);
            })
        },
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dictionary = {parentName:"一级分类",parentId:null,type:1,orderNum:0,language:1};
		},
		update: function (event) {
			var ids = layui.layTreeTable.checked(re);
			if(ids.length ==0 || ids.length>1){
				layer.msg('请选择一条记录进行修改');
				return ;
			}

			$.get(ctx + "cms/dictionary/info/"+ids[0], function(r){
				vm.showList = false;
	            vm.title = "修改";
	            vm.dictionary = r.dictionary;
	            vm.getDictionaryTree();
	            
            });
		},
		saveOrUpdate: function (event) {
		    	var url = vm.dictionary.dictId == null ? "cms/dictionary/save" : "cms/dictionary/update";
				var type = vm.dictionary.dictId == null ? "post":"put";
				$.ajax({
					type: type,
					url: ctx + url,
					contentType: "application/json",
					data: JSON.stringify(vm.dictionary),
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
		dictionaryTree: function(){
			vm.getDictionaryTree();
            localLayer.open({
                type: 1,
                offset: '10px',
                skin: 'layui-layer-molv',
                title: "选择分类",
                area: ['300px', '450px'],
                shade: 0,
                shadeClose: false,
                content: $("#dictLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级菜单
                    vm.dictionary.parentId = node[0].dictId;
                    vm.dictionary.parentName = node[0].name;
					localLayer.close(index);
                }
            });
        },

		del: function (event) {


			var ids = layui.layTreeTable.checked(re);
			if(ids.length ==0 || ids.length>1){
				layer.msg('请选择一条记录进行刪除');
				return ;
			}
			console.log(ids);

			$.ajax({
				type: "GET",
				url: ctx + "cms/dictionary/checkIsLeafnode/"+ids[0],
				contentType: "application/json",
				success: function(r){
					if(r.code == 0){
						layer.confirm("确定删除？", function(){
							$.ajax({
								type: "DELETE",
								url: ctx + "cms/dictionary/delete",
								contentType: "application/json",
								data:  JSON.stringify({dictIds:ids,moduleIds:null}),
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
					}else if(r.code == 601 ){
						layer.msg("请从最末级开始删除");
					}
				}
			});



			
		},
		reload: function (event) {
			vm.showList = true;
			layui.layTreeTable.render(re,{name:vm.dictName,language:vm.language});

		}
	},
	updated:function(){
		layui.form.render('select','myForm');
	}
});