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

    @GetMapping("/country/{countryISO2}")
    fun getSwiftCodesByCountry(@PathVariable countryISO2: String): Map<String, Any>? {
        val swiftCodes = repository.findByCountryISO2(countryISO2)

        if (swiftCodes.isEmpty()) {
            return null
        } else {
            return mapOf(
                "countryISO2" to swiftCodes.first().countryISO2,
                "countryName" to swiftCodes.first().countryName,
                "swiftCodes" to swiftCodes.map { swiftCode ->
                    mapOf(
                        "address" to swiftCode.address,
                        "bankName" to swiftCode.bankName,
                        "countryISO2" to swiftCode.countryISO2,
                        "isHeadquarter" to swiftCode.isHeadquarter,
                        "swiftCode" to swiftCode.swiftCode
                    )
                }
            )
        }
    }
}
