package xyz.nfcv.templateshop.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xyz.nfcv.templateshop.model.*
import xyz.nfcv.templateshop.security.UserTokenProvider
import xyz.nfcv.templateshop.service.UserService
import xyz.nfcv.templateshop.util.checkEmail
import xyz.nfcv.templateshop.util.checkEmpty

@RestController
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userTokenProvider: UserTokenProvider

    @RequestMapping("/user/login")
    fun login(@RequestBody user: UserLoginReq): Responses<UserLoginResp> {
        if (checkEmpty(user.email, user.password)) {
            return Responses.fail(message = "参数错误")
        }
        return userService.login(user.email!!, user.password!!)
    }

    @RequestMapping("/user/register")
    fun register(@RequestBody user: UserRegisterReq): Responses<UserRegisterResp> {
        if (checkEmpty(user.name, user.email, user.password)) {
            return Responses.fail(message = "参数错误")
        }
        if (!checkEmail(user.email)) {
            return Responses.fail(message = "邮箱异常")
        }
        return userService.register(user.name!!, user.email!!, user.password!!)
    }

    @RequestMapping("/user/verify")
    fun verify(@RequestHeader("Authorization") token: String?): Responses<UserVerifyResp> {
        if (checkEmpty(token)) {
            return Responses.fail(message = "参数错误")
        }
        return userService.verify(token!!)
    }

    @RequestMapping("/user/info")
    fun info(@RequestHeader("Authorization") token: String?): Responses<UserInfoResp> {
        if (token.isNullOrEmpty()) {
            return Responses.fail(message = "用户未登录")
        }

        // 校验用户token
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "用户未登录")

        return userService.info(uid)
    }
}