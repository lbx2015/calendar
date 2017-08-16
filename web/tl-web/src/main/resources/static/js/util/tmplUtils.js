var TmplUtils = {
    _tdClickId : 0,
    isEditing : 0,
    pageSize : 10,
    totalPages : 0,
    currPage:1,
    searchParam:"",
    pageTmpl:{},
    totalCount : 0,
    _eunmLinkselectOnChangeCall : null,
    _eunmLinkselectChangedCall:null,
    onEunmLinkChanged:function(eunmLinkselectOnChangeCall,eunmLinkselectChangedCall) {
        TmplUtils._eunmLinkselectOnChangeCall = eunmLinkselectOnChangeCall;
        TmplUtils._eunmLinkselectChangedCall = eunmLinkselectChangedCall;
    },
    verify: {
        required: [
            /[\S]+/
            ,'必填项不能为空'
        ]
        ,phone: [
            /^1\d{10}$/
            ,'请输入正确的手机号'
        ]
        ,email: [
            /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/
            ,'邮箱格式不正确'
        ]
        ,url: [
            /(^#)|(^http(s*):\/\/[^\s]+\.[^\s]+)/
            ,'链接格式不正确'
        ]
        ,number: [
            /^\d+$/
            ,'只能填写数字'
        ]
        ,date: [
            /^(\d{4})[-\/](\d{1}|0\d{1}|1[0-2])([-\/](\d{1}|0\d{1}|[1-2][0-9]|3[0-1]))*$/
            ,'日期格式不正确'
        ]
        ,time: [
            /^([0-2][0-9]):([0-5][0-9]):([0-5][0-9])$/
            ,'时间格式不正确'
        ]
        ,identity: [
            /(^\d{15}$)|(^\d{17}(x|X|\d)$)/
            ,'请输入正确的身份证号'
        ]
        ,specialCode:[
            /^[\u4e00-\u9fa5a-z]+$/gi
            ,'请输入非特殊符号字符'
        ]
        ,spaceCode:[
            /^[^\s]*$/
            ,'请输入非空格字符'
        ]
        , minLen:function(value,len) {
            if (value.length < len) {
                return '请输入超过'+len+'个字符';
            }
        }
        , maxLen:function(value,len) {
            if (value.length > len) {
                return '请输入少于'+len+'个字符';
            }
        }
    },
    __tablePreviewSwitch : function(id) {
        if(TmplUtils.isEditing == 0) {
        /*    if ($(".contentLeft").hasClass("w1") && id == TmplUtils._tdClickId && TmplUtils._tdClickId != 0) {
                $(".contentLeft").removeClass("w1");
                $(".contentRight").removeClass("w2");
                $(".lay-tab-right").removeClass("w3");
                $("#tr_"+id).removeClass("bg081");
                $(window).resize();
                $(".viewTable").show();
                TmplUtils.isEditing = 0;
            }*/
        }
    },
    __tablePreviewOpen : function(id) {
        if(TmplUtils.isEditing == 0) {
            TmplUtils._tdClickId = id;
            $(".contentLeft").addClass("w1");
            $(".contentLeft").css("width","70%");
            $(".lay-tab-right").addClass("w3");
            $(".contentRight").addClass("w2");
            $(".viewTable").hide();
            $("#tr_"+id).addClass("bg081").siblings("tr").removeClass("bg081");
        }
    },
    __getEnumVal:function (enums,tableName,filed,key) {
        if(enums[tableName.toUpperCase()]){
            for(var i=0; i<enums[tableName.toUpperCase()].length; i++){
                if(enums[tableName.toUpperCase()][i].field.toLowerCase() == filed.toLowerCase() && enums[tableName.toUpperCase()][i].ke == key){
                    return enums[tableName.toUpperCase()][i].valu;
                }
            }
        }else{
            return "";
        }
        return "";
    },
    __getEnumKeyVals:function (enums,tableName,field) {
        var keyVals = [];
        if(enums[tableName.toUpperCase()]) {
            for (var i = 0; i < enums[tableName.toUpperCase()].length; i++) {
                if(enums[tableName.toUpperCase()][i].field.toLowerCase() == field.toLowerCase()){
                    var keyVal = {};
                    keyVal.key = enums[tableName.toUpperCase()][i].ke;
                    keyVal.val = enums[tableName.toUpperCase()][i].valu;
                    keyVals.push(keyVal);
                }
            }
        }
        return keyVals;
    },
    //关闭预览
    closePreview : function () {
        $(".contentLeft").removeClass("w1");
        $(".contentRight").removeClass("w2");
        $(".lay-tab-right").removeClass("w3");
        $(".lay-tab-left tr").removeClass("bg081");
        $(".lay-tab-right tr").removeClass("bg081");
        $(".viewTable").show();
        TmplUtils.isEditing = 0;
        TmplUtils._tdClickId = 0;
        $(window).resize();
    } ,
    // 表格加载结束
    tableOnLoad : function() {
        // 左侧高度
        var contentH = $(".layui-table").innerHeight();
        $(".contentRight").css("height", contentH);
        $(".contentRight .text").css("height", contentH - 81);

        $(window).resize(function() {
            if( !$(".contentLeft").hasClass("w1")){
                var contentW = $(".content").width();
                if($(".viewTable").length>0){
                    if($("#main-panel-side").hasClass("main-panel-left") ){
                        $(".contentLeft").css("width", contentW-311);
                    }else{
                        $(".contentLeft").css("width", contentW-40);
                    }
                }
            }
        }).resize();
        //var contentW = $(".iframeBody").width();
        //$(".contentLeft").css("width", contentW-40);

        $(".layui-table tr:gt(0)").each(
            function () {
                $(this).attr("data-index", $(this).index());
                var lastTr = null;
                $(this).hover(
                    function () {
                        lastTr = this;
                        $(".lay-tab-left tr")
                            .eq(
                                window.parseInt($(this).data(
                                    "index")) + 1)
                            .addClass("tron");
                        $(".lay-tab-right tr")
                            .eq(
                                window.parseInt($(this).data(
                                    "index")) + 1)
                            .addClass("tron");
                    },
                    function () {
                        $(".lay-tab-left tr").eq(
                            parseInt($(lastTr).data("index")) + 1)
                            .removeClass("tron");
                        $(".lay-tab-right tr").eq(
                            parseInt($(lastTr).data("index")) + 1)
                            .removeClass("tron");
                    }
                );
            });
        var enums = {};
        var enumurlHtmlObjs = $("._enumurl");
        for (var i = 0; i < enumurlHtmlObjs.size(); i++) {
            var enumurlHtmlObj = enumurlHtmlObjs.eq(i);
            var prop = enumurlHtmlObj.data("prop");
            var data = {};
            data.enumurl = enumurlHtmlObj.data("enumurl");
            data.enumparam = enumurlHtmlObj.data("enumparam");
            data.prop = enumurlHtmlObj.data("prop");
            enums[prop] = data;
        }
        for(var key in enums){
            var url = enums[key].enumurl;
            if (url.indexOf("http") == -1) {
                url = Const.apiUrl + url;
            }
            var param = "";
            if(enums[key].enumparam && enums[key].enumparam != "undefined"){
                param =  enums[key].enumparam + "&prop=" + enums[key].prop;
            }else{
                param = "prop=" + enums[key].prop;
            }
            View.get(url,param,function (resp) {
                var enumurlSpans = $("._enumurl");
                for (var i = 0; i < resp.length; i++) {
                    for (var k = 0; k < enumurlSpans.size(); k++) {
                        var dataKey = enumurlSpans.eq(k).data("key");
                        var dataProp = enumurlSpans.eq(k).data("prop");
                        if(resp[i] && resp[i].prop){
                            if (dataKey == resp[i].key && dataProp == resp[i].prop) {
                                enumurlSpans.eq(k).html(resp[i].value);
                            }
                        }else{
                            if (dataKey == resp[i].key) {
                                enumurlSpans.eq(k).html(resp[i].value);
                            }
                        }
                    }
                }
            },function () {
            });
        }
        var lazyEnums = [];
        var lazyEnumurlHtmlObjs = $("._lazyEnum");
        for (var i = 0; i < lazyEnumurlHtmlObjs.size(); i++) {
            var enumurlHtmlObj = lazyEnumurlHtmlObjs.eq(i);
            var prop = enumurlHtmlObj.data("prop");
            var data = {};
            data.enumurl = enumurlHtmlObj.data("enumurl");
            data.enumparam = enumurlHtmlObj.data("enumparam");
            data.prop = enumurlHtmlObj.data("prop");
            data.value =  enumurlHtmlObj.data("key");
            lazyEnums.push(data);
        }
        for(var i=0;i< lazyEnums.length;i++){
            var url = lazyEnums[i].enumurl;
            if (url.indexOf("http") == -1) {
                url = Const.apiUrl + url;
            }
            var param ="";
            if(lazyEnums[i].enumparam && lazyEnums[i].enumparam != "undefined"){
                param =  lazyEnums[i].enumparam + "&prop=" + lazyEnums[i].prop;
            }else{
                param = "prop=" + lazyEnums[i].prop;
            }
            if(lazyEnums[i].value.indexOf(",")>-1){
        	param +="&key="+lazyEnums[i].value.split(",")[0];
        	param += "&keyword="+lazyEnums[i].value.split(",")[1];
            }else{
        	param += "&keyword="+lazyEnums[i].value;
            }
            View.get(url,param,function (resp) {
                var enumurlSpans = $("._lazyEnum");
                    for (var k = 0; k < enumurlSpans.size(); k++) {
                        var dataKey = enumurlSpans.eq(k).data("key");
                        var dataProp = enumurlSpans.eq(k).data("prop");
                        if( resp[0] && resp[0].prop){
                            if (dataKey.indexOf( resp[0].key)>-1 && dataProp == resp[0].prop) {
                                enumurlSpans.eq(k).html(resp[0].value+" ");
                            }
                        }
                    }
            },function () {
            });
        }
        var multipleChoicePropObj= {};
        $("._multipleChoice").each(function(){
            var url = $(this).data("choiceurl");
            if(!url || url == "undefined"){
                return;
            }
            var param = $(this).data("choiceparam");
            if(!param || param=="undefined"){
                param = "";
            }
            var prop = $(this).data("prop");
            var data = {};
            data.prop = prop;
            data.url = url;
            data.param = param;
            multipleChoicePropObj[prop] = data;
        });

        for(var prop in multipleChoicePropObj){
            View.get(multipleChoicePropObj[prop].url,multipleChoicePropObj[prop].param,function(resp){
                if(resp){
                    $("._multipleChoice").each(function(){
                        var keysStr = $(this).data("keys");
                        var keys = [];
                        if(keysStr && keysStr != 'undefined'){
                            keys = keysStr.toString().split(",");
                        }
                        var htmlStr = "";
                        for(var i=0; i<resp.length; i++){
                            if((resp[i].prop&&resp[i].prop == $(this).data("prop"))||!resp[i].prop) {
                                if ($.inArray(resp[i].key, keys) > -1) {
                                    htmlStr = htmlStr + resp[i].value + ",";
                                }
                            }
                        }
                        if(htmlStr){
                            htmlStr = htmlStr.substring(0,htmlStr.length -1);
                        }
                        $(this).html(htmlStr);
                    });
                }
            },function(){});
        }
        if($("#tableLeft").data("hasleftwidth") == "0"){
            Utils.generateTablewidth("tableLeft");
        }
        if($("#tableRight").data("hasrightwidth") == "0"){
            Utils.generateTablewidth("tableRight");
        }
    },

    // 查询条件加载结束
    filterCriteriaOnLoad : function() {
        // 筛选查询
        $("#layui-form-select-l").click(function() {
            $("#layui-form-select-p").toggleClass("layui-form-selected-l");
        });
        // 筛选查询tip
        $("._query").mouseover(function() {
            if($(this).data("desc")){
                layer.tips($(this).data("desc"), $(this), {
                    tips : [ 3, '#08c' ],// 还可配置颜色
                    time : 1000
                });
            }
        });

        $(".layui-unselect").mouseover(function() {
            if($(this).parent(".layui-form-select").siblings("._query").data("desc")){
                layer.tips($(this).parent(".layui-form-select").siblings("._query").data("desc"), $(this), {
                    tips : [ 3, '#08c' ],// 还可配置颜色
                    time : 3000
                });
            }
        });

        $(document).click(function() {
            $("#layui-form-select-p").removeClass("layui-form-selected-l");
        });
        $("#layui-form-select-p").click(function(event) {
            event.stopPropagation();
        });

        setTimeout(function(){
            $(".layui-form-select dd").mouseover(function() {
                if($(this).html().length > 15){
                    layer.tips($(this).html(), $(this), {
                        tips : [ 1, '#08c' ],// 还可配置颜色
                        time : 3000
                    });
                }
            });
        },500);
        $("input").each(function(){
            if($(this).data("type") && $(this).data("type")  == "date"){
                $(this).bind("input propertychange",function () {
                    if($(this).val().length == 8 && $(this).val().indexOf("-") == -1){
                        $(this).val($(this).val().substr(0,4)+"-"+$(this).val().substr(4,2) + "-"+$(this).val().substr(6,2));
                    }
                });
            }
        });
        $("#filterCriteria").find("select").each(function(){
            if($(this).data("enumurl") && $(this).data("enumurl") != "undefined"){
                var url =  $(this).data("enumurl");
                var prop = $(this).data("prop");
                var param = $(this).data("enumparam");
                if(!param || param=="undefined"){
                    param = "prop="+prop;
                }else{
                    param = prop + "&prop=" + prop;
                }
                var _this = this;
                View.get(url,param,function (resp) {
                    if(resp){
                        for(var i =0;i<resp.length;i++){
                            $(_this).append("<option value='"+resp[i].key+"'>"+resp[i].value+"</option>");
                        }
                    }
                    layui.form().render();
                    TmplUtils.layuiSelectOptionMouseOver();
                },function () {
                })
            }
        });
    },

    // 预览加载结束
    previewOnLoad : function() {
        var contentH = $(".layui-table").innerHeight();
        $(".contentRight").css("height", contentH);
        $(".contentRight .text").css("height", contentH - 81);
        var form = layui.form();
        // 监听指定开关
        form.on('switch(switchTest)', function(data) {
            if (this.checked) {
                $("#htmlCont select").each(function(){
                    if( $(this).data("editable") && $(this).data("editable") == 2){
                    }else{
                        $(this).removeAttr("disabled");
                        $(this).next().removeClass("layui-select-disabled");
                    }
                });
                $("#preservationBtn").show();
                $("#checkBnt").show();
                $('#preview input,#preview textarea').each(function(){
                        if( $(this).data("editable") && $(this).data("editable") == 2){
                        }else{
                            $(this).removeAttr("disabled");
                        }

                    }
                );
                TmplUtils.isEditing = 1;

            } else {
                $("#htmlCont select").attr("disabled", 'disabled');
                $("#preservationBtn").hide();
                $('#preview input,#preview textarea').attr("disabled", "disabled");
                $("#checkBnt").hide();
                TmplUtils.isEditing = 0;
            }
        });
        $("#closePreview").click(function() {
            TmplUtils.closePreview();
        });
        setTimeout(function () {
            $("#preview").find("select").each(function(){
                if($(this).data("enumurl") && $(this).data("enumurl") != "undefined"){
                    var url = Const.apiUrl + $(this).data("enumurl");
                    var _this = this;
                    var param = $(this).data("enumparam");
                    var prop = $(this).data("prop");
                    if(!param || param ==  "undefined"){
                        param = "";
                    }else{
                        param = param + "&prop"+prop;
                    }
                    View.get(url,param,function (resp) {
                        if(resp){
                            $(_this).html("");
                            $(_this).append("<option value='--##--'>--请选择--</option>");
                            for(var i =0;i<resp.length;i++){
                                if($(_this).data("key")==resp[i].key){
                                    $(_this).append("<option selected='true' value='"+resp[i].key+"'>"+resp[i].value+"</option>");
                                }else{
                                    $(_this).append("<option value='"+resp[i].key+"'>"+resp[i].value+"</option>");
                                }
                            }
                        }
                        layui.form().render();
                        TmplUtils.layuiSelectOptionMouseOver();
                        if($(_this).data("type")=="enumLink"){
                            var attrName = $(_this).attr("name");
                            var attrNameNext = attrName.substr(0,attrName.lastIndexOf("-|-")+3)+(parseInt(attrName.substr(attrName.lastIndexOf("-|-")+3,attrName.length))+1);
                            if($("select[name='"+attrNameNext+"']").size()>0){
                                var form = layui.form();
                                form.on('select('+attrName+')', function(data){
                                    var attrName = $(data.elem).attr("name");
                                    var attrNameNext = attrName.substr(0,attrName.lastIndexOf("-|-")+3)+(parseInt(attrName.substr(attrName.lastIndexOf("-|-")+3,attrName.length))+1);
                                    if(!$("select[name='"+attrNameNext+"']")){
                                        return;
                                    }
                                    var url_t = $("select[name='"+attrNameNext+"']").data("enumurl");
                                    var param_t = $("select[name='"+attrNameNext+"']").data("enumparam");
                                    var prop = $("select[name='"+attrNameNext+"']").data("prop");
                                    if(param_t && param_t != "undefined"){
                                        param_t = param_t + "&key="+ data.value +"&prop=" + prop;
                                    }else{
                                        param_t =  "key="+ data.value +"&prop=" + prop;;
                                    }
                                    View.get(url_t,param_t,function (resp) {
                                        if (resp) {
                                            $("select[name='"+attrNameNext+"']").html("");
                                            $("select[name='"+attrNameNext+"']").append("<option value='--##--'>--请选择--</option>");
                                            for (var i = 0; i < resp.length; i++) {
                                                if (  $("select[name='"+attrNameNext+"']").data("key") == resp[i].key) {
                                                    $("select[name='"+attrNameNext+"']").append("<option selected='true' value='" + resp[i].key + "'>" + resp[i].value + "</option>");
                                                } else {
                                                    $("select[name='"+attrNameNext+"']").append("<option value='" + resp[i].key + "'>" + resp[i].value + "</option>");
                                                }
                                            }
                                        }
                                        layui.form().render();
                                        TmplUtils.layuiSelectOptionMouseOver();
                                    });
                                });
                            }
                        }
                    },function () {
                    });
                }
            });
            $("#preview").find("input[data-type='lazyEnum']").each(function() {
        	     var _this = this;
        	     var _key = $(this).val();
        	     var _enum_url = $(this).data("enum_urls");
        	     if (_key&&_enum_url) {
        	         var keys = _key.split(",");
        	         var urls = _enum_url.split(",");
        	         View.get(urls[0], "keyword="+keys[0], function(data) {
        	             if (keys[0] && keys[1]) {
        	                 View.get(urls[1], "key="+keys[0]+"&keyword="+keys[1], function(data2) {
        	                     $(_this).val(data[0].value+" " + data2[0].value);
        	                 });
        	             }
        	         });
        	     }
    	 	});
        },500)
    },
    //编辑加载结束
    editOnLoad : function(){
        var element = layui.element();
        var layer = layui.layer;
        element.init();
        //监听折叠
        element.on('collapse(edit)', function(data){

        });
        // 筛选查询tip
        $("label").mouseover(function() {
            if($(this).data("desc")){
                layer.tips($(this).data("desc"), $(this), {
                    tips : [ '1', '#08c' ],// 还可配置颜色
                    time : 3000
                });
                $(".layui-layer-tips").css("left",$(this).offset().left+120);
            }
        });
        $(".layui-form-select dd").mouseover(function() {
            if($(this).html().length > 15){
                layer.tips($(this).html(), $(this), {
                    tips : [ 1, '#08c' ],// 还可配置颜色
                    time : 3000
                });
            }
        });
        $("input").each(function(){
            if($(this).data("type") && $(this).data("type") && $(this).data("type") == "date"){
                $(this).bind("input propertychange",function () {
                    if($(this).val().length == 8 && $(this).val().indexOf("-") == -1){
                        $(this).val($(this).val().substr(0,4)+"-"+$(this).val().substr(4,2) + "-"+$(this).val().substr(6,2));
                    }
                });
            }
        });
        setTimeout(function(){
            $("#edit").find("select").each(function(){
                if($(this).data("enumurl") && $(this).data("enumurl") != "undefined"){
                    var url = Const.apiUrl + $(this).data("enumurl");
                    var prop = $(this).data("prop");
                    var _this = this;
                    var param = $(this).data("enumparam");
                    if(!param || param ==  "undefined"){
                        param = "prop=" + prop;
                    }else{
                        param = param + "&prop=" + prop;
                    }
                    View.get(url,param,function (resp) {
                        if(resp){
                            $(_this).html("");
                            $(_this).append("<option value='--##--'>--请选择--</option>");
                            for(var i =0;i<resp.length;i++){
                                if($(_this).data("key")==resp[i].key){
                                    $(_this).append("<option selected='true' value='"+resp[i].key+"'>"+resp[i].value+"</option>");
                                }else{
                                    $(_this).append("<option value='"+resp[i].key+"'>"+resp[i].value+"</option>");
                                }
                            }
                        }
                        layui.form().render();
                        TmplUtils.layuiSelectOptionMouseOver();
                        if($(_this).data("type")=="enumLink"){
                            var attrName = $(_this).attr("name");
                            var attrNameNext = attrName.substr(0,attrName.lastIndexOf("-|-")+3)+(parseInt(attrName.substr(attrName.lastIndexOf("-|-")+3,attrName.length))+1);
                            if($("select[name='"+attrNameNext+"']").size()>0){
                                var form = layui.form();
                                form.on('select('+attrName+')', function(data){
                                    if(TmplUtils._eunmLinkselectOnChangeCall){
                                        if(!TmplUtils._eunmLinkselectOnChangeCall.call(this,data.elem)){
                                            return;
                                        }
                                    }
                                    var attrName = $(data.elem).attr("name");
                                    var attrNameNext = attrName.substr(0,attrName.lastIndexOf("-|-")+3)+(parseInt(attrName.substr(attrName.lastIndexOf("-|-")+3,attrName.length))+1);
                                    if(!$("select[name='"+attrNameNext+"']")){
                                        return;
                                    }
                                    var url_t = $("select[name='"+attrNameNext+"']").data("enumurl");
                                    var param_t = $("select[name='"+attrNameNext+"']").data("enumparam");
                                    var prop = $("select[name='"+attrNameNext+"']").data("prop");
                                    if(param_t && param_t != "undefined"){
                                        param_t = param_t + "&key="+ data.value +"&prop=" + prop;
                                    }else{
                                        param_t =  "key="+ data.value +"&prop=" + prop;;
                                    }
                                    View.get(url_t,param_t,function (resp) {
                                        if (resp) {
                                            $("select[name='"+attrNameNext+"']").html("");
                                            $("select[name='"+attrNameNext+"']").append("<option value='--##--'>--请选择--</option>");
                                            for (var i = 0; i < resp.length; i++) {
                                                if (  $("select[name='"+attrNameNext+"']").data("key") == resp[i].key) {
                                                    $("select[name='"+attrNameNext+"']").append("<option selected='true' value='" + resp[i].key + "'>" + resp[i].value + "</option>");
                                                } else {
                                                    $("select[name='"+attrNameNext+"']").append("<option value='" + resp[i].key + "'>" + resp[i].value + "</option>");
                                                }
                                            }
                                        }
                                        layui.form().render();
                                        TmplUtils.layuiSelectOptionMouseOver();
                                        if(TmplUtils._eunmLinkselectChangedCall){
                                            TmplUtils._eunmLinkselectChangedCall.call(this,data.elem);
                                        }
                                    });
                                });
                            }
                        }
                    },function () {
                    })
                }
            });
            $("#edit").find("._multipleChoice").each(function(){
                var url = $(this).data("choiceurl");
                if(!url || url == "undefined"){
                    return;
                }
                var param = $(this).data("choiceparam");
                if(!param || param=="undefined"){
                    param = "";
                }
                var keysStr = $(this).data("keys");
                var keys = [];
                if(keysStr && keysStr != 'undefined'){
                    try{
                        keys = keysStr.toString().split(",");
                    }catch(e){
                    }
                }
                var prop = $(this).data("prop");
                var editable = $(this).data("editable");
                if(!editable || editable == "undefined"){
                    editable == "0";
                }
                editable = parseInt(editable);
                var _this = this;
                View.get(url,param,function(resp){
                    if(resp){
                        $(_this).html("");
                        for(var i=0; i<resp.length; i++){
                            if(editable > 0){
                                if($.inArray(resp[i].key,keys)>-1){
                                    $(_this).append('<input disabled type="checkbox" name="'+prop+'-|-'+resp[i].key+ '" checked title="'+resp[i].value+'" lay-skin="primary">' );
                                }else{
                                    $(_this).append('<input disabled type="checkbox" name="'+prop+'-|-'+resp[i].key+'" title="'+resp[i].value+'" lay-skin="primary">');
                                }
                            }else{
                                if($.inArray(resp[i].key,keys)>-1) {
                                    $(_this).append('<input type="checkbox" name="' + prop + '-|-' + resp[i].key + '" checked title="' + resp[i].value + '" lay-skin="primary">');
                                }else{
                                    $(_this).append('<input type="checkbox" name="' + prop + '-|-' + resp[i].key + '" title="' + resp[i].value + '" lay-skin="primary">');
                                }
                            }
                        }
                        layui.form().render();
                    }
                },function(){});
            });
        },500);
        $(top.window).scrollTop(0);
        $(parent.document).find('.layer-iframe-js').css({
            "min-height" : "100%"
        });
    },
    pagesOnload : function(_tbTmpl){
        // $("#page_count").change(function () {
        //     TmplUtils.pageSize = $("#page_count").val()
        //     tbTmpl.refresh("pindex=0"+"&pcount="+pageSize +searchParam);
        //     TmplUtils.closePreview();
        // })
        var form = layui.form();
        form.on('select(pageSelect)', function(data){
            TmplUtils.pageSize = data.value;
            TmplUtils.currPage = 1;
            if(_tbTmpl){
                _tbTmpl.refresh("pindex=0"+"&pcount="+TmplUtils.pageSize +TmplUtils.searchParam);
            }
            TmplUtils.closePreview();
        });
    },
    showMsgSuccess:function(str){
        var layer = layui.layer;
        top.layer.msg(str, {icon: 6});
    },
    showMsgFail:function(str){
        var layer = layui.layer;
        top.layer.msg(str, {icon: 5});
    },
    showConfirm:function(str,ok,cancel,okCallback,cancelCallback){
        var layer = top.layui.layer;
        layer.confirm(str, {
            btn: [ok,cancel] //按钮
        }, function () {
            okCallback.call(this);
        },function(){
            cancelCallback.call(this);
        });
    },
    check:function(formId){
        var formObj = document.getElementById(formId);
        var fobj = Utils.formToObj(formId);
        var reg = /^[0-9]+.?[0-9]*$/;
        if(formObj){
            var elementsObj=formObj.elements;
            var obj;
            var ff;
            if(elementsObj){
                for(var i=0; i<elementsObj.length;i+=1){
                    obj=elementsObj[i];
                    var value= obj.value;
                    if(value){
                        value = value.replace("--##--","");
                    }
                    var verifys = [];
                    if($(obj).data("verifyfun") && $(obj).data("verifyfun") != "undefined"){
                        for(var key in fobj){
                            if($(obj).data("verifyfun").indexOf("$"+key) > 0){
                                var val;
                                if(reg.test(fobj[key])&&fobj[key].type=="int"){
                                    val = fobj[key];
                                }else{
                                    val = '"'+fobj[key]+'"';
                                }
                                ff = $(obj).data("verifyfun");
                                ff = ff.replace("$"+key,val);
                                try{
                                    var fun =new Function(ff);
                                    verifys = fun().split(",");
                                }catch(e){

                                }
                            }
                        }
                    }
                    if($(obj).data("verify")){
                        var ver = $(obj).data("verify").split(',');
                        for(var j=0; j<ver.length; j++){
                            verifys.push(ver[j]);
                        }
                    }
                    for(var j=0; j<verifys.length; j++){
                        thisVer = verifys[j];
                        if(TmplUtils.verify[thisVer]) {
                            var isFn = typeof TmplUtils.verify[thisVer] === 'function';
                            var tips = "";
                            if (TmplUtils.verify[thisVer] && (isFn ? tips = TmplUtils.verify[thisVer](value,$(obj).data("len")) : !TmplUtils.verify[thisVer][0].test(value))) {
                                if(tips){
                                    TmplUtils.showMsgFail($(obj).data("alias")+tips);
                                }else{
                                    TmplUtils.showMsgFail($(obj).data("alias")+TmplUtils.verify[thisVer][1]);
                                }
                                /*            if($(obj).parent("div").parent("div").parent("div")){
                                 $(obj).parent("div").parent("div").parent("div").parent("div").addClass("layui-show");
                                 }*/
                                if($(obj).parent("div").parent("div").parent("div").parent("div")){
                                    $(obj).parent("div").parent("div").parent("div").parent("div").addClass("layui-show");
                                }
                                if(obj.tagName=="SELECT"){
                                    $(obj).next("div").find("input").removeAttr("readonly");
                                    $(obj).next("div").find("input").focus();
                                    $(obj).next("div").find("input").attr("readonly",true);
                                }else{
                                    $(obj).focus();
                                }
                                $(obj).addClass('layui-form-danger');
                                return false;
                            }
                        }
                    }
                }
                var forms = formObj.getElementsByTagName("form");
                for(var i=0; i< forms.length; i++){
                    if(!TmplUtils.check($(forms[i]).attr("id"))){
                        return false;
                    }
                }
            }else{
                return true;
            }
        }else{
            return true;
        }
        return true;
    },
    pagesOnBind : function(attrs,param,dependTmpl) {
        tmpl = this;
        TmplUtils.pageTmpl = tmpl;
        var _this = this;
        var dependTmpl = View.getTmpl(dependTmpl);
        if(null == TmplUtils.totalPages || undefined == TmplUtils.totalPages){
            return;
        }
        tmpl.bind(function (callback) {
            callback.call(_this, {});
            var laypage = layui.laypage;
            laypage({
                cont: 'laypage',
                pages: TmplUtils.totalPages,
                skip: true,
                curr:TmplUtils.currPage,
                jump:function (obj,first){
                    if(!first){
                        TmplUtils.currPage = obj.curr;
                        dependTmpl.refresh("pindex="+(obj.curr-1)+"&pcount="+ TmplUtils.pageSize+TmplUtils.searchParam);
                        TmplUtils.closePreview();
                    }
                }
            });
            //加载选择页数下拉框切换事件
            TmplUtils.pagesOnload(dependTmpl);
        })
    },
    rollingDown :function(){
        if(document.getElementsByTagName("iframe")[0]){
            $(document.getElementsByTagName("iframe")[0].contentWindow.document).find(".layui-btn-div").css("top",$(parent.window).scrollTop()-127);
        }

    },
    roll: function(){
        if(document.getElementsByTagName("iframe")[0]){
            $(document.getElementsByTagName("iframe")[0].contentWindow.document).find(".layui-btn-div").css("top",0);
        }
    },
    layuiSelectOptionMouseOver : function () {
        $(".layui-form-select dd").mouseover(function() {
            if($(this).html().length > 15){
                layer.tips($(this).html(), $(this), {
                    tips : [ 1, '#08c' ],// 还可配置颜色
                    time : 3000
                });
            }
        });
    },
    _getTableTdConfig:function(data){
        var tableTdConfig = {};
        tableTdConfig.cellLeftCount = !!Const.cellLeftCount?Const.cellLeftCount:4;
        tableTdConfig.cellRightCount =  !!Const.cellRightCount?Const.cellRightCount:3;
        tableTdConfig.cellLeftCount = !!data.cellLeftCount?data.cellLeftCount: tableTdConfig.cellLeftCount;
        tableTdConfig.cellRightCount = !!data.cellRightCount?data.cellRightCount: tableTdConfig.cellRightCount;
        tableTdConfig.totalCellLeftCount = tableTdConfig.cellLeftCount;
        tableTdConfig.totalCellRightCount = tableTdConfig.cellRightCount;
        //是否需要全选checkbox
        tableTdConfig.tdLeftWidthList = [];
        tableTdConfig.tdRightWidthList = [];
        if(data.hasCheckbox){
            tableTdConfig.totalCellLeftCount ++;
            tableTdConfig.tdLeftWidthList.push(5);
        }
        //是否有操作列
        if(data.hasOperation){
            tableTdConfig.totalCellLeftCount ++;
            if(data.operationWidth){
                tableTdConfig.tdLeftWidthList.push(data.operationWidth)
            }else{
                tableTdConfig.tdLeftWidthList.push(20);
            }
        }

        if(!data.tdLeftWidth) {
            data.tdLeftWidth=[];
            tableTdConfig.hasLeftWidth =0;
        }else{
            tableTdConfig.hasLeftWidth =1;
        }
        for(var i=0; i< tableTdConfig.cellLeftCount; i++) {
            if(i<data.attrs.length){
                if(i<data.tdLeftWidth.length){
                    tableTdConfig.tdLeftWidthList.push(data.tdLeftWidth[i]);
                }else{
                    tableTdConfig.tdLeftWidthList.push(20);
                }
            }
        }
        if(!data.tdRightWidth){
            data.tdRightWidth = [];
            tableTdConfig.hasRightWidth =0;
        }else{
            tableTdConfig.hasRightWidth =1;
        }
        for(var i=0; i< tableTdConfig.cellRightCount; i++) {
            if(i+tableTdConfig.cellLeftCount<data.attrs.length){
                if(i< data.tdRightWidth.length){
                    tableTdConfig.tdRightWidthList.push(data.tdRightWidth[i]);
                }else{
                    tableTdConfig.tdRightWidthList.push(20);
                }
            }
        }
        tableTdConfig.hasRight = false;
        if(tableTdConfig.cellLeftCount +1 < data.attrs.length){
            tableTdConfig.hasRight =true;
            if(tableTdConfig.cellLeftCount + tableTdConfig.cellRightCount > data.attrs.length){
                tableTdConfig.cellRightCount = data.attrs.length - tableTdConfig.cellLeftCount;
                tableTdConfig.totalCellRightCount = tableTdConfig.cellRightCount;
            }
        }else if(tableTdConfig.cellLeftCount +1 == data.attrs.length){
            tableTdConfig.cellLeftCount ++;
            tableTdConfig.totalCellLeftCount ++;
            data.tdLeftWidth.push( data.tdRightWidth[0]);
        }else{
            var comp = tableTdConfig.totalCellLeftCount -  tableTdConfig.cellLeftCount;
            tableTdConfig.cellLeftCount = data.attrs.length;
            tableTdConfig.totalCellLeftCount = comp +  tableTdConfig.cellLeftCount;
        }
        return tableTdConfig;
    },

    _getPropValue:function(itemStr,prop){
        var fun =new Function("return "+itemStr+"."+prop+";");
        var propValue = "";
        try{
            propValue = fun();
        }catch(e){
        }
        if(null == propValue || undefined == propValue || propValue == "null" || propValue == "undefined"){
            return "";
        }
        return propValue;
    },

    _addMultipleString:function (_this,name) {
        var htmlStr = '<div class="layui-form-item multipleStringInfo"> <input type="text" name="'+name+'-|-'+Math.ceil(Math.random()*10000)+'"  autocomplete="off" placeholder="" class="layui-input"> <span class="multipleString" onclick="TmplUtils._removeMultipleString(this);"><i class="layui-icon">&#xe640;</i></span> </div>';
        $(_this).after(htmlStr);
    },
    _removeMultipleString:function (_this) {
        $(_this).parent(".multipleStringInfo").remove();
    },
    _addMultipleEnum:function (name) {
        var val = $("select[name="+name+"_me]").val();
        if(!val || val=="--##--"){
            TmplUtils.showMsgFail("请选择一个选项");
            return;
        }
        var text = $("select[name="+name+"_me]").find("option:selected").text();
        var htmlStr = ' <li> <input name="'+name+'-|-'+Math.ceil(Math.random()*10000)+'" data-type="multipleEnum"  data-key="'+val+'" value="'+text+'"/> <i class="layui-icon" title="删除" onclick="TmplUtils._removeMultipleEnum(this);">&#xe640;</i> </li>';
        $("ul[name="+name+"]").append(htmlStr);
    },
    _removeMultipleEnum:function (_this) {
        $(_this).parent("li").remove();
    }
}
$(document).bind('click',function(e){
    $(".layui-layer-tips").each(function(){
        if( $(this).attr('showtime')<5000){
            $(this).hide();
        }
    });
    if(e.target.tagName == "INPUT" && $(e.target).val().indexOf("请选择")>0){
        $(e.target).val("");
    }
});