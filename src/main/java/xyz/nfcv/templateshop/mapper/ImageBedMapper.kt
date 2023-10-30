package xyz.nfcv.templateshop.mapper

import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import xyz.nfcv.templateshop.model.Image
import java.util.*

@Mapper
interface ImageBedMapper {
    @Insert("insert into image_bed (image_id, image_owner, upload_time) values (#{imageId}, #{imageOwner}, #{uploadTime})")
    fun addImage(imageId: String, imageOwner: String, uploadTime: Date): Int

    @Delete("delete from image_bed where image_id = #{imageId} and image_owner = #{imageOwner}")
    fun deleteImage(imageId: String, imageOwner: String): Int

    @Select("select * from image_bed where image_id = #{imageId} limit 1")
    fun getImage(imageId: String): Image?

    @Select("select * from image_bed where image_owner = #{imageOwner} order by upload_time desc")
    fun getImagesOfOwner(imageOwner: String): List<Image>
}