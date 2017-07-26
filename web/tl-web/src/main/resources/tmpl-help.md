##模板使用文档

###一、json配置
json配置文件统一存放在 api项目"static\dict\"路径下(.json)
####格式如下
```javascript
{
	"table":"T_EMPLOYEE", //业务表名，此属性无需与数据表中对应
	"groups":[{           //属性分组，编辑模板中会根据此分组显示
		"prop":"base",     
		"alias":"基本信息"
	},{
		"prop":"family",
		"alias":"家庭"
	},{
		"prop":"demo",
		"alias":"demo"
	}],
	"attrs": [            
		{
			"prop": "name",    //属性标识
			"alias": "姓名",   //属性显示名
			"type": "string",  //属性类型
			"query": "1",      //属否为搜索条件
			"query_order": "2",  //搜索条件顺序
			"group":"base",     //所属分组
			"verify":"required"， //校验，可填写多个
			“verify_fun":"if($birthday=""){ return required}" //带条件校验
		},
		{
			"prop": "birthday",
			"alias": "出生日期",
			"type": "date",
			"query": "1",
			"query_order": "1",
			"desc":"员工出生日期",
			"group":"base",
			"hidden":"edit,table", //需要隐藏的属性，分别可在相应的模板中隐藏
			"editable":2 ,        //不可编辑 ，默认为1
			"verify":"required,number"， //校验，可填写多个
		}
	]
}
```


###二、模板编写
####1.现有模板 分别有表格(table.html)、 预览(preview.html)、搜索条件（filterCriteria.html）、编辑（edit.html)、翻页（pages.html）
模板存放在web项目"public\common\tmpls\"路径下

####引用方法格式如下，在html中对应位置添加div标签
```html
<div class="_tmpl" data-url="filterCriteria.html" data-attr="employee.json"  data-bind="filterCriteriaOnBind" data-param=""></div>
<div class="_tmpl" data-url="table.html" id="table" data-attr="employee.json"  data-bind="tbOnBind" data-param="pindex=0&pcount=10"></div>
<div class="_tmpl" data-url="preview.html" data-attr="employee.json"  data-bind="previewOnBind" data-param=""></div>
<div class="_tmpl" data-url="pages.html" data-depend="table" data-attr="" data-bind="TmplUtils.pagesOnBind" data-param=""></div>
```
* data-url 模板地址
* data-attr json文件名
* data-bind 数据绑定方法
* data-param 数据绑定所需参数
* data-depend 所依赖模板 所依赖的模板必须设置相应的id属性


###二、数据绑定方法编写
页面加载模板前会调用"data-bind"属性所绑定的js方法
####示例
```javascript
	//搜索条件数据加载绑定
	function filterCriteriaOnBind(attrs,param){
	    tmpl = this;
	    var _this = this;
	    tmpl.bind(function (callback) {
	        var data={};
	        //查询按钮点击事件
	        data.searchClick = "search";
	        callback.call(_this, data);
	        TmplUtils.filterCriteriaOnLoad();
	    });
	}

	//表格模板数据加载绑定
	var tbTmpl；
	function tbOnBind(attrs,param){
        tmpl = this;
        tbTmpl = tmpl;
        tmpl.bind(function(callback) {
            var url = Const.apiUrl+"/employees/getMore";
            var _this = this;
            View.get(url,param,function(resp){
                var data={};
                if(resp){
                    data.data = resp;
                    TmplUtils.totalPages = data.data.totalPages;
                }
                //是否需要全选checkbox
                data.hasCheckbox = true;
                //是否有操作列
                data.hasOperation = true;
                //操作按钮
                data.operations=[{value:"修改",onclick:"edit",class:"layui-btn layui-btn-mini layui-btn-normal"},{value:"禁用",onclick:"forbbit",class:"layui-btn layui-btn-mini layui-btn-danger"}];
                //tr的点击事件绑定，该方法会传入item参数，不绑定改事件无预览效果
                data.trClick = "bindPro";
                //设置tb宽度，根据实际情况配置
				data.tdLeftWidth = [10,20,30,10];
                data.tdRightWidth = [30,20,50];
				//如果要修改表格内容 ，无需修改的忽略此项
                data.tdBeforeFill = function(obj){
                   //obj = {item,prop,html,row,col};
					//item 当前行数据
					//prop 当前单元格属性
					//html 当前单元格html内容
					//row 当前行数
					//col 当前列数
					if(obj.prop == "sex"){
					    var html = "<a>" +obj.html+"</a>";
						return html;
					}
	/*				if(obj.row == 3 || obj.col == 2){
					    return "<span style='color:red;'>"+"wwww"+"</span>";
					}*/
					return obj.html;
				}
                //操作栏的宽度
                data.operationWidth = 20;
                callback.call(_this, data);
                TmplUtils.pageTmpl.refresh();
                //
                TmplUtils.tableOnLoad();
			},function(code) {
                layer.msg('查询出错', {icon: 5});
            });
        });
    }



	//预览模板数据加载绑定
    var previewTmpl;
    function previewOnBind(attrs,param) {
        tmpl = this;
        previewTmpl = tmpl;
        if(!item){
            return;
        }
        tmpl.bind(function(callback) {
            var url = Const.apiUrl+"/employees/get";
			var data={};
			//是否可编辑
			data.canEdit = true;

			data.data = item;

			//绑定点击事件
			data.saveClick = "save";

			callback.call(this, data);
			TmplUtils.previewOnLoad();
        });
    }
```
####其余相关
```javascript
     //表格单元格点击事件
    function bindPro(item){
        if(!TmplUtils.isEditing){
            this.item = item;
            previewTmpl.refresh();
        }
    }

    //查询
    function search(){
        TmplUtils.searchParam = Utils.formToStr("filterCriteria");
        tbTmpl.refresh("pindex=0"+"&pcount="+ TmplUtils.pageSize +TmplUtils.searchParam);
        TmplUtils.currPage = 1;
        TmplUtils.closePreview();
    }


	//保存
    function save() {
        var url = Const.apiUrl + "/employees/addOrUpdate";
        var employee = Utils.formToObj("preview");
        View.post(url,employee,function(resp){
            TmplUtils.showMsgSuccess('编辑成功');
            tbTmpl.refresh("pindex=" + (TmplUtils.currPage - 1) + "&pcount=" +  TmplUtils.pageSize + TmplUtils.searchParam);
            TmplUtils.closePreview();
        },function (){
        	TmplUtils.showMsgFail('编辑失败');
        })
    }

    	//弹出添加iframe
     function edit(id){
        layer.open({
            type: 2,
            title: '添加数据',
            shadeClose: true,
            shade: false,
            maxmin: false, //开启最大化最小化按钮
            area: ['100%', 'auto'],
            offset: '0px',
            move: false,
            content: 'editDialog.html#'+id //iframe的url
        });
        parent.startInit(document.getElementsByTagName("iframe")[0], 560);
    }
```

###三、校验
####目前自带的校验有 required（必填项）、phone（手机号）、email（邮箱）、url（链接）、number（数字）、date（日期）、identity（身份证号）
####可自定义校验
####示例
	//自定义前段校验规则，可以多个
    TmplUtils.verify.desc=function(value){
        if(value.length < 5){
            return '描述至少得5个字符啊';
        }
    };


###四、TmplUtils封装属性
	isEditing//预览的编辑状态  编辑中为true
	pageSize
    totalPage
    currPage
    searchParam
    pageTmpl
    verify
    
json配置attrs新增属性"enum_url": "/employees/getDemoEnum"  配置自定义的下拉框数据
新增属性"enum_search":"1",  此下拉框可搜索




    
