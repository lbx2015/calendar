var Utils = {
    compare:function(propertyName){
        return function(object1, object2) {
            var value1 = object1[propertyName];
            var value2 = object2[propertyName];
            if (value2 < value1) {
                return 1;
            } else if (value2 > value1) {
                return -1;
            } else {
                return 0;
            }
        }
    },
    getDate : function(s) {
        if(!s){
            return "";
        }
/*
 * var now=new Date(s); var year = now.getFullYear(); var month =
 * now.getMonth()+1; var date = now.getDate(); return year+"-"+month+"-"+date;
 */
        if(Tool.isStr(s)&&isNaN(s)){
            var time = new Date(s.replace(/-/g,'/').replace(/T|Z/g,' ').replace(/(^\s*)|(\s*$)/g, ""));
            return time.format("yyyy-MM-dd");
        }else{
            if(Tool.isStr(s) && !isNaN(s)){
                s = parseInt(s);
            }
            return  new Date(s).format("yyyy-MM-dd");
        }
    },
    getDatetime : function(s) {
        if(!s){
            return "";
        }

        if(Tool.isStr(s)&&isNaN(s)){
            var time = new Date(s.replace(/-/g,'/').replace(/T|Z/g,' ').replace(/(^\s*)|(\s*$)/g, ""));
            return time.format("yyyy-MM-dd hh:mm:ss");
        }else{
            if(Tool.isStr(s) && !isNaN(s)){
                s = parseInt(s);
            }
            return  new Date(s).format("yyyy-MM-dd hh:mm:ss");
        }
    },
    getTime : function(s) {
        if(!s){
            return "";
        }
        if(Tool.isStr(s)&&isNaN(s)){
            var time = new Date(s.replace(/-/g,'/').replace(/T|Z/g,' ').replace(/(^\s*)|(\s*$)/g, ""));
            return time.format("hh:mm:ss");
        }else{
            if(Tool.isStr(s) && !isNaN(s)){
                s = parseInt(s);
            }
            return  new Date(s).format("hh:mm:ss");
        }
    },
    formToObj : function(formId){
        var formObj = document.getElementById(formId);
        var data={};
        if(!formObj){
            return data;
        }
        var elementsObj=formObj.elements;
        var obj;
        if(!elementsObj){
            var forms = formObj.getElementsByTagName("form");
            for(var i=0; i< forms.length; i++){
                data = $.extend(true,data,Utils.formToObj($(forms[i]).attr("id")));
            }
            return data;
        }
        for(var i=0; i<elementsObj.length;i+=1){
            obj=elementsObj[i];
            if(obj.name!=undefined&&obj.name!=""){
                if(obj.tagName == "SELECT"&& $(obj).data("type") &&$(obj).data("type").toLowerCase() == "multipleenum"){
                    continue;
                }
                var value = obj.value;
                if(value == "--##--") {
                    value="";
                }
                if($(obj).data("type") == "money"){
                    value = Utils.moneyToNumber(value);
                }
                if($(obj).data("type") && $(obj).data("type").toLowerCase() == "multipleenum"){
                    value = $(obj).data("key");
                }
                if(value && Tool.isStr(value)){
                    value = value.replace(/(^\s*)|(\s*$)/g, "");
                }
                if(obj.name.indexOf("-|-") > 0){
                    var props = obj.name.split("-|-");
                    if(data[props[0]]){
                        if(obj.type == "checkbox" && obj.checked) {
                            data[[props[0]]] = data[[props[0]]] + "," + props[1];
                        }else if(obj.tagName == "SELECT"){
                            data[[props[0]]] =data[[props[0]]]  + "," + value;
                        }else if(obj.tagName == "INPUT"&&obj.type != "checkbox"){
                            data[[props[0]]] =data[[props[0]]]  + "," + value;
                        }
                    }else{
                        if(obj.type == "checkbox" && obj.checked){
                            data[[props[0]]] =props[1];
                        }else if(obj.tagName == "SELECT"){
                            data[[props[0]]] =value;
                        }else if(obj.tagName == "INPUT"&&obj.type != "checkbox"){
                            data[[props[0]]] =value;
                        }
                    }
                }else if(obj.name.indexOf(".") > 0){
                    var props = obj.name.split(".");
                    var funStr = "";
                    for(var j = 0; j<props.length; j++){
                        if(j == props.length -1){
                            funStr += "var " +props[j] +"='"+ value +"';" + props[j-1] + "." +props[j] + "=" + props[j] + "; ";
                        }else{
                            if(j == 0){
                                funStr += "var " +props[j] + "={}; "
                            }else{
                                funStr +=  "var " +props[j] +"={};" + props[j-1] + "." +props[j] +  "=" + props[j] + "; ";
                            }
                        }
                    }
                    funStr += "return " +  props[0] + ";";
                    var fun = new Function(funStr);
                    if( data[props[0]]){
                        $.extend(data[props[0]],fun());
                    }else {
                        data[props[0]] = fun();
                    }
                }else{
                    data[obj.name] = value;
                }
            }
        }
        var forms = formObj.getElementsByTagName("form");
        for(var i=0; i< forms.length; i++){
            data = $.extend(true,data,Utils.formToObj($(forms[i]).attr("id")));
        }
        return data;
    },

    formToStr : function(formId) {
        var formObj = document.getElementById(formId);
        var str = "";
        if (formObj) {
            var elementsObj = formObj.elements;
            var obj;
            if (elementsObj) {
                for (var i = 0; i < elementsObj.length; i += 1) {
                    obj = elementsObj[i];
                    if (obj.name != undefined && obj.name != "") {
                        if (obj.value != "--##--") {
                            var value = "";
                            if(obj.value){
                                value = obj.value.replace(/(^\s*)|(\s*$)/g, "");
                            }
                            str += "&" + obj.name + "=" + value;
                        }
                    }
                }
            } else {
                return;
            }
        } else {
            return;
        }
        return str;
    },
    numberToMoney : function (num,digit) {
        if(num == null){
            return "";
        }
        if(isNaN(num)){
            return num;
        }
        if(!digit){
            digit = 2;
        }
        return parseFloat(num).formatMoney(digit);
    },
    moneyToNumber : function (money) {
        if(!money){
            return "";
        }
        try {
            return parseFloat(money.replace(/[^0-9-.]/g, ''));
        }catch (e){
            return money;
        }
    },
    generateTablewidth : function(tableid) {
        var tableObj = document.getElementById(tableid);
        var lengths = [];
        for (var i = 0; i < tableObj.rows.length; i++) {    //遍历Table的所有Row
            for (var j = 0; j < tableObj.rows[i].cells.length; j++) {   //遍历Row中的每一列
                var tdHtmllength  = tableObj.rows[i].cells[j].innerHTML.gblen();   //获取Table中单元格的内容
                if(lengths[j]){
                    if(lengths[j]<tdHtmllength){
                        lengths[j] = tdHtmllength;
                    }
                }else{
                    lengths[j] = tdHtmllength;
                }
            }
        }
        var total = 0;
        for(var i=0; i<lengths.length;i++){
            total +=lengths[i];
        }
        for(var i=0; i<lengths.length;i++){
            lengths[i] =(lengths[i]/total)*100;
        }
        i =0;
        $("#"+tableid).find("col").each(function(){
            $(this).attr("width",lengths[i] +"%");
            i++;
        });
    }
};
/*Number.prototype.formatMoney = function (places, thousand, decimal) {
    places = !isNaN(places = Math.abs(places)) ? places : 2;
    thousand = thousand || ",";
    decimal = decimal || ".";
    var number = this,
        negative = number < 0 ? "-" : "",
        i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "",
        j = (j = i.length) > 3 ? j % 3 : 0;
    return  negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "");
};*/

