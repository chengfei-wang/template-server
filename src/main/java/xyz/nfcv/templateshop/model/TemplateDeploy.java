package xyz.nfcv.templateshop.model;

import java.util.Date;

public class TemplateDeploy {
    private String deployId;
    private String uid;
    private String deployTemplate;
    private String deployType;
    private String userVerify;
    private String deployAddition;
    private int price;
    private String pagePath;
    private Date deployTime;

    public String getDeployId() {
        return deployId;
    }

    public void setDeployId(String deployId) {
        this.deployId = deployId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDeployTemplate() {
        return deployTemplate;
    }

    public void setDeployTemplate(String deployTemplate) {
        this.deployTemplate = deployTemplate;
    }

    public String getDeployType() {
        return deployType;
    }

    public void setDeployType(String deployType) {
        this.deployType = deployType;
    }

    public String getUserVerify() {
        return userVerify;
    }

    public void setUserVerify(String userVerify) {
        this.userVerify = userVerify;
    }

    public String getDeployAddition() {
        return deployAddition;
    }

    public void setDeployAddition(String deployAddition) {
        this.deployAddition = deployAddition;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public Date getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(Date deployTime) {
        this.deployTime = deployTime;
    }
}
