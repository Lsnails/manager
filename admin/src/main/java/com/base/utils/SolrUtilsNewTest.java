package com.base.utils;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;

public class SolrUtilsNewTest {
	public static void main(String[] args) {
		//获取链接 本地solr访问地址，ip：端口号/solr/模块名
		String url="http://127.0.0.1:8983/solr/xgglxy_core";
		HttpSolrClient httpSolrClient = new HttpSolrClient(url);
		httpSolrClient.setParser(new XMLResponseParser());//设置响应解析器
		httpSolrClient.setConnectionTimeout(500);//建立链接的最大时常
		SolrQuery solrQuery = new SolrQuery();//新建查询
		solrQuery.setQuery("keyword:網絡");
		solrQuery.addFilterQuery("id:65 or id:66");
		solrQuery.setStart(0);
		solrQuery.setRows(50);
		try {
			QueryResponse queryResponse = httpSolrClient.query(solrQuery);
			//获取过了后总记录数据
			long totalRow = Long.valueOf(queryResponse.getResults().getNumFound()).intValue();
			System.out.println("总记录数："+totalRow);
			List<SolrSerachVoTest> solrSerachVoList = queryResponse.getBeans(SolrSerachVoTest.class);
			if(solrSerachVoList!=null){
				System.out.println("过滤记录数据："+solrSerachVoList.size());
			}
			
			for (SolrSerachVoTest solrSerachVo : solrSerachVoList) {
				System.out.println("-----------------------");
				System.out.println("id:"+solrSerachVo.getId());
				System.out.println("productName:"+solrSerachVo.getProductName());
				System.out.println("cityName:"+solrSerachVo.getCityName());
				System.out.println("productDes:"+solrSerachVo.getProductDes());
			}
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
