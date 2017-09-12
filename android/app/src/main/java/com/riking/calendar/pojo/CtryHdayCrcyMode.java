package com.riking.calendar.pojo;

import java.util.ArrayList;

public class CtryHdayCrcyMode {
    public ArrayList<CtryHdayCrcy> content;
    public byte totalPages;
    public int totalElements;
    public boolean last;

    @Override
    public String toString() {
        return "CtryHdayCrcyMode{" +
                "content=" + content +
                '}';
    }
}
