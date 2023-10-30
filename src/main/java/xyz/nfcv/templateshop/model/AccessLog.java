package xyz.nfcv.templateshop.model;

import java.util.Date;

public class AccessLog {
    private String accessId;
    private String accessPage;
    private String accessIpAddress;
    private Date accessTime;

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessPage() {
        return accessPage;
    }

    public void setAccessPage(String accessPage) {
        this.accessPage = accessPage;
    }

    public String getAccessIpAddress() {
        return accessIpAddress;
    }

    public void setAccessIpAddress(String accessIpAddress) {
        this.accessIpAddress = accessIpAddress;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

}
