package xyz.nfcv.templateshop.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import xyz.nfcv.templateshop.mapper.UserMapper
import xyz.nfcv.templateshop.model.*
import xyz.nfcv.templateshop.security.UserTokenProvider
import xyz.nfcv.templateshop.util.EncryptProvider.salted
import xyz.nfcv.templateshop.util.uuid


@Service
class UserService {
    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var userTokenProvider: UserTokenProvider

    fun register(name: String, email: String, password: String): Responses<UserRegisterResp> {
        val user = userMapper.getUserByEmail(email)
        if (user != null) {
            return Responses.fail(message = "邮箱已被注册")
        }
        val uid = uuid()
        val resultUser = userMapper.addUser(uid, name, email, password.salted(uid))
        val resultUserInfo = userMapper.addUserInfo(uid, 0)
        return if (resultUser >= 0 && resultUserInfo >= 0) {
            Responses.ok(message = "注册成功", data = UserRegisterResp(uid, name, email))
        } else {
            Responses.fail(message = "注册失败")
        }
    }

    fun login(email: String, password: String): Responses<UserLoginResp> {
        val user = userMapper.getUserByEmail(email)
        if (user == null || user.password != password.salted(user.uid)) {
            return Responses.fail(message = "用户名或密码错误")
        }
        val token = userTokenProvider.sign(user)
        return Responses.ok(message = "登录成功", data = UserLoginResp(user.uid, user.name, user.email, token))
    }

    fun verify(token: String): Responses<UserVerifyResp> {
        val uid = userTokenProvider.verify(token) ?: return Responses.fail(message = "TOKEN无效")
        val user = userMapper.getUser(uid) ?: return Responses.fail(message = "用户不存在")
        return Responses.ok(data = UserVerifyResp(user.uid, user.name, user.email))
    }

    fun info(uid: String): Responses<UserInfoResp> {
        val user = userMapper.getUser(uid) ?: return Responses.fail(message = "用户不存在")
        val info = userMapper.getUserInfo(uid) ?: return Responses.fail(message = "用户信息不存在")
        return Responses.ok(data = UserInfoResp(user.uid, user.name, user.email, info.balance))
    }
}