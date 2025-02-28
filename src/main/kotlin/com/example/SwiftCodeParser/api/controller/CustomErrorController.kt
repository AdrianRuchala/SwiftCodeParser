package com.example.SwiftCodeParser.api.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/error")
class CustomErrorController : ErrorController {

    @GetMapping
    fun handleError(request: HttpServletRequest): Map<String, String> {
        val status = request.getAttribute("jakarta.servlet.error.status_code") as Int? ?: 500

        val message = when (status) {
            404 -> "Endpoint not found"
            405 -> "Method not allowed"
            500 -> "Internal server error"
            else -> "Something went wrong"
        }

        return mapOf(
            "status" to status.toString(),
            "message" to message,
            "timestamp" to LocalDateTime.now().toString()
        )
    }
}
