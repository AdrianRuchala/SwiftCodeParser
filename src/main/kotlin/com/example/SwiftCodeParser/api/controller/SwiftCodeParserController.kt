package com.example.SwiftCodeParser.api.controller

import com.example.SwiftCodeParser.api.repository.SwiftCodeParserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/swift-codes")
class SwiftCodeParserController(val repository: SwiftCodeParserRepository) {

    @GetMapping("/{swiftCode}")
    fun getSwiftCode(@PathVariable swiftCode: String) =
        repository.findBySwiftCode(swiftCode)
}
