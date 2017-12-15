package com.riking.calendar.pojo.params;


/**
 * 问题回答类的接收参数
 * 
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class QAnswerParams   extends BaseParams {

	// 问题回答ID
	public String questAnswerId;

	// 内容
	public String content;

	// 1-点赞；2-收藏
	public int optType=1;

	// 0-取消；1-赞同/收藏
	public int enabled;

}
