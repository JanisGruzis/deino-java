package com.deino.common;

import java.text.SimpleDateFormat;

/**
 * Created by Inwhite on 12.04.2015..
 */
public abstract class CPSBase {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public abstract String toXML();
}
