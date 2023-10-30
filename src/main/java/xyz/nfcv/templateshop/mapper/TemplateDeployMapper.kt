package xyz.nfcv.templateshop.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import xyz.nfcv.templateshop.model.TemplateDeploy
import java.util.*

@Mapper
interface TemplateDeployMapper {

    @Insert("insert into template_deploy (deploy_id, uid, deploy_template, deploy_type, user_verify, deploy_addition, price, page_path, deploy_time) values (#{deployId}, #{uid}, #{deployTemplate}, #{deployType}, #{userVerify}, #{deployAddition}, #{price}, #{pagePath}, #{deployTime})")
    fun deployPage(
        deployId: String,
        uid: String,
        deployTemplate: String,
        deployType: String,
        userVerify: String,
        deployAddition: String,
        price: Int,
        pagePath: String,
        deployTime: Date
    ): Int

    @Select("select * from template_deploy where deploy_id = #{deployId} limit 1")
    fun getPageDeploy(deployId: String): TemplateDeploy?

    @Select("select * from template_deploy where uid = #{uid} order by deploy_time desc")
    fun getPageDeployList(uid: String): List<TemplateDeploy>


}