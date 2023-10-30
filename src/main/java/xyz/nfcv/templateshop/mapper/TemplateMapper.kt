package xyz.nfcv.templateshop.mapper

import org.apache.ibatis.annotations.*
import org.springframework.stereotype.Repository
import xyz.nfcv.templateshop.model.*

@Repository
@Mapper
interface TemplateMapper {
    @Select("select tid, creator, title, update_time from template where creator = #{creator} order by update_time desc")
    fun getTemplatesByCreator(creator: String): List<TemplateNoContent>

    @Select("select * from template where creator = #{creator} and tid = #{tid} limit 1")
    fun getTemplate(creator: String, tid: String): Template?

    @Select("select tid, creator, title, update_time from template where creator = #{creator} and tid = #{tid} limit 1")
    fun getTemplateNoContent(creator: String, tid: String): TemplateNoContent?

    @Insert("insert into template (tid, creator, title, content, update_time) values (#{tid}, #{creator}, #{title}, #{content}, current_timestamp)")
    fun addTemplate(creator: String, tid: String, title: String, content: String): Int

    @Update("update template set title = #{title}, content = #{content}, update_time = current_timestamp where creator = #{creator} and tid = #{tid}")
    fun updateTemplate(creator: String, tid: String, title: String, content: String): Int

    @Delete("delete from template where creator = #{creator} and tid = #{tid}")
    fun deleteTemplate(creator: String, tid: String): Int

    @Insert("insert into template_share (tid, creator, title, content, price, share_time) values (#{tid}, #{creator}, #{title}, #{content}, #{price}, current_timestamp)")
    fun shareTemplate(creator: String, tid: String, title: String, content: String, price: Int): Int

    @Update("insert into template_order (order_id, template, buyer, price, order_time) values (#{orderId}, #{tid}, #{buyer}, #{price}, current_timestamp)")
    fun addTemplateOrder(orderId: String, buyer: String, tid: String, price: Int): Int

    @Select("select tid, creator, title, content, price, share_time from template_share where tid = #{tid} limit 1")
    fun getTemplateShare(tid: String): TemplateShare?

    @Select("select tid, creator, title, price, share_time from template_share where tid = #{tid} limit 1")
    fun getTemplateShareNoContent(tid: String): TemplateShareNoContent?

    // 获取除uid之外的所有模版
    @Select("select tid, creator, title, price, share_time from template_share where creator != #{uid} order by share_time desc")
    fun getAllSharedTemplates(uid: String): List<TemplateShareNoContent>

    // 从template_order获取uid所有已购买的模版
    @Select("select order_id, template, buyer, price, order_time from template_order where buyer = #{uid} order by order_time desc")
    @Results(
        Result(
            property = "template",
            column = "template",
            one = One(select = "xyz.nfcv.templateshop.mapper.TemplateMapper.getTemplateShareNoContent")
        ),
        Result(
            property = "buyer",
            column = "buyer",
            one = One(select = "xyz.nfcv.templateshop.mapper.UserMapper.getUserSimple")
        )
    )
    fun getAllBoughtTemplates(uid: String): List<TemplateOrder>

    @Select("select order_id, template, buyer, price, order_time from template_order where template = #{tid} and buyer = #{buyer} limit 1")
    @Results(
        Result(
            property = "template",
            column = "template",
            one = One(select = "xyz.nfcv.templateshop.mapper.TemplateMapper.getTemplateShareNoContent")
        ),
        Result(
            property = "buyer",
            column = "buyer",
            one = One(select = "xyz.nfcv.templateshop.mapper.UserMapper.getUserSimple")
        ),
    )
    fun getTemplateOrder(tid: String, buyer: String): TemplateOrder?
}