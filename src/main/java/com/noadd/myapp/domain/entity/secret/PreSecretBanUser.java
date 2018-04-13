package com.noadd.myapp.domain.entity.secret;

public class PreSecretBanUser {
    private String uuid;
    private String banUserId;
    private Long banTime;
    private Long releaseTime;
    private String banType;
    private int isVoid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBanUserId() {
        return banUserId;
    }

    public void setBanUserId(String banUserId) {
        this.banUserId = banUserId;
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

    public String getBanType() {
        return banType;
    }

    public void setBanType(String banType) {
        this.banType = banType;
    }

    public int getIsVoid() {
        return isVoid;
    }

    public void setIsVoid(int isVoid) {
        this.isVoid = isVoid;
    }
}
