package xyz.nfcv.templateshop.model

import java.util.Date


data class Image(var imageId: String, var imageOwner: String, var uploadTime: Date) {
    constructor() : this("", "", Date())
}