package com.xuecheng.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDSL {

    @Autowired
    private RestHighLevelClient client;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void searchAllTest() throws IOException, ParseException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());//搜索全部
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});
        searchRequest.source(searchSourceBuilder);
        //执行搜索，向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        long totalHist = hits.getTotalHits();
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit : searchHits){
            String id = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String)sourceAsMap.get("name");
            String studymodel = (String)sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            Date timestamp = format.parse((String)sourceAsMap.get("timestamp"));
            System.out.println("name： " + name);
            System.out.println("studymodel： " + studymodel);
            System.out.println("price： " + price);
            System.out.println("timestamp： " + timestamp);
        }
    }

    @Test
    public void searchPageTest() throws IOException, ParseException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        int page = 1;
        int size = 1;
        searchSourceBuilder.from((page-1)*size);//记录下标
        searchSourceBuilder.size(size);
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());//搜索全部
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});
        searchRequest.source(searchSourceBuilder);
        //执行搜索，向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        long totalHist = hits.getTotalHits();
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit : searchHits){
            String id = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String)sourceAsMap.get("name");
            String studymodel = (String)sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            Date timestamp = format.parse((String)sourceAsMap.get("timestamp"));
            System.out.println("name： " + name);
            System.out.println("studymodel： " + studymodel);
            System.out.println("price： " + price);
            System.out.println("timestamp： " + timestamp);
        }
    }

    @Test
    public void searchTermqueryTest() throws IOException, ParseException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("name","spring"));//搜索全部
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});
        searchRequest.source(searchSourceBuilder);
        //执行搜索，向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        long totalHist = hits.getTotalHits();
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit : searchHits){
            String id = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String)sourceAsMap.get("name");
            String studymodel = (String)sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            Date timestamp = format.parse((String)sourceAsMap.get("timestamp"));
            System.out.println("name： " + name);
            System.out.println("studymodel： " + studymodel);
            System.out.println("price： " + price);
            System.out.println("timestamp： " + timestamp);
        }
    }

    @Test
    public void searchIDTermqueryTest() throws IOException, ParseException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        //搜索源构建对象
        String[] ids = {"1","3"};
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id",ids));//搜索全部
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});
        searchRequest.source(searchSourceBuilder);
        //执行搜索，向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        long totalHist = hits.getTotalHits();
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit : searchHits){
            String id = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String)sourceAsMap.get("name");
            String studymodel = (String)sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            Date timestamp = format.parse((String)sourceAsMap.get("timestamp"));
            System.out.println("name： " + name);
            System.out.println("studymodel： " + studymodel);
            System.out.println("price： " + price);
            System.out.println("timestamp： " + timestamp);
        }
    }

    @Test
    public void searchMatchqueryTest() throws IOException, ParseException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("description","spring开发框架")
                                    .minimumShouldMatch("10%")/*.operator(Operator.OR)*/);//搜索全部
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});
        searchRequest.source(searchSourceBuilder);
        //执行搜索，向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        long totalHist = hits.getTotalHits();
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit : searchHits){
            String id = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String)sourceAsMap.get("name");
            String studymodel = (String)sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            Date timestamp = format.parse((String)sourceAsMap.get("timestamp"));
            System.out.println("name： " + name);
            System.out.println("studymodel： " + studymodel);
            System.out.println("price： " + price);
            System.out.println("timestamp： " + timestamp);
        }
    }

    @Test
    public void searchMultiMatchqueryTest() throws IOException, ParseException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("spring开发框架","name","description")
                .minimumShouldMatch("10%")
                .field("name",10)/*.operator(Operator.OR)*/);//搜索全部
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});
        searchRequest.source(searchSourceBuilder);
        //执行搜索，向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        long totalHist = hits.getTotalHits();
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit : searchHits){
            String id = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String)sourceAsMap.get("name");
            String studymodel = (String)sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            Date timestamp = format.parse((String)sourceAsMap.get("timestamp"));
            System.out.println("name： " + name);
            System.out.println("studymodel： " + studymodel);
            System.out.println("price： " + price);
            System.out.println("timestamp： " + timestamp);
        }
    }

    @Test
    public void searchBoolqueryTest() throws IOException, ParseException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MultiMatchQueryBuilder field = QueryBuilders.multiMatchQuery("spring开发框架", "name", "description")
                .minimumShouldMatch("10%")
                .field("name", 10);

        TermQueryBuilder studymodel1 = QueryBuilders.termQuery("studymodel", "201001");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(field);
        boolQueryBuilder.must(studymodel1);

        searchSourceBuilder.query(boolQueryBuilder);//搜索全部
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});
        searchRequest.source(searchSourceBuilder);
        //执行搜索，向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        long totalHist = hits.getTotalHits();
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit : searchHits){
            String id = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String)sourceAsMap.get("name");
            String studymodel = (String)sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            Date timestamp = format.parse((String)sourceAsMap.get("timestamp"));
            System.out.println("name： " + name);
            System.out.println("studymodel： " + studymodel);
            System.out.println("price： " + price);
            System.out.println("timestamp： " + timestamp);
        }
    }

    @Test
    public void searchBoolFilterqueryTest() throws IOException, ParseException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MultiMatchQueryBuilder field = QueryBuilders.multiMatchQuery("spring开发框架", "name", "description")
                .minimumShouldMatch("10%")
                .field("name", 10);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(field);

        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel","201001"));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").lte(100).gte(60));

        searchSourceBuilder.query(boolQueryBuilder);//搜索全部
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});
        searchRequest.source(searchSourceBuilder);
        //执行搜索，向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        long totalHist = hits.getTotalHits();
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit : searchHits){
            String id = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String)sourceAsMap.get("name");
            String studymodel = (String)sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            Date timestamp = format.parse((String)sourceAsMap.get("timestamp"));
            System.out.println("name： " + name);
            System.out.println("studymodel： " + studymodel);
            System.out.println("price： " + price);
            System.out.println("timestamp： " + timestamp);
        }
    }

    @Test
    public void searchBoolOrderqueryTest() throws IOException, ParseException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").lte(100).gte(60));

        searchSourceBuilder.query(boolQueryBuilder);//搜索全部

        //排序
        searchSourceBuilder.sort("studymodel", SortOrder.DESC);
        searchSourceBuilder.sort("price",SortOrder.ASC);

        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});
        searchRequest.source(searchSourceBuilder);
        //执行搜索，向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        long totalHist = hits.getTotalHits();
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit : searchHits){
            String id = hit.getId();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String)sourceAsMap.get("name");
            String studymodel = (String)sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            Date timestamp = format.parse((String)sourceAsMap.get("timestamp"));
            System.out.println("name： " + name);
            System.out.println("studymodel： " + studymodel);
            System.out.println("price： " + price);
            System.out.println("timestamp： " + timestamp);
        }
    }

    @Test
    public void searchHighlightTest() throws IOException, ParseException {
        //搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("description","spring开发框架")
                .minimumShouldMatch("10%")/*.operator(Operator.OR)*/);//搜索全部
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","timestamp"},new String[]{});


        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("description"));
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);
        //执行搜索，向ES发起http请求
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        long totalHist = hits.getTotalHits();
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit : searchHits){
            String id = hit.getId();
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField name = highlightFields.get("description");
            if(name != null){
                Text[] fragments = name.getFragments();
                for(Text t : fragments){
                    System.out.println(t.toString());
                }
                System.out.println();
            }
        }
    }
}
