package com.xp.queszone.service;

import com.xp.queszone.model.Question;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    private static final String SOLR_URL = "http://你的主机ip和端口号";
    private static final String QUESTION_TITLE_FIELD = "question_title";
    private static final String QUESTION_CONTENT_FIELD = "question_content";
    private HttpSolrClient httpSolrClient = new HttpSolrClient.Builder(SOLR_URL).build();

    public List<Question> searchQuestion(String keyword, int offset, int count,
                                         String hlPre, String hlPos) {

        List<Question> questionList = new ArrayList<>();
        SolrQuery query = new SolrQuery();
        String qkey = "question_title:"+keyword+" OR question_content:"+keyword;
        query.set("q",qkey);
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);
        query.setHighlightSimplePre(hlPre);
        query.setHighlightSimplePost(hlPos);
        query.set("h1.f1", QUESTION_CONTENT_FIELD+","+QUESTION_TITLE_FIELD);
        QueryResponse response = null;
        try {
            response = httpSolrClient.query(query);
        }catch (Exception e) {
             System.out.println(e.getMessage());
             e.printStackTrace();
        }

        for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
            Question question = new Question();
            question.setId(Integer.parseInt(entry.getKey()));
            if (entry.getValue().containsKey(QUESTION_CONTENT_FIELD)) {
                List<String> contentList = entry.getValue().get(QUESTION_CONTENT_FIELD);
                if (contentList.size() > 0 ) {
                    question.setContent(contentList.get(0));
                }
            }
            if (entry.getValue().containsKey(QUESTION_TITLE_FIELD)) {
                List<String> titleList = entry.getValue().get(QUESTION_CONTENT_FIELD);
                if (titleList.size() > 0 ) {
                    question.setTitle(titleList.get(0));
                }
            }
            questionList.add(question);
        }
        return questionList;
    }
}
