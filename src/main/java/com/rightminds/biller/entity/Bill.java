package com.rightminds.biller.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rightminds.biller.model.BillStatus;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.rightminds.biller.model.BillStatus.COMPLETED;
import static com.rightminds.biller.model.BillStatus.IN_PROGRESS;
import static com.rightminds.biller.util.CastUtil.*;

@Entity
@Table(name = "BILL")
@Data
public class Bill {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq_gen")
    @SequenceGenerator(name = "order_seq_gen", sequenceName = "order_seq_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "CUSTOMERID", referencedColumnName = "ID")
    private Customer customer;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SERVICECHARGE")
    private BigDecimal serviceCharge;

    @Column(name = "SERVICETAX")
    private BigDecimal serviceTax;

    @Column(name = "SUBTOTAL")
    private BigDecimal subTotal;

    @Column(name = "DISCOUNT")
    private BigDecimal discount;

    @Column(name = "TOTAL")
    private BigDecimal total;

    @Column(name = "CASH")
    private BigDecimal cash;

    @Column(name = "CARD")
    private BigDecimal card;

    @Column(name = "STATUS")
    private BillStatus status;

    @CreatedDate
    @Column(name = "CREATEDON")
    private Date createdOn;

    @LastModifiedDate
    @Column(name = "LASTMODIFIEDON")
    private Date lastModifiedOn;

    @OneToMany(mappedBy = "bill", fetch = FetchType.EAGER)
    @JsonProperty("orders")
    private List<BillItem> billItems = new ArrayList<>();

    public Bill() {

    }

    public Bill(Customer customer, String name, BigDecimal serviceCharge,
                BigDecimal serviceTax, BigDecimal subTotal, BigDecimal discount, BigDecimal total, BigDecimal cash,
                BigDecimal card, BillStatus status) {
        this.customer = customer;
        this.name = name;
        this.serviceCharge = serviceCharge;
        this.serviceTax = serviceTax;
        this.subTotal = subTotal;
        this.discount = discount;
        this.total = total;
        this.cash = cash;
        this.card = card;
        this.status = status;
    }

    public Bill(Integer id, Customer customer, String name, BigDecimal serviceCharge, BigDecimal serviceTax,
                BigDecimal subTotal, BigDecimal discount, BigDecimal total, BigDecimal cash, BigDecimal card, BillStatus status) {
        this(customer, name, serviceCharge, serviceTax, discount, subTotal, total, cash, card, status);
        this.id = id;
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


    public static Bill fromMap(Map map) {
        Customer customer = map.get("customer") != null ? Customer.fromMap((Map) map.get("customer")) : null;
        String name = getString(map.get("name"));
        BigDecimal serviceCharge = getBigDecimal(map.get("serviceCharge"));
        BigDecimal serviceTax = getBigDecimal(map.get("serviceTax"));
        BigDecimal subTotal = getBigDecimal(map.get("subTotal"));
        BigDecimal discount = getBigDecimal(map.get("discount"));
        BigDecimal total = getBigDecimal(map.get("total"));
        BigDecimal cash = getBigDecimal(map.get("cash"));
        BigDecimal card = getBigDecimal(map.get("card"));
        BillStatus status = map.get("status") != null ? BillStatus.valueOf(getString(map.get("status")).toUpperCase()) : IN_PROGRESS;
        Integer id = getInteger(map.get("id"));
        if (id == null) {
            return new Bill(customer, name, serviceCharge, serviceTax, subTotal, discount, total, cash, card, status);
        }
        return new Bill(id, customer, name, serviceCharge, serviceTax, subTotal, discount, total, cash, card, status);
    }

    public Bill withComputedValues(BigDecimal serviceCharge, BigDecimal serviceTax, BigDecimal total) {
        return new Bill(id, customer, name, serviceCharge, serviceTax, subTotal, discount, total, cash, card, COMPLETED);
    }
}
