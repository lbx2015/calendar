package com.riking.calendar.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZZW on 09/01/17.
 */

public class MultipleResource {

    @SerializedName("page")
    public Integer page;
    @SerializedName("per_page")
    public Integer perPage;
    @SerializedName("total")
    public Integer total;
    @SerializedName("total_pages")
    public Integer totalPages;
    @SerializedName("data")
    public List<Datum> data = new ArrayList<>();

    public class Datum {

        @SerializedName("userId")
        public Integer id;
        @SerializedName("name")
        public String name;
        @SerializedName("year")
        public Integer year;
        @SerializedName("pantone_value")
        public String pantoneValue;

    }
}
