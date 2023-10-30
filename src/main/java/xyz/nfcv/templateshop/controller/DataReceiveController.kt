package xyz.nfcv.templateshop.controller

import com.google.gson.Gson
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xyz.nfcv.templateshop.model.Responses
import javax.servlet.http.HttpServletRequest

@RestController
class DataReceiveController {

    @RequestMapping("/form/test")
    fun testReceiver(request: HttpServletRequest): Responses<String> {
        val data = Gson().toJson(request.parameterMap)
        return Responses.ok(data = data)
    }
}