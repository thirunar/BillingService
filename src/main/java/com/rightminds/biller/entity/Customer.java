package com.rightminds.biller.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

import static com.rightminds.biller.util.CastUtil.getString;

@Entity
@Table(name = "CUSTOMER")
@Data
public class Customer {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq_gen")
    @SequenceGenerator(name = "customer_seq_gen", sequenceName = "customer_seq_id")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONENUMBER")
    private String phoneNumber;

    @Column(name = "ADDRESS")
    private String address;

    @CreatedDate
    @Column(name = "CREATEDON")
    private Date createdOn;

    @LastModifiedDate
    @Column(name = "LASTMODIFIEDON")
    private Date lastModifiedOn;

    public Customer() {
    }

    public Customer(String name, String phoneNumber, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    @PrePersist
    protected void onCreate() {
        setCreatedOn();
        setLastModifiedOn();
    }

    @PreUpdate
    protected void onUpdate() {
        setLastModifiedOn();
    }

    public void setCreatedOn() {
        this.createdOn = new Date();
    }

    public void setLastModifiedOn() {
        this.lastModifiedOn = new Date();
    }

    public static Customer fromMap(Map map) {
        String name = getString(map.get("name"));
        String description = getString(map.get("phoneNumber"));
        String address = getString(map.get("address"));

        return new Customer(name, description, address);
    }



}
