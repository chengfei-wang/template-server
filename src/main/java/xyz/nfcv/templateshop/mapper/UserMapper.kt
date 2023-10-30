package xyz.nfcv.templateshop.mapper

import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update
import org.springframework.stereotype.Repository
import xyz.nfcv.templateshop.model.User
import xyz.nfcv.templateshop.model.UserInfo
import xyz.nfcv.templateshop.model.UserSimple

@Mapper
@Repository
interface UserMapper {
    @Insert("insert into user (uid, name, email, password) values (#{uid}, #{name}, #{email}, #{password})")
    fun addUser(uid: String, name: String, email: String, password: String): Int

    @Select("select * from user where uid = #{uid} limit 1")
    fun getUser(uid: String): User?

    @Select("select * from user where email = #{email} limit 1")
    fun getUserByEmail(email: String): User?

    @Select("select uid, name, email from user where uid = #{uid} limit 1")
    fun getUserSimple(uid: String): UserSimple?

    @Update("update user_info set balance = balance + #{price} where uid = #{buyer}")
    fun updateBalance(buyer: String, price: Int): Int

    @Insert("insert into user_info (uid, balance) values (#{uid}, #{balance})")
    fun addUserInfo(uid: String, balance: Int): Int

    @Select("select * from user_info where uid = #{uid} limit 1")
    fun getUserInfo(uid: String): UserInfo?
}