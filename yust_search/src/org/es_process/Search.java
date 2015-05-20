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
	private static String INDEX_NAME = "testframe";
	private static String TYPE = "test";
	private static String IP_ADDRESS = "127.0.0.1";
	private static String PORT = "9300";
	private static int SEARCH_SIZE = 50;

	public static List<Document> doSearch(String query_input) {

		// 连接elasticsearch
		client = new TransportClient()
				.addTransportAddress(new InetSocketTransportAddress(IP_ADDRESS,
						Integer.valueOf(PORT)));
		String query = query_input;

		List<Document> result = searcher(query, INDEX_NAME, TYPE);
		return result;
	}

	public static List<Document> searcher(String query, String indexname,
			String type) {
		List<Document> list = new ArrayList<Document>();
		// 搜多的类型是DEFAULT,高亮的Filed是content，搜索大小是50
		SearchResponse searchResponse = client.prepareSearch(indexname)
				.setTypes(type).setSearchType(SearchType.DEFAULT)
				.setQuery(QueryBuilders.queryString(query))
				.addHighlightedField("content").setSize(SEARCH_SIZE).execute()
				.actionGet();

		SearchHits hits = searchResponse.getHits();
		// System.out.println("查询到记录数=" + hits.getTotalHits());
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
