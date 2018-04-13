package com.noadd.myapp.domain.entity.secret;

public class PreSecretBanAddr {
    private String uuid;
    private String banAddr;
    private Long banTime;
    private Long releaseTime;
    private String tryUserId;
    private String banType;
    private int isVoid;

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

    public String getBanAddr() {
        return banAddr;
    }

    public void setBanAddr(String banAddr) {
        this.banAddr = banAddr;
    }

    public Long getBanTime() {
        return banTime;
    }

    public void setBanTime(Long banTime) {
        this.banTime = banTime;
    }

    public Long getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Long releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getTryUserId() {
        return tryUserId;
    }

    public void setTryUserId(String tryUserId) {
        this.tryUserId = tryUserId;
    }

    public String getBanType() {
        return banType;
    }

    public void setBanType(String banType) {
        this.banType = banType;
    }
}
