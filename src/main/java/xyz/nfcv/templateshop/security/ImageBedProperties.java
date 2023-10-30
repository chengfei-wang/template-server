package xyz.nfcv.templateshop.security;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "image.bed")
@Component
public class ImageBedProperties {
    private String imageBedRoot;
    private String templateThumbnailRoot;

    public String getImageBedRoot() {
        return imageBedRoot;
    }

    public void setImageBedRoot(String imageBedRoot) {
        this.imageBedRoot = imageBedRoot;
    }

    public String getTemplateThumbnailRoot() {
        return templateThumbnailRoot;
    }
    public void setTemplateThumbnailRoot(String templateThumbnailRoot) {
        this.templateThumbnailRoot = templateThumbnailRoot;
    }
    public static String NOT_FOUND_IMAGE = "404.jpg";
    public static String NOT_FOUND_IMAGE_THUMBNAIL = "404_thumbnail.png";
}
