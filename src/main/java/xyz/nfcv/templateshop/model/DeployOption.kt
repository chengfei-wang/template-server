package xyz.nfcv.templateshop.model

data class DeployOption(
    val id: String,
    val group: String,
    val name: String,
    val description: String,
    val price: Int,
) {
    companion object {
        const val GROUP_DEPLOY_TYPE = "DEPLOY_TYPE"
        const val GROUP_USER_VERIFY = "USER_VERIFY"
        const val GROUP_DEPLOY_ADDITION = "DEPLOY_ADDITION"

        const val DEPLOY_TYPE_STATIC = "DEPLOY_TYPE_STATIC"
        const val DEPLOY_TYPE_DYNAMIC = "DEPLOY_TYPE_DYNAMIC"

        const val USER_VERIFY_NONE = "USER_VERIFY_NONE"
        const val USER_VERIFY_EMAIL = "USER_VERIFY_EMAIL"
        const val USER_VERIFY_TEL = "USER_VERIFY_TEL"

        const val DEPLOY_ADDITION_EXPORT_DATA = "DEPLOY_ADDITION_EXPORT_DATA"
        const val DEPLOY_ADDITION_ACCESS_STATS = "DEPLOY_ADDITION_ACCESS_STATS"
        const val DEPLOY_ADDITION_LARGE_DATA = "DEPLOY_ADDITION_LARGE_DATA"

        val DEPLOY_TYPE = listOf(
            DeployOption(DEPLOY_TYPE_STATIC, GROUP_DEPLOY_TYPE, "静态部署", "适合无需交互的网站", 0),
            DeployOption(DEPLOY_TYPE_DYNAMIC, GROUP_DEPLOY_TYPE, "动态部署", "适合有数据提交的网站", 499),
        )

        val USER_VERIFY = listOf(
            DeployOption(USER_VERIFY_NONE, GROUP_USER_VERIFY, "无需验证", "无需验证用户身份", 0),
            DeployOption(USER_VERIFY_EMAIL, GROUP_USER_VERIFY, "邮箱验证", "用户访问前验证邮箱是否真实", 500),
            DeployOption(USER_VERIFY_TEL, GROUP_USER_VERIFY, "手机验证", "用户访问前验证手机号码是否真实", 1000),
        )

        val DEPLOY_ADDITION = listOf(
            DeployOption(DEPLOY_ADDITION_EXPORT_DATA, GROUP_DEPLOY_ADDITION, "数据导出服务(Beta)", "可以将表单数据进行解析并按表格导出", 1),
            DeployOption(DEPLOY_ADDITION_ACCESS_STATS, GROUP_DEPLOY_ADDITION, "访问统计服务(Beta)", "统计网站用户访问状况", 1),
            DeployOption(DEPLOY_ADDITION_LARGE_DATA, GROUP_DEPLOY_ADDITION, "大容量数据服务", "可存储数据存储量增加10000条(初始1000条)", 1000),
        )

        val DEPLOY_OPTION_GROUP = mapOf<String, List<DeployOption>>(
            GROUP_DEPLOY_TYPE to DEPLOY_TYPE,
            GROUP_USER_VERIFY to USER_VERIFY,
            GROUP_DEPLOY_ADDITION to DEPLOY_ADDITION,
        )
    }
}