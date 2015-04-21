package org.es_process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.entity.Document;

public class Search {

	private static Client client;

	public static List<Document> doSearch(String query_input) {
		// 连接elasticsearch
		client = new TransportClient()
				.addTransportAddress(new InetSocketTransportAddress(
						"127.0.0.1", 9300));
		String indexname = "yustsearch_ik";
		String type = "yust_demo";
		String query = query_input;

		List<Document> result = searcher(query, indexname, type);
		return result;
	}

	public static List<Document> searcher(String query, String indexname,
			String type) {
		List<Document> list = new ArrayList<Document>();
		// 搜多的类型是DEFAULT,高亮的Filed是content，搜索大小是50
		SearchResponse searchResponse = client.prepareSearch(indexname)
				.setTypes(type).setSearchType(SearchType.DEFAULT)
				.setQuery(QueryBuilders.queryString(query))
				.addHighlightedField("content").setSize(50).execute()
				.actionGet();

		SearchHits hits = searchResponse.getHits();
		System.out.println("查询到记录数=" + hits.getTotalHits());
		if (hits.getTotalHits() > 0) {
			for (SearchHit hit : hits) {
				Map<String, HighlightField> result = hit.highlightFields();
				Integer id = (Integer) hit.getSource().get("id");
				String url = (String) hit.getSource().get("url");
				// String content = (String) hit.getSource().get("content");
				String content = result.get("content").toString();
				list.add(new Document(id, url, content));
			}
		}
		return list;
	}
}
