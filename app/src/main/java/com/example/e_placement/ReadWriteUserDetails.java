package com.example.e_placement;

public class ReadWriteUserDetails {
     public  String first_Name, middle_Name, last_Name, emailtxt, contact_no, facultytxt, sem;

     public  ReadWriteUserDetails(){}

    public ReadWriteUserDetails(String emailtxt) {
        this.emailtxt = emailtxt;
    }

    public ReadWriteUserDetails(String first_Name, String middle_Name, String last_Name, String emailtxt, String contact_no, String facultytxt, String sem) {
        this.first_Name = first_Name;
        this.middle_Name = middle_Name;
        this.last_Name = last_Name;
        this.emailtxt = emailtxt;
        this.contact_no = contact_no;
        this.facultytxt = facultytxt;
        this.sem = sem;
    }

    public ReadWriteUserDetails(String first_Name, String middle_Name, String last_Name, String emailtxt, String contact_no, String facultytxt) {
        this.first_Name = first_Name;
        this.middle_Name = middle_Name;
        this.last_Name = last_Name;
        this.emailtxt = emailtxt;
        this.contact_no = contact_no;
        this.facultytxt = facultytxt;
    }
}
