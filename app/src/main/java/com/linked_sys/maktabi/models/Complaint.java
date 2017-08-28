package com.linked_sys.maktabi.models;


public class Complaint {
    private int compID;
    private String compTitle;
    private String compBody;
    private String compDate;
    private boolean isSolved;

    public Complaint() {
    }

    public int getCompID() {
        return compID;
    }

    public void setCompID(int compID) {
        this.compID = compID;
    }

    public String getCompTitle() {
        return compTitle;
    }

    public void setCompTitle(String compTitle) {
        this.compTitle = compTitle;
    }

    public String getCompBody() {
        return compBody;
    }

    public void setCompBody(String compBody) {
        this.compBody = compBody;
    }

    public String getCompDate() {
        return compDate;
    }

    public void setCompDate(String compDate) {
        this.compDate = compDate;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }
}
