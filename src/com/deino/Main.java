package com.deino;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
	   DelfiParser test =  new DelfiParser();
        HashMap<String ,Article> result = test.getMessages();
    }
}
