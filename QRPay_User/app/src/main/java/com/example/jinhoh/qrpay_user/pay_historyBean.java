package com.example.jinhoh.qrpay_user;

public class pay_historyBean {

    String orderDate;
    String orderCompany;
    String orderHistory;
    String orderMoney;

    public pay_historyBean(String orderDate, String orderCompany, String orderHistory, String orderMoney) {
        this.orderDate = orderDate;
        this.orderCompany = orderCompany;
        this.orderHistory = orderHistory;
        this.orderMoney = orderMoney + "원";
    }
}
