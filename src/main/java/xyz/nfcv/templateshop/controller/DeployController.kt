package xyz.nfcv.templateshop.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import xyz.nfcv.templateshop.model.*
import xyz.nfcv.templateshop.model.DeployOption.Companion.DEPLOY_OPTION_GROUP
import xyz.nfcv.templateshop.security.UserTokenProvider
import xyz.nfcv.templateshop.service.DeployService
import javax.servlet.http.HttpServletResponse

@RestController
class DeployController {

    @Autowired
    private lateinit var userTokenProvider: UserTokenProvider

    @Autowired
    private lateinit var deployService: DeployService

    @RequestMapping("/deploy/options")
    fun getDeployOptionGroup(): Responses<Map<String, List<DeployOption>>> {
        return Responses.ok(data = DEPLOY_OPTION_GROUP)
    }

    @RequestMapping("/deploy/page")
    fun deployPage(
        @RequestHeader("Authorization") token: String?,
        @RequestBody deploy: DeployPageReq?
    ): Responses<DeployPageResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        if (deploy == null || deploy.template.isNullOrEmpty() || deploy.deployType.isNullOrEmpty() || deploy.userVerify == null || deploy.deployAddition == null) {
            return Responses.fail(message = "参数错误")
        }
        return deployService.deployPage(uid, deploy.template, deploy.deployType, deploy.userVerify, deploy.deployAddition)
    }

    @RequestMapping("/page/access/statistics/{deployId}")
    fun getPageAccessStatistics(
        @RequestHeader("Authorization") token: String?,
        @PathVariable deployId: String?
    ): Responses<PageAccessStatisticsResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        if (deployId.isNullOrEmpty()) {
            return Responses.fail(message = "参数错误")
        }
        return deployService.getPageAccessStatistics(uid, deployId)
    }

    @RequestMapping("/page/form/data/{deployId}")
    fun getPageFormData(
        @RequestHeader("Authorization") token: String?,
        @PathVariable deployId: String?
    ): Responses<PageFromDataResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        if (deployId.isNullOrEmpty()) {
            return Responses.fail(message = "参数错误")
        }
        return deployService.getPageFormData(uid, deployId)
    }

    @RequestMapping("/page/form/data/export/{deployId}")
    fun getPageFormDataExcel(
        @RequestHeader("Authorization") token: String?,
        @PathVariable deployId: String?,
    ): Responses<String>{
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        if (deployId.isNullOrEmpty()) {
            return Responses.fail(message = "参数错误")
        }
        val base64 = deployService.exportPageFormDataXlsx(uid, deployId)?: return Responses.fail(message = "页面数据不存在")

        return Responses.ok(data = base64)
    }

    @RequestMapping("/deploy/history")
    fun pageDeployHistory(@RequestHeader("Authorization") token: String?): Responses<DeployPageListResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")

        return deployService.getPageDeployList(uid)
    }

    @RequestMapping("/deploy/info/{deployId}")
    fun getDeployInfo(
        @RequestHeader("Authorization") token: String?,
        @PathVariable deployId: String?
    ): Responses<TemplateDeploy> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")
        if (deployId.isNullOrEmpty()) {
            return Responses.fail(message = "参数错误")
        }
        return deployService.getPageDeployInfo(uid, deployId)
    }

    @RequestMapping("/page/release/{page}")
    fun redirectPage(@PathVariable page: String, response: HttpServletResponse): String {
        val url = "https://page.nfcv.xyz/$page"
        response.sendRedirect(url)
        return url
    }
}