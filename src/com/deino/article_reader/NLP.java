package com.deino.article_reader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Rob on 11.04.2015..
 */
public class NLP {

    private static HashMap<String,Double> idf;
    private static double threshold=0.8;
    private static double default_idf=1000;
    static
    {
        idf=new HashMap<>();
        try {
            Scanner in=new Scanner(new InputStreamReader(new FileInputStream("idf.txt")));
            while(in.hasNext())
            {
                idf.put(in.next(),in.nextDouble());
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param text
     * @return tokens
     */
    public static List<String> tokenize(String text)
    {
        ArrayList<String> tokens=new ArrayList<>();
        for(String token : text.split("[^\\p{L}0-9]"))
        {
            if(token.length()==0)
                continue;
            token=token.toLowerCase();
            tokens.add(token);
        }

        return tokens;
    }

    /**
     * @param text
     * @return Top keywords by tf-idf
     */
    public static TreeMap<String,Double> getTopTokens(String text)
    {
        HashMap<String,Integer> uniq_tokens = new HashMap<>();

        List<String> tokens= tokenize(text);
        Integer tmp;

        for(String token : tokens)
        {
            if((tmp=uniq_tokens.get(token))==null)
            {
                tmp=0;
            }
            tmp++;
            uniq_tokens.put(token,tmp);
        }

        TreeMap<String,Double> relative_tokens=new TreeMap<>();
        double n=uniq_tokens.size();
        Double idf;
        for (Map.Entry<String,Integer> entry : uniq_tokens.entrySet())
        {
            idf=NLP.idf.get(entry.getKey());
            if(idf==null)
                idf=default_idf;
            relative_tokens.put(entry.getKey(), entry.getValue() / n * idf);
        }

        return relative_tokens;
    }

    public static boolean isEqual(HashMap<String, Double> a, HashMap<String, Double> b) {
        return threshold<cosineSimilarity(a,b);
    }

    public static Double cosineSimilarity(HashMap<String, Double> a, HashMap<String, Double> b)
    {
        double multiplication=0;
        double a_score=0;
        double b_score=0;


        Double tmp;
        for(Map.Entry<String,Double> entry : a.entrySet())
        {
            if((tmp=b.get(entry.getKey()))!=null)
            {
                multiplication+=entry.getValue()*tmp;
            }
        }

        for(Map.Entry<String,Double> entry : a.entrySet())
        {
            a_score+=entry.getValue()*entry.getValue();
        }

        for(Map.Entry<String,Double> entry : b.entrySet())
        {
            b_score+=entry.getValue()*entry.getValue();
        }

        double divider=Math.sqrt(a_score*b_score);

        return (divider==0 ? 0 : multiplication/divider);
    }
}
