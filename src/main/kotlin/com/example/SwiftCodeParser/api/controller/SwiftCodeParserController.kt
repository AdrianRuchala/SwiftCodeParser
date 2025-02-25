package com.example.SwiftCodeParser.api.controller

import com.example.SwiftCodeParser.api.model.SwiftCode
import com.example.SwiftCodeParser.api.repository.SwiftCodeParserRepository
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/swift-codes")
class SwiftCodeParserController(val repository: SwiftCodeParserRepository) {

    @GetMapping("/{swiftCode}")
    fun getSwiftCode(@PathVariable swiftCode: String): Map<String, Any>? {
        val swiftCodes = repository.findBySwiftCode(swiftCode)

        if (swiftCodes.isEmpty()) {
            return mapOf("message" to "SWIFT Code doesn't exist")
        } else {

            val foundedSwiftCode = swiftCodes.first()

            return if (foundedSwiftCode.isHeadquarter) {
                val branches = repository.findByCountryISO2(foundedSwiftCode.countryISO2)
                    .filter { it.bankName == foundedSwiftCode.bankName && !it.isHeadquarter }

                mapOf(
                    "address" to foundedSwiftCode.address,
                    "bankName" to foundedSwiftCode.bankName,
                    "countryISO2" to foundedSwiftCode.countryISO2.uppercase(),
                    "countryName" to foundedSwiftCode.countryName.uppercase(),
                    "isHeadquarter" to foundedSwiftCode.isHeadquarter,
                    "swiftCode" to foundedSwiftCode.swiftCode,
                    "branches" to branches.map { branch ->
                        mapOf(
                            "address" to branch.address,
                            "bankName" to branch.bankName,
                            "countryISO2" to branch.countryISO2.uppercase(),
                            "isHeadQuarter" to branch.isHeadquarter,
                            "swiftCode" to branch.swiftCode
                        )
                    }
                )
            } else {
                mapOf(
                    "address" to foundedSwiftCode.address,
                    "bankName" to foundedSwiftCode.bankName,
                    "countryISO2" to foundedSwiftCode.countryISO2.uppercase(),
                    "countryName" to foundedSwiftCode.countryName.uppercase(),
                    "isHeadquarter" to foundedSwiftCode.isHeadquarter,
                    "swiftCode" to foundedSwiftCode.swiftCode
                )
            }
        }
    }

    @GetMapping("/country/{countryISO2}")
    fun getSwiftCodesByCountry(@PathVariable countryISO2: String): Map<String, Any>? {
        val swiftCodes = repository.findByCountryISO2(countryISO2)

        if (swiftCodes.isEmpty()) {
            return mapOf("message" to "SWIFT Codes list is empty")
        } else {
            return mapOf(
                "countryISO2" to swiftCodes.first().countryISO2.uppercase(),
                "countryName" to swiftCodes.first().countryName.uppercase(),
                "swiftCodes" to swiftCodes.map { swiftCode ->
                    mapOf(
                        "address" to swiftCode.address,
                        "bankName" to swiftCode.bankName,
                        "countryISO2" to swiftCode.countryISO2.uppercase(),
                        "isHeadquarter" to swiftCode.isHeadquarter,
                        "swiftCode" to swiftCode.swiftCode
                    )
                }
            )
        }
    }

    @PostMapping
    fun addSwiftCode(@RequestBody swiftCode: SwiftCode): Map<String, String> {
        val formattedSwiftCode = swiftCode.copy(
            countryISO2 = swiftCode.countryISO2.uppercase(),
            countryName = swiftCode.countryName.uppercase()
        )

        if (repository.existsById(swiftCode.swiftCode)) {
            return mapOf("message" to "SWIFT Code already exists")
        } else {
            repository.save(formattedSwiftCode)
            return mapOf("message" to "SWIFT Code added successfully")
        }
    }

    @DeleteMapping("/{swiftCode}")
    fun deleteSwiftCode(@PathVariable swiftCode: String): Map<String, String> {
        if(repository.existsById(swiftCode)) {
            repository.deleteById(swiftCode)
            return mapOf("message" to "SWIFT Code deleted successfully")
        } else {
            return mapOf("message" to "SWIFT Code doesn't exist")
        }
    }
}
