package com.example.androidstudytutorial.model;

import java.util.List;

public class ListDescx {

    List<Images> descxList ;
    private static ListDescx single_instance = null;

    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private ListDescx()
    {

    }

    // Static method
    // Static method to create instance of Singleton class
    public static ListDescx getInstance()
    {
        if (single_instance == null)
            single_instance = new ListDescx();

        return single_instance;
    }
    public List<Images> getDescxList() {
        return descxList;
    }

    public void setDescxList(List<Images> descxList) {
        this.descxList = descxList;
    }
}
