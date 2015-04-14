package com.deino.article_reader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Rob on 11.04.2015..
 */
public class NLP {

    private static HashMap<String, Double> idf;
    private static double threshold=0.8;
    private static double default_idf = 1000;

    static {
        idf = new HashMap<>();
        try {
            Scanner in = new Scanner(new InputStreamReader(new FileInputStream("words.tsv")));
            int count = Integer.valueOf(in.next());
            default_idf = Math.log(count);// / Math.log(2);       // thats why: http://stackoverflow.com/questions/13831150/logarithm-algorithm
            System.out.println(default_idf);
            while (in.hasNext()) {
                String key = in.next();
                double rate = count / in.nextDouble();
                rate = Math.log(rate);// / Math.log(2);
                idf.put(key, rate);
                //System.out.println(key + " : " + rate);
            }
            in.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @param text
     * @return tokens
     */
    public static List<String> tokenize(String text) {
        ArrayList<String> tokens = new ArrayList<>();
        for (String token : text.split("[^\\p{L}0-9]")) {
            if (token.length() == 0)
                continue;
            token = token.toLowerCase();
            tokens.add(token);
        }

        return tokens;
    }

    /**
     * @param text
     * @return Top keywords by tf-idf
     */
    public static ArrayList<Token> getTopTokens(String text) {
        HashMap<String, Integer> uniq_tokens = new HashMap<>();

        List<String> tokens = tokenize(text);
        Integer tmp;

        for (String token : tokens) {
            if ((tmp = uniq_tokens.get(token)) == null) {
                tmp = 0;
            }
            tmp++;
            uniq_tokens.put(token, tmp);
        }

        ArrayList<Token> sorted_tokens = new ArrayList<>();
        double n = uniq_tokens.size();
        Double idf;

        for (Map.Entry<String, Integer> entry : uniq_tokens.entrySet()) {
            idf = NLP.idf.get(entry.getKey());
            if (idf == null)
                idf = default_idf;
            sorted_tokens.add(new Token(entry.getKey(), entry.getValue() / n * idf));
        }

        Collections.sort(sorted_tokens,Collections.reverseOrder());

        return sorted_tokens;
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

