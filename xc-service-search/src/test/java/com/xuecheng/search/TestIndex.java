package com.xuecheng.search;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestIndex {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private RestClient restClient;

    @Test
    public void createIndexTest() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
        //分片、副本
        createIndexRequest.settings(Settings.builder().put("number_of_shards","1").put("number_of_replicas","0"));
        //指定映射
        createIndexRequest.mapping("doc","{\n" +
                "\t\"properties\":{\n" +
                "\t\t\"studymodel\":{\n" +
                "\t\t\t\"type\":\"keyword\"\n" +
                "\t\t},\n" +
                "\t\t\"name\":{\n" +
                "\t\t\t\"type\":\"keyword\"\n" +
                "\t\t},\n" +
                "\t\t\"description\":{\n" +
                "\t\t\t\"type\":\"text\",\n" +
                "\t\t\t\"analyzer\":\"ik_max_word\",\n" +
                "\t\t\t\"search_analyzer\":\"ik_smart\"\n" +
                "\t\t},\n" +
                "\t\t\"pic\":{\n" +
                "\t\t\t\"type\":\"text\",\n" +
                "\t\t\t\"index\":false\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}", XContentType.JSON);
        IndicesClient indices = restHighLevelClient.indices();
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest);
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }

    @Test
    public void deleteIndexTest() throws IOException {
        //删除索引对象
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_course");
        //操作索引的客户端
        IndicesClient indices = restHighLevelClient.indices();
        //执行
        DeleteIndexResponse delete = indices.delete(deleteIndexRequest);
        boolean acknowledged = delete.isAcknowledged();
        System.out.println(acknowledged);
    }

    @Test
    public void addDoc() throws IOException {
        //PUT http://localhost:9200/xc_course/doc/id  -- 无id，则用post，id自动生成
        //{index}/{type}/{id}
        /**
         {
           "name":"spring cloud实战",
           "description":"本课程主要从四个章节进行讲解",
           "studymodel":"201001"
         }
         */
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("name","spring cloud实战");
        jsonMap.put("description","本课程主要从四个章节进行讲解：1.微服务架构入门 2.spring cloud基础入门 3.实战spring boot 4.注册中心eureka。");
        jsonMap.put("studymodel","201001");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        jsonMap.put("timestamp",dateFormat.format(new Date()));
        jsonMap.put("price",5.6f);
        //创建索引请求对象
        IndexRequest indexRequest = new IndexRequest("xc_course","doc");
        indexRequest.source(jsonMap);
        IndexResponse index = restHighLevelClient.index(indexRequest);
        DocWriteResponse.Result result = index.getResult();
        System.out.println(result);
    }

    @Test
    public void getDoc() throws IOException {
        //GET http://localhost:9200/xc_course/doc/id
        //{index}/{type}/{id}
        GetRequest getRequest = new GetRequest("xc_course","doc","q0WSL3ABIu4XLVqGzDCj");
        GetResponse getResponse = restHighLevelClient.get(getRequest);
        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        System.out.println(sourceAsMap);
    }

    @Test
    public void updateDoc() throws IOException {
        //POST http://localhost:9200/xc_course/doc/id --- 完全替换
        //POST http://localhost:9200/xc_course/doc/id/_update  ---局部更新
        //{"doc":{"name":"bootstrap"}}
        //{index}/{type}/{id}
        UpdateRequest updateRequest = new UpdateRequest("xc_course","doc","q0WSL3ABIu4XLVqGzDCj");
        Map<String, Object> json = new HashMap<>();
        json.put("name","Java Developer!!!");
        updateRequest.doc(json);
        UpdateResponse update = restHighLevelClient.update(updateRequest);
        DocWriteResponse.Result result = update.getResult();
        System.out.println(result);
    }

    @Test
    public void deleteDoc() throws IOException {
        //DELETE /{index}/{type}/{id} --id删除
        //POST   http://localhost:9200/xc_course/doc/_delete_by_query
        //{
        //	"query":{
        //		"term":{
        //			"name":"bootstrap"
        //		}
        //	}
        //}
        DeleteRequest deleteRequest = new DeleteRequest("xc_course","doc","rUWsL3ABIu4XLVqG7jBu");
        DeleteResponse delete = restHighLevelClient.delete(deleteRequest);
        DocWriteResponse.Result result = delete.getResult();
        System.out.println(result);
    }
}
