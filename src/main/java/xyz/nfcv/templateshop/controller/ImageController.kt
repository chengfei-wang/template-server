package xyz.nfcv.templateshop.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import xyz.nfcv.templateshop.model.*
import xyz.nfcv.templateshop.security.ImageBedProperties
import xyz.nfcv.templateshop.security.ImageBedProperties.NOT_FOUND_IMAGE
import xyz.nfcv.templateshop.security.ImageBedProperties.NOT_FOUND_IMAGE_THUMBNAIL
import xyz.nfcv.templateshop.security.UserTokenProvider
import xyz.nfcv.templateshop.service.ImageBedService
import xyz.nfcv.templateshop.util.child
import xyz.nfcv.templateshop.util.uuid
import java.io.File
import java.io.IOException

@RestController
class ImageController {
    @Autowired
    lateinit var imageService: ImageBedService

    @Autowired
    lateinit var imageProp: ImageBedProperties

    @Autowired
    lateinit var userTokenProvider: UserTokenProvider

    @RequestMapping("/image/upload")
    fun uploadImage(
        @RequestHeader("Authorization") token: String?,
        @RequestParam("file") file: MultipartFile?
    ): Responses<ImageUploadResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        if (file == null || file.isEmpty) {
            return Responses.fail(message = "图片不能为空")
        }
        val imageId = uuid()
        val imageRoot = File(imageProp.imageBedRoot)
        if (!imageRoot.exists()) {
            imageRoot.mkdirs()
        }
        val imagePath = imageRoot.child(imageId).toPath()
        try {
            // 将图片写入文件imagePath
            file.transferTo(imagePath)
        } catch (e: IOException) {
            return Responses.fail(message = "图片保存失败")
        }

        return imageService.addImage(imageId, uid)
    }

    @RequestMapping("/thumbnail/upload")
    fun uploadThumbnail(
        @RequestHeader("Authorization") token: String?,
        @RequestParam("file") file: MultipartFile?,
        @RequestParam("tid") tid: String?
    ): Responses<TemplateThumbnailResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        if (file == null || file.isEmpty) {
            return Responses.fail(message = "图片不能为空")
        }
        if (tid.isNullOrEmpty()) {
            return Responses.fail(message = "模板ID不能为空")
        }
        return imageService.addThumbnail(uid, tid, file)
    }

    @RequestMapping("/thumbnail/{templateId}", produces = ["image/*"])
    fun getTemplateThumbnail(@PathVariable("templateId") imageId: String?): ByteArray {
        if (imageId.isNullOrEmpty()) {
            return ClassPathResource(NOT_FOUND_IMAGE_THUMBNAIL).inputStream.use { it.readBytes() }
        }
        return imageService.getTemplateThumbnail(imageId)
    }

    @RequestMapping("/image/delete")
    fun deleteImage(
        @RequestHeader("Authorization") token: String?,
        @RequestBody image: ImageDeleteReq?
    ): Responses<ImageDeleteResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        if (image?.imageId == null) {
            return Responses.fail(message = "图片id不能为空")
        }

        return imageService.deleteImage(image.imageId, uid)
    }


    @RequestMapping("/image/list")
    fun listImage(
        @RequestHeader("Authorization") token: String?
    ): Responses<List<Image>> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")

        return imageService.getImageList(uid)
    }

    @RequestMapping("/image/{imageId}", produces = ["image/*"])
    fun getImage(@PathVariable("imageId") imageId: String?): ByteArray {
        if (imageId.isNullOrEmpty()) {
            return ClassPathResource(NOT_FOUND_IMAGE).inputStream.use { it.readBytes() }
        }

        return imageService.getImage(imageId)
    }

    @RequestMapping("/image/full/{imageId}", produces = ["image/*"])
    fun getImageFull(@PathVariable("imageId") imageId: String?): ByteArray {
        if (imageId.isNullOrEmpty()) {
            return ClassPathResource(NOT_FOUND_IMAGE).inputStream.use { it.readBytes() }
        }

        return imageService.getImageFull(imageId)
    }

}
