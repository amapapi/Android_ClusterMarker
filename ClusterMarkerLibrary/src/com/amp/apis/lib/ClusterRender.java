package com.amp.apis.lib;

import android.graphics.drawable.Drawable;

/**
 * @author yiyi.qi 聚合点的渲染规则
 */
public interface ClusterRender {
	/**
	 * 根据聚合点的元素数目返回渲染背景样式
	 * 
	 * @param clusterNum
	 * @return
	 */
	public Drawable getDrawAble(int clusterNum);
}
