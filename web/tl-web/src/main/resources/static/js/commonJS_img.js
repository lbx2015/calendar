//获得img标签加载的图片点击需要显示的宽高(标签的id)
function getImgHeightAndWidth(id){
	var myimage = document.getElementById(id);
	var imgInfo={};
    if (myimage.naturalWidth==null || typeof myimage.naturalWidth == "undefined") {
        　　	 // IE 6/7/8
	        　　 var img = new Image();
	        　　 img.src = myimage.src;
	        　　  imgInfo.width = img.width;
	      imgInfo.height= img.height;
    }else {
	        　　  // HTML5 browsers
	      imgInfo.width = myimage.naturalWidth;
		  imgInfo.height= myimage.naturalHeight; 
    }
    //添加方法调整宽高
    var newImgInfo = adjustImgInfo(800,0,imgInfo);
    return newImgInfo;
}
//调整显示图片的大小(maxWidth:显示最大的宽,maxHeight:显示最大的高,imgInfo:显示图片的信息)
function adjustImgInfo(maxWidth,maxHeight,imgInfo){
	var hRatio;
	var wRatio;
	var Ratio = 1;
	var w = imgInfo.width;
	var h = imgInfo.height;
	wRatio = maxWidth / w;
	hRatio = maxHeight / h;
	if (maxWidth ==0 && maxHeight==0){
	Ratio = 1;
	}else if (maxWidth==0){//
	if (hRatio<1) Ratio = hRatio;
	}else if (maxHeight==0){
	if (wRatio<1) Ratio = wRatio;
	}else if (wRatio<1 || hRatio<1){
	Ratio = (wRatio<=hRatio?wRatio:hRatio);
	}
	if (Ratio<1){
	w = w * Ratio;
	h = h * Ratio;
	}
	//压缩之后的宽高
	var newImgInfo = {};
	newImgInfo.height = h;
	newImgInfo.width = w;
	return newImgInfo;
}

//点击放大图片(需要在页面中加一个id为:displayimg的img标签,点击的img标签(只能是img标签)需要设置id属性并且将id值作为该方法的第一个属性)
function scanImg(name, url) {
    $("#displayimg").attr("src", '/images/img_default.png');
    var imgInfo = getImgHeightAndWidth(name);
    var height = imgInfo.height;
    var width =  imgInfo.width;
    //设置显示组件的宽高
    $('#displayimg').css("width",width);
    $('#displayimg').css("height",height);
    layer.open({
    	  type: 1,
    	  title: false,
    	  closeBtn: 0,
    	  area: [width + 'px', height + 'px'],
    	  skin: 'layui-layer-nobg', //没有背景色
    	  shadeClose: true,
    	  content: $('#displayimg')
    	});
}
