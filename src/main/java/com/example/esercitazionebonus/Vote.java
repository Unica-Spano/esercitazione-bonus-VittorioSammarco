package com.example.esercitazionebonus;

import java.io.Serializable;
import java.util.Calendar;

public class Vote implements Serializable {
    private String exam;
    private int vote, credits;

    public String getExam() {return exam;}
    public int getVote() {return vote;}
    public int getCredits() {return credits;}
    public void setExam(String exam) {this.exam = exam;}
    public void setVote(int vote) {this.vote = vote;}
    public void setCredits(int credits) {this.credits = credits;}
}