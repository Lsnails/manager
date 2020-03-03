package com.base.modules.business.system.datainfo.entity;

import java.util.List;

public class EchartsBar {
	private List<String> xAxisData; // 横坐标
	private List<String> seriesData; //纵坐标值
	public List<String> getxAxisData() {
		return xAxisData;
	}
	public void setxAxisData(List<String> xAxisData) {
		this.xAxisData = xAxisData;
	}
	public List<String> getSeriesData() {
		return seriesData;
	}
	public void setSeriesData(List<String> seriesData) {
		this.seriesData = seriesData;
	}
	
}
