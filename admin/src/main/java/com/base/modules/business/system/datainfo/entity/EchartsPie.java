package com.base.modules.business.system.datainfo.entity;

import java.util.List;

public class EchartsPie {
	private List<String> legeneData; //横坐标
	private List<PieData> seriesData; // 数据表示
	public List<String> getLegeneData() {
		return legeneData;
	}
	public void setLegeneData(List<String> legeneData) {
		this.legeneData = legeneData;
	}
	public List<PieData> getSeriesData() {
		return seriesData;
	}
	public void setSeriesData(List<PieData> seriesData) {
		this.seriesData = seriesData;
	}
	
}
