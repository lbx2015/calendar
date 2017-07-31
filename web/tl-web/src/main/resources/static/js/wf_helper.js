var FlowHelper = {
	openCommentsBox : function(opts, callback) {
		opts = opts || {};
		opts.width = opts.width || '420px';
		opts.height = opts.height || '240px';
		opts.title = opts.title || '请填写说明';
		var html = '<div><textarea id="wf_comments" class="layui-textarea"></textarea></div>';
		layer.confirm(html, {
			btn : [ '提交', '取消' ],
			area : [ opts.width, opts.height ],
			title : opts.title
		}, function() {
			var comments = $("#wf_comments").val();
			if(!!comments){
			    callback.call(window, comments);
			}else{
			    TmplUtils.showMsgFail('不通过原因不可为空');
			}
		}, function() {
		});
	}
}
