package com.example.SwiftCodeParser.api.repository

import com.example.SwiftCodeParser.api.model.SwiftCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SwiftCodeParserRepository: JpaRepository<SwiftCode, String> {
    fun findBySwiftCode(swiftCode: String): List<SwiftCode>

    fun findByCountryISO2(countryISO2: String): List<SwiftCode>
}
