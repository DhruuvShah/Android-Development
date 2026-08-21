package com.northq.learninghub;

public class Expense {
    public long id;
    public String title;
    public String date;
    public double amount;
    public String status;

    public Expense(long id, String title, String date, double amount, String status) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.status = status;
    }
}
