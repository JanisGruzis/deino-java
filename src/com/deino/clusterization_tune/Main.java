package com.deino.clusterization_tune;

import com.clusterpoint.api.CPSOrder;
import com.clusterpoint.api.request.CPSSearchRequest;
import com.clusterpoint.api.response.CPSSearchResponse;
import com.deino.article_reader.Article;
import com.deino.article_reader.NLP;
import com.deino.common.CPSBase;
import com.deino.common.Database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Created by Rob on 17.04.2015..
 */
public class Main {

    public static List<SimilarityResult> getSimilaritResults(CPSSearchRequest request)
    {
        ArrayList<SimilarityResult> result=new ArrayList<SimilarityResult>();

        List<Article> articles=Database.getArticles(request);
        List<Article> similar;
        for(Article article : articles)
        {
            similar=Database.getSimilarArticles(article);
            if(similar==null)
                continue;
            for(Article similar_article : similar)
            {
                result.add(new SimilarityResult(
                                article,
                                similar_article,
                                NLP.cosineSimilarity(article.getKeywords(),similar_article.getKeywords()
                                )));
            }
        }

        Collections.sort(result,Collections.reverseOrder());

        return result;
    }

    public static void main(String[] args) throws Exception {



        CPSSearchRequest req=new CPSSearchRequest(
                String.format("<type>article</type>")
                ,0,3000);
        req.setOrdering("<date><date>descending</date></date>");
        List<SimilarityResult> result=getSimilaritResults(req);

        BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("similarity_result.txt"))));

        for(SimilarityResult item : result)
        {
            out.write(Double.toString(item.similarity));
            out.write("\n");
            out.write(item.article1.getTitle());
            out.write("\n");
            out.write(item.article1.getKeywords().toString());
            out.write("\n");
            out.write(item.article1.getURL());
            out.write("\n");
            out.write(item.article1.getDescription());
            out.write("\n\n");
            out.write(item.article2.getTitle());
            out.write("\n");
            out.write(item.article2.getKeywords().toString());
            out.write("\n");
            out.write(item.article2.getURL());
            out.write("\n");
            out.write(item.article2.getDescription());
            out.write("\n---------------------------------------\n");
        }

        out.close();

    }

    private static class SimilarityResult implements Comparable<SimilarityResult> {
        public final Article article1;
        public final Article article2;
        public final Double similarity;

        public SimilarityResult(Article article1, Article article2, Double similarity) {

            this.article1 = article1;
            this.article2 = article2;
            this.similarity = similarity;
        }

        @Override
        public int compareTo(SimilarityResult o) {
            return Double.compare(similarity,o.similarity);
        }
    }
}
