package com.linked_sys.maktabi.models;


public class CaptainBalance {
    private int balanceID;
    private int captainID;
    private int amount;
    private String notes;
    private String createdDate;

    public CaptainBalance() {
    }

    public int getBalanceID() {
        return balanceID;
    }

    public void setBalanceID(int balanceID) {
        this.balanceID = balanceID;
    }

    public int getCaptainID() {
        return captainID;
    }

    public void setCaptainID(int captainID) {
        this.captainID = captainID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
