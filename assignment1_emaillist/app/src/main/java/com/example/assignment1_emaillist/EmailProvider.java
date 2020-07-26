package com.example.assignment1_emaillist;

import android.content.Context;
import android.widget.Button;

import java.util.ArrayList;

class AssignmentEmail{

    private String date;
    private String title;
    private String author;
    private String body;

    public AssignmentEmail(String date, String title, String author, String body){
        this.date = date;
        this.title = title;
        this.author = author;
        this.body = body;
    }

    public String getDate(){return date;}
    public String getTitle(){return title;}
    public String getAuthor(){return author;}
    public String getBody(){return body;}
}

public class EmailProvider {



    public static ArrayList<AssignmentEmail> getEmails(Context context){

        ArrayList<String[]> rawEmails = new ArrayList<>();
        rawEmails.add(context.getResources().getStringArray(R.array.email_0));
        rawEmails.add(context.getResources().getStringArray(R.array.email_1));
        rawEmails.add(context.getResources().getStringArray(R.array.email_2));
        rawEmails.add(context.getResources().getStringArray(R.array.email_3));
        rawEmails.add(context.getResources().getStringArray(R.array.email_4));
        rawEmails.add(context.getResources().getStringArray(R.array.email_5));
        rawEmails.add(context.getResources().getStringArray(R.array.email_6));
        rawEmails.add(context.getResources().getStringArray(R.array.email_7));
        rawEmails.add(context.getResources().getStringArray(R.array.email_8));
        rawEmails.add(context.getResources().getStringArray(R.array.email_9));
        rawEmails.add(context.getResources().getStringArray(R.array.email_10));
        rawEmails.add(context.getResources().getStringArray(R.array.email_11));
        rawEmails.add(context.getResources().getStringArray(R.array.email_12));
        rawEmails.add(context.getResources().getStringArray(R.array.email_13));
        rawEmails.add(context.getResources().getStringArray(R.array.email_14));
        rawEmails.add(context.getResources().getStringArray(R.array.email_15));
        rawEmails.add(context.getResources().getStringArray(R.array.email_16));
        rawEmails.add(context.getResources().getStringArray(R.array.email_17));
        rawEmails.add(context.getResources().getStringArray(R.array.email_18));
        rawEmails.add(context.getResources().getStringArray(R.array.email_19));
        rawEmails.add(context.getResources().getStringArray(R.array.email_20));

        ArrayList<AssignmentEmail> assignmentEmails = new ArrayList<>();
        for(String[] rawEmail: rawEmails)
            assignmentEmails.add(new AssignmentEmail(rawEmail[0],rawEmail[1],rawEmail[2],rawEmail[3]));

        return assignmentEmails;
    }



}