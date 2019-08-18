var tabFilter,menu=[],liIndex,curNav,delMenu,
    changeRefreshStr = window.sessionStorage.getItem("changeRefresh");
layui.define(["element","jquery"],function(exports){
	var element = layui.element,
		$ = layui.$,
		layId,
		Tab = function(){
			this.tabConfig = {
                maxTabNum : undefined,  //最大可打开窗口数量
				tabFilter : "main-tab",  //添加窗口的filter
				url : undefined  //获取菜单json地址
			}
		};

    //生成左侧菜单
    Tab.prototype.navBar = function(dataStr){
        var data;
        if(typeof(dataStr) == "string"){
            var data = JSON.parse(dataStr);
        }else{
            data = dataStr;
        }
        var tabStr = '';
        for(var i=0;i<data.length;i++){
            if(data[i].spread || data[i].spread == undefined){
                tabStr += '<li class="layui-nav-item layui-nav-itemed">';
            }else{
                tabStr += '<li class="layui-nav-item">';
            }
            if(data[i].list != undefined && data[i].list.length > 0){
                tabStr += '<a href="javascript:;">';
                if(data[i].icon != undefined && data[i].icon != ''){
                    tabStr += '<i class=""'+data[i].icon+'"></i>';
                }
                tabStr += '<cite>'+data[i].name+'</cite>';
                tabStr += '<span class="layui-nav-more"></span>';
                tabStr += '</a>';
                tabStr += '<dl class="layui-nav-child">';
                for(var j=0;j<data[i].list.length;j++){
                    if(data[i].list[j].target == "_blank"){
                        tabStr += '<dd><a href="javascript:;" data-url="'+data[i].list[j].url+'" target="'+data[i].list[j].target+'">';
                    }else{
                        tabStr += '<dd><a href="javascript:;" data-url="'+data[i].list[j].url+'">';
                    }
                    if(data[i].list[j].icon != undefined && data[i].list[j].icon != ''){

                       tabStr += '<i class=""'+data[i].list[j].icon+'"></i>';
                    }
                    tabStr += '<cite>'+data[i].list[j].name+'</cite></a></dd>';
                }
                tabStr += "</dl>";
            }else{
                if(data[i].target == "_blank"){
                    tabStr += '<a href="javascript:;" data-url="'+data[i].url+'" target="'+data[i].target+'">';
                }else{
                    tabStr += '<a href="javascript:;" data-url="'+data[i].url+'">';
                }
                if(data[i].icon != undefined && data[i].icon != ''){
                    tabStr += '<i class=""'+data[i].icon+'"></i>';
                }
                tabStr += '<cite>'+data[i].name+'</cite></a>';
            }
            tabStr += '</li>';
        }
        return tabStr;
    }
	//获取二级菜单数据
	Tab.prototype.render = function(dataStr) {
		//显示左侧菜单
		var _this = this;
		$("#larry-side ul").append(_this.navBar(dataStr)).height($(window).height()-210);
		element.init();  //初始化页面元素
	}

	//点击窗口是否切换刷新页面
	Tab.prototype.changeRegresh = function(index){
        if(changeRefreshStr == "true"){
            $(".clildFrame .layui-tab-item").eq(index).find("iframe")[0].contentWindow.location.reload();
        }
	}

	//参数设置
	Tab.prototype.set = function(option) {
		var _this = this;
		$.extend(true, _this.tabConfig, option);
		return _this;
	};

	//获取lay-id
	Tab.prototype.getLayId = function(title){
		$(".layui-tab-title.tabs_home li").each(function(){
			if($(this).find("cite").text() == title){
				layId = $(this).attr("lay-id");
			}
		})
		return layId;
	}
	//通过title判断tab是否存在
	Tab.prototype.hasTab = function(title){
		var tabIndex = -1;
		$(".layui-tab-title.tabs_home li").each(function(){
			if($(this).find("cite").text() == title){
				tabIndex = 1;
			}
		})
		return tabIndex;
	}

	//tabs多页签渲染
	var tabIdIndex = 0;
	Tab.prototype.tabAdd = function(_this){
		if(window.sessionStorage.getItem("menu")){
			menu = JSON.parse(window.sessionStorage.getItem("menu"));
		}
		var that = this;
		var maxTabNum = that.tabConfig.maxTabNum;
			tabFilter = that.tabConfig.tabFilter;
		if(_this.attr("target") == "_blank"){
			window.open(_this.attr("data-url"));
		}else if(_this.attr("data-url") != undefined){
			var title = '';
			if(_this.find("i.seraph,i.layui-icon").attr("data-icon") != undefined){
				if(_this.find("i.seraph").attr("data-icon") != undefined){
					title += '<i class="seraph '+_this.find("i.seraph").attr("data-icon")+'"></i>';
				}else{
					title += '<i class="layui-icon">'+_this.find("i.layui-icon").attr("data-icon")+'</i>';
				}
			}
			//已打开的窗口中不存在
			if(that.hasTab(_this.find("cite").text()) == -1 && _this.siblings("dl.layui-nav-child").length == 0){
				if($(".layui-tab-title.tabs_home li").length == maxTabNum){
					layer.msg('只能同时打开'+maxTabNum+'个页签。');
					return;
				}
				tabIdIndex++;
				title += '<cite>'+_this.find("cite").text()+'</cite>';
				title += '<i class="layui-icon layui-unselect layui-tab-close" data-id="'+tabIdIndex+'">&#x1006;</i>';
				element.tabAdd(tabFilter, {
			        title : title,
			        content :"<iframe src='"+_this.attr("data-url")+"' data-id='"+tabIdIndex+"'></frame>",
			        id : new Date().getTime()
			    })
				//当前窗口内容
				var curmenu = {
					"icon" : _this.find("i.seraph").attr("data-icon")!=undefined ? _this.find("i.seraph").attr("data-icon") : _this.find("i.layui-icon").attr("data-icon"),
					"title" : _this.find("cite").text(),
					"href" : _this.attr("data-url"),
					"layId" : new Date().getTime()
				}
				menu.push(curmenu);
				window.sessionStorage.setItem("menu",JSON.stringify(menu)); //打开的窗口
				window.sessionStorage.setItem("curmenu",JSON.stringify(curmenu));  //当前的窗口
				element.tabChange(tabFilter, that.getLayId(_this.find("cite").text()));
			}else{
				//当前窗口内容
				var curmenu = {
					"icon" : _this.find("i.seraph").attr("data-icon")!=undefined ? _this.find("i.seraph").attr("data-icon") : _this.find("i.layui-icon").attr("data-icon"),
					"title" : _this.find("cite").text(),
					"href" : _this.attr("data-url")
				}
                that.changeRegresh(_this.parent('.layui-nav-item').index());
				window.sessionStorage.setItem("curmenu", JSON.stringify(curmenu));  //当前的窗口
				element.tabChange(tabFilter, that.getLayId(_this.find("cite").text()));
			}
		}
	}

    //切换后获取当前窗口的内容
	$("body").on("click",".tabs_home li",function(){
		var curmenu = '';
		var menu = JSON.parse(window.sessionStorage.getItem("menu"));
        if(window.sessionStorage.getItem("menu")) {
            curmenu = menu[$(this).index() - 1];
        }
		if($(this).index() == 0){
			window.sessionStorage.setItem("curmenu",'');
		}else{
			window.sessionStorage.setItem("curmenu",JSON.stringify(curmenu));
			if(window.sessionStorage.getItem("curmenu") == "undefined"){
				//如果删除的不是当前选中的tab,则将curmenu设置成当前选中的tab
				if(curNav != JSON.stringify(delMenu)){
					window.sessionStorage.setItem("curmenu",curNav);
				}else{
					window.sessionStorage.setItem("curmenu",JSON.stringify(menu[liIndex-1]));
				}
			}
		}
		element.tabChange(tabFilter,$(this).attr("lay-id")).init();
        layoutTabs.changeRegresh($(this).index());
		setTimeout(function(){
		},100);
	})

	//删除tab
	$("body").on("click",".tabs_home li i.layui-tab-close",function(){
		//删除tab后重置session中的menu和curmenu
		liIndex = $(this).parent("li").index();
		var menu = JSON.parse(window.sessionStorage.getItem("menu"));
		if(menu != null) {
            //获取被删除元素
            delMenu = menu[liIndex - 1];
            var curmenu = window.sessionStorage.getItem("curmenu") == "undefined" ? undefined : window.sessionStorage.getItem("curmenu") == "" ? '' : JSON.parse(window.sessionStorage.getItem("curmenu"));
            if (JSON.stringify(curmenu) != JSON.stringify(menu[liIndex - 1])) {  //如果删除的不是当前选中的tab
                // window.sessionStorage.setItem("curmenu",JSON.stringify(curmenu));
                curNav = JSON.stringify(curmenu);
            } else {
                if ($(this).parent("li").length > liIndex) {
                    window.sessionStorage.setItem("curmenu", curmenu);
                    curNav = curmenu;
                } else {
                    window.sessionStorage.setItem("curmenu", JSON.stringify(menu[liIndex - 1]));
                    curNav = JSON.stringify(menu[liIndex - 1]);
                }
            }
            menu.splice((liIndex - 1), 1);
            window.sessionStorage.setItem("menu", JSON.stringify(menu));
        }
		element.tabDelete("main-tab",$(this).parent("li").attr("lay-id")).init();

        var topTabsBoxWidth = $("#larry-tab").width()-127,
            topTabs = $("#tabs_home"),
            topTabsWidth = $("#tabs_home").width();
        if(topTabsBoxWidth > topTabsWidth) {
            topTabs.css("left",40);
		}

	})

    $(window).on("resize",function(){
        var topTabs = $("#tabs_home");
        topTabs.css("left",40);
	}).resize();

    //tabs向前操作
    $(".prev").on("click",function() {
        var topTabsBoxWidth = $("#larry-tab").width()-127,
            topTabs = $("#tabs_home"),
            topTabsWidth = $("#tabs_home").width(),
			maxLeft = topTabsWidth-topTabsBoxWidth,
        	leftStep = 50;

        if(topTabsWidth > topTabsBoxWidth) {
        	if(topTabs.position().left == 40){
                topTabs.css("left", -leftStep);
			}else if ((Math.abs(topTabs.position().left)+leftStep)>maxLeft) {
                topTabs.css("left",topTabs.position().left);
            } else {
                topTabs.css("left", -(Math.abs(topTabs.position().left)+leftStep));
            }
        }

    })

    //tabs向后操作
    $(".next").on("click",function() {
        var topTabs = $("#tabs_home"),
            minLeft = 40,
            leftStep = 50;
            if(topTabs.position().left < 40){
                if ((topTabs.position().left+leftStep)>minLeft) {
                    topTabs.css("left",minLeft);
                }else {
                    topTabs.css("left", topTabs.position().left+leftStep);
                }

            }

    })
	//刷新当前
	$(".refresh").on("click",function(){  //此处禁止短时间多次刷新
		if($(this).hasClass("refreshThis")){
			$(this).removeClass("refreshThis");
			$(".clildFrame .layui-tab-item.layui-show").find("iframe")[0].contentWindow.location.reload();
			setTimeout(function(){
				$(".refresh").addClass("refreshThis");
			},3000)
		}else{
			layer.msg("短时间多次刷新，会让服务器压力过大哦！");
		}
	})

	//关闭其他
	$(".closeOtherPages").on("click",function(){
		if($("#tabs_home li").length>2 && $("#tabs_home li.layui-this cite").text()!="后台首页"){
			var menu = JSON.parse(window.sessionStorage.getItem("menu"));
			$("#tabs_home li").each(function(){
				if($(this).attr("lay-id") != '' && !$(this).hasClass("layui-this")){
					element.tabDelete("main-tab",$(this).attr("lay-id")).init();
					//此处将当前窗口重新获取放入session，避免一个个删除来回循环造成的不必要工作量
					for(var i=0;i<menu.length;i++){
						if($("#tabs_home li.layui-this cite").text() == menu[i].title){
							menu.splice(0,menu.length,menu[i]);
							window.sessionStorage.setItem("menu",JSON.stringify(menu));
						}
					}
				}
			})
		}else if($("#tabs_home li.layui-this cite").text()=="后台首页" && $("#tabs_home li").length>1){
			$("#tabs_home li").each(function(){
				if($(this).attr("lay-id") != '' && !$(this).hasClass("layui-this")){
					element.tabDelete("main-tab",$(this).attr("lay-id")).init();
					window.sessionStorage.removeItem("menu");
					menu = [];
					window.sessionStorage.removeItem("curmenu");
				}
			})
		}else{
            layer.msg("窗口已全部关闭");
		}
	})
	//关闭全部
	$(".closeAllPages").on("click",function(){
		if($("#tabs_home li").length > 1){
			$("#tabs_home li").each(function(){
				if($(this).attr("lay-id") != ''){
					element.tabDelete("main-tab",$(this).attr("lay-id")).init();
					window.sessionStorage.removeItem("menu");
					menu = [];
					window.sessionStorage.removeItem("curmenu");
				}
			})
		}else{
			layer.msg("窗口已全部关闭");
		}
	})

	var layoutTabs = new Tab();
	exports("layoutTabs",function(option){
		return layoutTabs.set(option);
	});
})