Number.prototype.formatMoney =function(digit){
    var money = this;
    var tpMoney = '0.00';
    if(undefined != money){
        tpMoney = money;
    }
    tpMoney = new Number(tpMoney);
    if(isNaN(tpMoney)){
        return '0.00';
    }
    tpMoney = tpMoney.toFixed(digit) + '';
    var re = /^(-?\d+)(\d{3})(\.?\d*)/;
    while(re.test(tpMoney)){
        tpMoney = tpMoney.replace(re, "$1,$2$3")
    }
    return tpMoney;
}
Date.prototype.format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1,                 // 月份
        "d+" : this.getDate(),                    // 日
        "h+" : this.getHours(),                   // 小时
        "m+" : this.getMinutes(),                 // 分
        "s+" : this.getSeconds(),                 // 秒
        "q+" : Math.floor((this.getMonth()+3)/3), // 季度
        "S"  : this.getMilliseconds()             // 毫秒
    };
    if(/(y+)/.test(fmt)) {
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
}
// 两种调用方式
// var template1="我是{0}，今年{1}了";
// var template2="我是{name}，今年{age}了";
// var result1=template1.format("loogn",22);
// var result2=template2.format({name:"loogn",age:22});
String.prototype.format = function(args) {
     var result = this;
     if (arguments.length > 0) {
         if (arguments.length == 1 && typeof (args) == "object") {
             for (var key in args) {
                 if(args[key]!=undefined){
                     var reg = new RegExp("({" + key + "})", "g");
                     result = result.replace(reg, args[key]);
                 }
             }
         }
         else {
             for (var i = 0; i < arguments.length; i++) {
                 if (arguments[i] != undefined) {
                     var reg = new RegExp("({)" + i + "(})", "g");
                     result = result.replace(reg, arguments[i]);
              }
           }
        }
    }
    return result;
}
//中文占两个字符，英文占1个字符
String.prototype.gblen = function() {
    var str = this;
    if(!str || !Tool.isStr(str)){
        return 0;
    }
    var beishu = 1;
    if(str.indexOf("layui-btn")>-1){
        beishu = 2;
    }
    str = str.replace(/<\/?.+?>/g,"");
    var len = 0;
    for (var i=0; i<str.length; i++) {
        if (str.charCodeAt(i)>127 || str.charCodeAt(i)==94) {
            len += 2;
        } else {
            len ++;
        }
    }
    return len * beishu;
}