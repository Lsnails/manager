
var $,tab,layer;
var cacheStr = true;
layui.config({
    base : ctx+"/statics/libs/"
}).extend({
    "layoutTabs" : "layoutTabs"
})


//isquery=true;
var vm = new Vue({
    el: '#layui_layout',
    data: {
        user: {},
        menuList: {},
        password: '',
        newPassword: '',
        navTitle: "首页"
    },
    methods: {

        getUser: function () {
            $.getJSON("sys/user/info?_" + $.now(), function (r) {
                vm.user = r.user;
                sessionStorage.setItem('x-user',JSON.stringify(vm.user));
            });
        },
        updatePassword: function () {
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: "修改密码",
                area: ['550px', '270px'],
                shadeClose: false,
                content: jQuery("#passwordLayer"),
                btn: ['修改', '取消'],
                btn1: function (index) {
                    var data = "password=" + vm.password + "&newPassword=" + vm.newPassword;
                    $.ajax({
                        type: "POST",
                        url: "sys/user/password",
                        data: data,
                        dataType: "json",
                        success: function (result) {
                            if (result.code == 0) {
                                layer.close(index);
                                layer.alert('修改成功', function (index) {
                                    location.reload();
                                });
                            } else {
                                layer.alert(result.msg);
                            }
                        }
                    });
                }
            });
        },
        logout:function () {
            window.sessionStorage.removeItem("menu");
            window.sessionStorage.removeItem("curmenu");
            $.ajax({
                type: "GET",
                url: "logout",
                success: function (result) {
                }
            });
        }

    },
    created: function () {
        this.getUser();
    },updated:function(){



        layui.use(['layoutTabs','form','element','layer','jquery'],function(){
            var form = layui.form,
                element = layui.element;
            $ = layui.$;
            layer = parent.layer === undefined ? layui.layer : top.layer;

            tab = layui.layoutTabs({
                maxTabNum : "20",  //最大可打开窗口数量
                url : "sys/menu/nav" //获取菜单json地址
            });

            function getData(){
                $.get(tab.tabConfig.url,function(res){
                	debugger
                    if(res.code == 0){
                        //重新渲染左侧菜单
                        tab.render(res.menuList);
                    }else{
                        layer.msg("无法获得数据");
                    }


                })
            }
            getData();


            // 添加新窗口
            $("body").on("click",".layui-nav .layui-nav-item a",function(){
                //如果不存在子级
                if($(this).siblings().length == 0){
                    tab.tabAdd($(this));
                }
                $(this).parent("li").siblings().removeClass("layui-nav-itemed");
            })



            //刷新后还原打开的窗口
            if(cacheStr) {
            	debugger;
                if (window.sessionStorage.getItem("menu") != null) {
                    menu = JSON.parse(window.sessionStorage.getItem("menu"));
                    curmenu = window.sessionStorage.getItem("curmenu");
                    var openTitle = '';
                    for (var i = 0; i < menu.length; i++) {
                        openTitle = '';
                        if (menu[i].icon) {
                        	debugger
                            if (menu[i].icon.split("-")[0] == 'icon') {
                                openTitle += '<i class="seraph ' + menu[i].icon + '"></i>';
                            } else {
                                openTitle += '<i class="layui-icon">' + menu[i].icon + '</i>';
                            }
                        }
                        openTitle += '<cite>' + menu[i].title + '</cite>';
                        openTitle += '<i class="layui-icon layui-unselect layui-tab-close" data-id="' + menu[i].layId + '">&#x1006;</i>';
                        element.tabAdd("main-tab", {
                            title: openTitle,
                            content: "<iframe src='" + menu[i].href + "' data-id='" + menu[i].layId + "'></frame>",
                            id: menu[i].layId
                        })
                        //定位到刷新前的窗口
                        if (curmenu != "undefined") {
                            if (curmenu == '' || curmenu == "null") {  //定位到后台首页
                                element.tabChange("main-tab", '');
                            } else if (JSON.parse(curmenu).title == menu[i].title) {  //定位到刷新前的页面
                                element.tabChange("main-tab", menu[i].layId);
                            }
                        } else {
                            element.tabChange("main-tab", menu[menu.length - 1].layId);
                        }
                    }
                }
            }else{
                window.sessionStorage.removeItem("menu");
                window.sessionStorage.removeItem("curmenu");
            }


        })

        $('.larry-side-menu').click(function () {
            var sideWidth = $('#larry-side').width();
            if (sideWidth === 200) {
                $('#larry-body').animate({
                    left: '0'
                });
                $('#larry-footer').animate({
                    left: '0'
                });
                $('#larry-side').animate({
                    width: '0'
                });
                $('.layui-icon-shrink-right').addClass('layui-hide')
                $('.layui-icon-spread-left').removeClass('layui-hide')
            } else {
                $('#larry-body').animate({
                    left: '200px'
                });
                $('#larry-footer').animate({
                    left: '200px'
                });
                $('#larry-side').animate({
                    width: '200px'
                });
                $('.layui-icon-shrink-right').removeClass('layui-hide')
                $('.layui-icon-spread-left').addClass('layui-hide')
            }
        });
    }
});