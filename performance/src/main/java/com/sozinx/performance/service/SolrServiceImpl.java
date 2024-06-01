package com.sozinx.performance.service;

import com.sozinx.performance.entity.News;
import com.sozinx.performance.repository.NewsJpaRepository;
import jakarta.annotation.Resource;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.sozinx.performance.utils.Constants.MAX_RESULTS_IN_SEARCH;

@Service
public class SolrServiceImpl implements SolrService{

    @Value(value = "${solr.solr-host}")
    private String solrHost;
    @Resource
    private NewsJpaRepository newsJpaRepository;

    @Override
    public void indexDataFromDataBase() {
        try (SolrClient solrClient = new Http2SolrClient.Builder(solrHost).build()) {
            SolrInputDocument doc = new SolrInputDocument();
            for(int i = 300; i < 518; i++) {
                for(News news: newsJpaRepository.getAll(PageRequest.of(i, 1000))) {
                    doc.addField("id", news.getId());
                    doc.addField("name", news.getName());
                    doc.addField("content", news.getContent());
                    solrClient.add(doc);
                    doc.clear();
                }
                System.out.println("Added successfully: " + i);
            }
            solrClient.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<String> searchByString(String value) {
        try (SolrClient solrClient = new Http2SolrClient.Builder(solrHost).build()) {
            SolrQuery query = new SolrQuery();
            query.set("defType", "edismax");
            query.set("q", value);
            query.add("qf", "name^2 content");
            query.add("pf", "name^2 content");
            query.set("fl", "*,score");
            query.setRows(MAX_RESULTS_IN_SEARCH);
            QueryResponse response = solrClient.query(query);
            SolrDocumentList results = response.getResults();
            List<String> identifiers = new ArrayList<>();
            for (org.apache.solr.common.SolrDocument result : results) {
                identifiers.add(result.getFieldValue("id").toString());
            }
            return identifiers;
        } catch (Exception e) {
            return null;
        }
    }
}
