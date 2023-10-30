package xyz.nfcv.templateshop.service

import net.coobird.thumbnailator.Thumbnails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import xyz.nfcv.templateshop.mapper.ImageBedMapper
import xyz.nfcv.templateshop.mapper.TemplateMapper
import xyz.nfcv.templateshop.model.*
import xyz.nfcv.templateshop.security.ImageBedProperties
import xyz.nfcv.templateshop.util.child
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.Date

@Service
class ImageBedService {
    @Autowired
    lateinit var imageRepository: ImageBedMapper

    @Autowired
    lateinit var templateMapper: TemplateMapper

    @Autowired
    lateinit var imageProp: ImageBedProperties

    fun getImage(imageId: String): ByteArray {
        val image = imageRepository.getImage(imageId)
            ?: return ClassPathResource(ImageBedProperties.NOT_FOUND_IMAGE).inputStream.use { it.readBytes() }
        val imageRoot = File(imageProp.imageBedRoot)
        val imageFile = File(imageRoot, image.imageId)
        if (!imageFile.exists()) {
            return ClassPathResource(ImageBedProperties.NOT_FOUND_IMAGE).inputStream.use { it.readBytes() }
        }
        val outputStream = ByteArrayOutputStream()
        Thumbnails.of(imageFile)
            .size(1024, 1024)
            .outputQuality(0.8f)
            .toOutputStream(outputStream)
        return outputStream.toByteArray()
    }

    fun getImageFull(imageId: String): ByteArray {
        val image = imageRepository.getImage(imageId)
            ?: return ClassPathResource(ImageBedProperties.NOT_FOUND_IMAGE).inputStream.use { it.readBytes() }
        val imageRoot = File(imageProp.imageBedRoot)
        val imageFile = File(imageRoot, image.imageId)
        if (!imageFile.exists()) {
            return ClassPathResource(ImageBedProperties.NOT_FOUND_IMAGE).inputStream.use { it.readBytes() }
        }
        return imageFile.readBytes()
    }

    fun getImageList(owner: String): Responses<List<Image>> {
        val images = imageRepository.getImagesOfOwner(owner)
        return Responses.ok(data = images)
    }

    fun addImage(imageId: String, owner: String): Responses<ImageUploadResp> {
        val uploadTime = Date()
        val result = imageRepository.addImage(imageId, owner, uploadTime)

        if (result > 0) {
            return Responses.ok(message = "图片上传成功", data = ImageUploadResp(imageId, owner, uploadTime))
        }

        return Responses.fail(message = "图片上传失败")
    }

    fun deleteImage(imageId: String, uid: String): Responses<ImageDeleteResp> {
        val imageInfo = imageRepository.getImage(imageId) ?: return Responses.fail(message = "图片不存在")
        if (imageInfo.imageOwner != uid) {
            return Responses.fail(message = "没有权限删除该图片")
        }

        imageRepository.deleteImage(imageId, uid)

        val imageRoot = File(imageProp.imageBedRoot)
        if (!imageRoot.exists()) {
            imageRoot.mkdirs()
        }
        val imagePath = imageRoot.child(imageInfo.imageId)
        if (!imagePath.exists()) {
            return Responses.fail(message = "图片不存在")
        }
        imagePath.delete()
        return Responses.ok(data = ImageDeleteResp(imageInfo.imageId))
    }

    fun addThumbnail(uid: String, tid: String, file: MultipartFile): Responses<TemplateThumbnailResp> {
        val templateInfo = templateMapper.getTemplateNoContent(uid, tid)
        if (templateInfo == null || templateInfo.creator != uid) {
            return Responses.fail(message = "没有权限添加缩略图")
        }
        val imageRoot = File(imageProp.templateThumbnailRoot)
        if (!imageRoot.exists()) {
            imageRoot.mkdirs()
        }
        val imagePath = imageRoot.child(tid).toPath()
        try {
            // 将图片写入文件imagePath
            file.transferTo(imagePath)
        } catch (e: IOException) {
            return Responses.fail(message = "图片保存失败")
        }
        return Responses.ok(data = TemplateThumbnailResp(tid))
    }

    fun copyThumbnail(tid: String, shareId: String) {
        val imageRoot = File(imageProp.templateThumbnailRoot)
        if (!imageRoot.exists()) {
            imageRoot.mkdirs()
        }
        val imageSrc = imageRoot.child(tid)
        val imageDst = imageRoot.child(shareId)
        if (imageSrc.exists()) {
            imageSrc.copyTo(imageDst)
        }
    }

    fun getTemplateThumbnail(tid: String): ByteArray {
        val imageRoot = File(imageProp.templateThumbnailRoot)
        val imageFile = File(imageRoot, tid)
        if (!imageFile.exists()) {
            return ClassPathResource(ImageBedProperties.NOT_FOUND_IMAGE_THUMBNAIL).inputStream.use { it.readBytes() }
        }
        val outputStream = ByteArrayOutputStream()
        Thumbnails.of(imageFile)
            .size(720, 1080)
            .outputQuality(0.8f)
            .toOutputStream(outputStream)
        return outputStream.toByteArray()
    }
}