package xyz.nfcv.templateshop.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xyz.nfcv.templateshop.model.*
import xyz.nfcv.templateshop.security.UserTokenProvider
import xyz.nfcv.templateshop.service.TemplateService

@RestController
class TemplateController {
    @Autowired
    private lateinit var templateService: TemplateService

    @Autowired
    private lateinit var userTokenProvider: UserTokenProvider

    @RequestMapping("/template/{tid}")
    fun getTemplate(@RequestHeader("Authorization") token: String?, @PathVariable tid: String): Responses<TemplateQueryResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        return templateService.getTemplate(uid, tid)
    }

    @RequestMapping("/template/insert")
    fun addTemplate(@RequestHeader("Authorization") token: String?, @RequestBody template: TemplateCreateReq?): Responses<TemplateCreateResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        if (template?.title == null || template.content == null) {
            return Responses.fail(message = "参数错误")
        }
        return templateService.createTemplate(uid, template.title, template.content)
    }

    @RequestMapping("/template/save")
    fun saveTemplate(@RequestHeader("Authorization") token: String?, @RequestBody template: TemplateUpdateReq?): Responses<TemplateUpdateResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        if (template?.tid == null || template.title == null || template.content == null) {
            return Responses.fail(message = "参数错误")
        }

        return templateService.updateTemplate(uid, template.tid, template.title, template.content)
    }

    @RequestMapping("/template/list")
    fun getTemplateList(@RequestHeader("Authorization") token: String?): Responses<TemplateQueryListResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        return templateService.getTemplatesByUid(uid)
    }

    @RequestMapping("/template/delete")
    fun deleteTemplate(@RequestHeader("Authorization") token: String?, @RequestBody tid: String?): Responses<TemplateDeleteResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        if (tid == null) {
            return Responses.fail(message = "参数错误")
        }
        return templateService.deleteTemplate(uid, tid)
    }

    @RequestMapping("/template/share")
    fun shareTemplate(@RequestHeader("Authorization") token: String?, @RequestBody template: TemplateShareReq?): Responses<TemplateShareResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        if (template?.tid == null || template.price == null || template.price < 0) {
            return Responses.fail(message = "参数错误")
        }
        return templateService.shareTemplate(uid, template.tid, template.price)
    }

    @RequestMapping("/template/buy")
    fun buyTemplate(@RequestHeader("Authorization") token: String?, @RequestBody template: TemplateBuyReq?): Responses<TemplateBuyResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        if (template?.tid == null) {
            return Responses.fail(message = "参数错误")
        }
        return templateService.buyTemplate(template.tid, uid)
    }

    @RequestMapping("/template/share/list")
    fun getShareTemplateList(@RequestHeader("Authorization") token: String?): Responses<TemplateSharedListResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        return templateService.getAllSharedTemplates(uid)
    }

    @RequestMapping("/template/bought/list")
    fun getBoughtTemplateList(@RequestHeader("Authorization") token: String?): Responses<TemplateBoughtListResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        return templateService.getAllBoughtTemplates(uid)
    }

    @RequestMapping("/template/bought/create")
    fun createTemplateFromBought(@RequestHeader("Authorization") token: String?, @RequestBody template: TemplateCreateFromBoughtReq?): Responses<TemplateCreateResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        if (template?.tid == null) {
            return Responses.fail(message = "参数错误")
        }
        return templateService.createTemplateFromBought(uid, template.tid)
    }
}