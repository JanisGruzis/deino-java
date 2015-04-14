package com.deino.article_reader;

public class Token implements Comparable<Token>
{
    public final String token;
    public final double value;

    Token(String token, double value) {
        this.token = token;
        this.value = value;
    }

    @Override
    public int compareTo(Token o) {
        return Double.compare(value,o.value);
    }

    @Override
    public String toString() {
        return "@Token["+token+", "+Double.toString(value)+"]";
    }
}
