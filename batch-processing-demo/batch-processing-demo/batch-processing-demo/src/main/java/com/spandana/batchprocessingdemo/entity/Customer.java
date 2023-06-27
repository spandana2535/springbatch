package com.spandana.batchprocessingdemo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Customer_Details")
public class Customer {
    @Id
    @Column(name = "Index")
    private int index;
    @Column(name = "Customer_ID")
    private String customer_id;
    @Column(name = "First_Name")
    private String first_name;
    @Column(name = "Last_Name")
    private String last_name;
    @Column(name = "Company")
    private String company;
    @Column(name = "City")
    private String city;
    @Column(name = "Phone 1")
    private String phone1;
    @Column(name = "Phone 2")
    private String phone2;
    @Column(name = "Email")
    private String email;
    @Column(name = "Subscription_Date")
    private String subscription_date;
    @Column(name = "Website")
    private String website;
}
