package xyz.nfcv.templateshop.service

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.nfcv.templateshop.mapper.TemplateDeployMapper
import xyz.nfcv.templateshop.mapper.TemplateMapper
import xyz.nfcv.templateshop.mapper.UserMapper
import xyz.nfcv.templateshop.model.*
import xyz.nfcv.templateshop.model.DeployOption.Companion.DEPLOY_ADDITION
import xyz.nfcv.templateshop.model.DeployOption.Companion.DEPLOY_TYPE
import xyz.nfcv.templateshop.model.DeployOption.Companion.USER_VERIFY
import xyz.nfcv.templateshop.util.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

@Service
class DeployService {
    @Autowired
    private lateinit var deployMapper: TemplateDeployMapper

    @Autowired
    private lateinit var templateMapper: TemplateMapper

    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var remoteExecutor: RemoteExecutor

    fun deployPage(uid: String, template: String, deployType: String, userVerify: String, deployAddition: Array<String>): Responses<DeployPageResp> {
        var price = 0
        val deployTypeOption = DEPLOY_TYPE.firstOrNull { it.id == deployType } ?: return Responses.fail(message = "请选择正确的部署类型")
        price += deployTypeOption.price
        val userVerifyOption = USER_VERIFY.firstOrNull { it.id == userVerify } ?: return Responses.fail(message = "请选择正确的验证方式")
        price += userVerifyOption.price
        val deployAdditionOptions = deployAddition.mapNotNull { option -> DEPLOY_ADDITION.firstOrNull { it.id == option } }
        deployAdditionOptions.forEach { price += it.price }

        val user = userMapper.getUserInfo(uid) ?: return Responses.fail(message = "用户不存在")
        if (user.balance < price) return Responses.fail(message = "余额不足")
        // 扣除用户余额
        userMapper.updateBalance(uid, -price)

        val deployId = uuid()
        val pagePath = uuid()
        // 创建部署配置
        val result = deployMapper.deployPage(
            deployId, uid, template, deployType, userVerify,
            deployAdditionOptions.joinToString(" ") { it.id },
            price, pagePath, Date()
        )
        if (result == 0) {
            return Responses.fail(message = "页面部署失败")
        }

        // 创建部署页面
        val templateObject = templateMapper.getTemplate(uid, template) ?: return Responses.fail(message = "模板不存在")
        val pageInfo = PageInfo(
            pagePath,
            templateObject.title,
            templateObject.content,
            deployType,
            userVerify,
            deployAdditionOptions.joinToString(" ") { it.id }
        )

        return remoteExecutor.request("page/deploy", RemoteAction.ACTION_DEPLOY, pageInfo)
    }

    fun getPageDeployList(uid: String): Responses<DeployPageListResp> {
        val deployPageList = deployMapper.getPageDeployList(uid)
        return Responses.ok(data = DeployPageListResp(deployPageList))
    }

    fun getPageDeployInfo(uid: String, deployId: String): Responses<TemplateDeploy> {
        val deployPageInfo = deployMapper.getPageDeploy(deployId) ?: return Responses.fail(message = "部署页面不存在")
        if (deployPageInfo.uid != uid) return Responses.fail(message = "无查看权限")
        return Responses.ok(data = deployPageInfo)
    }

    fun getPageAccessStatistics(uid: String, deployId: String): Responses<PageAccessStatisticsResp> {
        val deploy = deployMapper.getPageDeploy(deployId) ?: return Responses.fail(message = "页面不存在")
        if (deploy.uid != uid) return Responses.fail(message = "没有页面访问权限")

        return remoteExecutor.request("page/access/statistics", RemoteAction.ACTION_ACCESS_LOG, PageAccessStatisticsReq(deploy.pagePath))
    }

    fun getPageFormData(uid: String, deployId: String): Responses<PageFromDataResp> {
        val deploy = deployMapper.getPageDeploy(deployId) ?: return Responses.fail(message = "页面不存在")
        if (deploy.uid != uid) return Responses.fail(message = "没有页面访问权限")

        return remoteExecutor.request("page/form/data", RemoteAction.ACTION_FORM_DATA, PageFromDataReq(deploy.pagePath))
    }

    fun exportPageFormDataXlsx(uid: String, deployId: String): String? {
        val response: Responses<PageFromDataResp> = getPageFormData(uid, deployId)
        if (response.code != 200 || response.data == null) return null
        val pageFormData = response.data
        val headers = pageFormData.fields
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Sheet1")
        sheet.defaultColumnWidth = 20
        sheet.createRow(0).let { row ->
            row.createCell(0).setCellValue("IP地址")
            row.createCell(1).setCellValue("提交时间")
            row.createCell(2).setCellValue("提交用户")
            headers.forEachIndexed { i, header ->
                row.createCell(i + 3).also { cell ->
                    cell.setCellValue(header.desc)
                }
            }
        }

        pageFormData.data.forEachIndexed { i, data ->
            sheet.createRow(i + 1).let { row ->
                row.createCell(0).also { cell ->
                    cell.setCellValue(data.submit_ip_address)
                }
                row.createCell(1).also { cell ->
                    cell.setCellValue(formatter.format(data.submit_time))
                }
                row.createCell(2).also { cell ->
                    cell.setCellValue(data.submit_user)
                }
                headers.forEachIndexed { j, header ->
                    row.createCell(j + 3).also { cell ->
                        cell.setCellValue(data.submit_content[header.name])
                    }
                }
            }
        }

        val stream = ByteArrayOutputStream()
        workbook.write(stream)
        workbook.close()
        val bytes = stream.toByteArray()
        return bytes.b64encode.string
    }

    companion object{
        private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    }
}