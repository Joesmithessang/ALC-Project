package com.joesmith.devlist.data;


public class RepoLength {

    // No of repositories a developer has
    private static int length;

    // set the number when it queried by the QueryDevs class
    public static void setRepoLength(int len){
        length = len;
    }

    // return the number to the activity that requires it
    public static int getLength(){
        return length;
    }
}
