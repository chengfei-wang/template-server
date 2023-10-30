package xyz.nfcv.templateshop.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.nfcv.templateshop.mapper.TemplateMapper
import xyz.nfcv.templateshop.mapper.UserMapper
import xyz.nfcv.templateshop.model.*
import xyz.nfcv.templateshop.util.uuid

@Service
class TemplateService {
    @Autowired
    private lateinit var templateMapper: TemplateMapper
    @Autowired
    private lateinit var userMapper: UserMapper
    @Autowired
    private lateinit var imageBedService: ImageBedService

    fun createTemplate(uid: String, title: String, content: String): Responses<TemplateCreateResp> {
        val tid = uuid()
        val result = templateMapper.addTemplate(uid, tid, title, content)

        if (result > 0) {
            return Responses.ok(message = "保存成功", data = TemplateCreateResp(tid))
        }

        return Responses.fail(message = "保存失败")
    }

    fun deleteTemplate(uid: String, tid: String): Responses<TemplateDeleteResp> {
        val result = templateMapper.deleteTemplate(uid, tid)

        if (result > 0) {
            return Responses.ok(message = "删除成功", data = TemplateDeleteResp(tid))
        }

        return Responses.fail(message = "删除失败")
    }

    fun getTemplatesByUid(uid: String): Responses<TemplateQueryListResp> {
        val result = templateMapper.getTemplatesByCreator(uid)
        return Responses.ok(data = TemplateQueryListResp(result))
    }

    fun getTemplate(uid: String, tid: String): Responses<TemplateQueryResp> {
        val template = templateMapper.getTemplate(uid, tid) ?: return Responses.fail(message = "模版不存在")
        return Responses.ok(data = TemplateQueryResp(template))
    }

    fun updateTemplate(uid: String, tid: String, title: String, content: String): Responses<TemplateUpdateResp> {
        templateMapper.getTemplateNoContent(uid, tid) ?: return Responses.fail(message = "模版不存在")
        val result = templateMapper.updateTemplate(uid, tid, title, content)
        if (result > 0) {
            val template = templateMapper.getTemplateNoContent(uid, tid) ?: return Responses.fail(message = "模版不存在")
            return Responses.ok(message = "更新成功", data = TemplateUpdateResp(template.tid, template.updateTime))
        }

        return Responses.fail(message = "更新失败")
    }

    fun shareTemplate(uid: String, tid: String, price: Int): Responses<TemplateShareResp> {
        val template = templateMapper.getTemplate(uid, tid) ?: return Responses.fail(message = "模版不存在")
        val shareTid = uuid()
        val result = templateMapper.shareTemplate(template.creator, shareTid, template.title, template.content, price)
        if (result > 0) {
            imageBedService.copyThumbnail(tid, shareTid)
            return Responses.ok(message = "分享成功", data = TemplateShareResp(shareTid))
        }

        return Responses.fail(message = "分享失败")
    }

    fun buyTemplate(tid: String, buyer: String): Responses<TemplateBuyResp> {
        val template = templateMapper.getTemplateShareNoContent(tid) ?: return Responses.fail(message = "模版不存在")

        val existResult = templateMapper.getTemplateOrder(tid, buyer)
        if (existResult != null) {
            return Responses.fail(message = "不能重复购买")
        }

        val buyerUser = userMapper.getUserInfo(buyer) ?: return Responses.fail(message = "用户不存在")
        if (buyerUser.uid == template.creator) {
            return Responses.fail(message = "不能购买自己的模版")
        }
        if (buyerUser.balance < template.price) {
            return Responses.fail(message = "余额不足")
        }

        // 扣除用户余额
        val buyerResult = userMapper.updateBalance(buyer, -template.price)
        if(buyerResult <= 0) {
            return Responses.fail(message = "买方扣除余额失败")
        }
        // 卖方增加余额，抽取5%作为收入
        val sellerResult = userMapper.updateBalance(template.creator, (template.price * 0.95).toInt())
        if (sellerResult <= 0) {
            return Responses.fail(message = "卖方增加余额失败")
        }
        // 创建购买记录
        val orderId = uuid()
        val orderResult = templateMapper.addTemplateOrder(orderId, buyer, tid, template.price)
        if (orderResult > 0) {
            return Responses.ok(message = "购买成功", data = TemplateBuyResp(template.tid))
        }

        return Responses.fail(message = "购买失败")
    }

    // 获取除uid之外的共享模版列表
    fun getAllSharedTemplates(uid: String): Responses<TemplateSharedListResp> {
        val result = templateMapper.getAllSharedTemplates(uid)
        return Responses.ok(data = TemplateSharedListResp(result))
    }

    // 获取已购买的模版列表
    fun getAllBoughtTemplates(uid: String): Responses<TemplateBoughtListResp> {
        val result = templateMapper.getAllBoughtTemplates(uid)
        return Responses.ok(data = TemplateBoughtListResp(result))
    }

    fun createTemplateFromBought(uid: String, tid: String): Responses<TemplateCreateResp> {
        val template = templateMapper.getTemplateShare(tid) ?: return Responses.fail(message = "模版不存在")
        val newTid = uuid()
        val result = templateMapper.addTemplate(uid, newTid, template.title, template.content)
        if (result > 0) {
            return Responses.ok(message = "创建成功", data = TemplateCreateResp(newTid))
        }

        return Responses.fail(message = "创建失败")
    }
}