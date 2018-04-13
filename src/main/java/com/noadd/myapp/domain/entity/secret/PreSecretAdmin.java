package com.noadd.myapp.domain.entity.secret;

import java.math.BigDecimal;

public class PreSecretAdmin {
    private String uuid;
    private String userId;
    private String adminType;
    private BigDecimal balance;
    private int isVoid;
    private String adminStatus;

    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }

    public int getIsVoid() {
        return isVoid;
    }

    public void setIsVoid(int isVoid) {
        this.isVoid = isVoid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAdminType() {
        return adminType;
    }

    public void setAdminType(String adminType) {
        this.adminType = adminType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
