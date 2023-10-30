package xyz.nfcv.templateshop.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "remote.info")
public class RemoteInfoProperties {
    private String templateShopUrl;
    private String templateUniversalUrl;

    public String getTemplateShopUrl() {
        return templateShopUrl;
    }

    public void setTemplateShopUrl(String templateShopUrl) {
        this.templateShopUrl = templateShopUrl;
    }

    public String getTemplateUniversalUrl() {
        return templateUniversalUrl;
    }

    public void setTemplateUniversalUrl(String templateUniversalUrl) {
        this.templateUniversalUrl = templateUniversalUrl;
    }
}
