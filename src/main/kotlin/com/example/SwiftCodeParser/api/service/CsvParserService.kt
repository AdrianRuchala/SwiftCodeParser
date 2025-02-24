package com.example.SwiftCodeParser.api.service

import com.example.SwiftCodeParser.api.model.SwiftCode
import com.example.SwiftCodeParser.api.repository.SwiftCodeParserRepository
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.stereotype.Service
import java.io.InputStreamReader

@Service
class CsvParserService(val repository: SwiftCodeParserRepository) {
    fun parseSwiftCodes() {
        val inputFile =
            javaClass.classLoader.getResourceAsStream("Interns_2025_SWIFT_CODES - Sheet1.csv")
                ?: throw IllegalArgumentException("File not found")

        val reader = InputStreamReader(inputFile)
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())

        val swiftCodes = mutableListOf<SwiftCode>()

        for (record in csvParser) {
            val swiftCode = record["SWIFT CODE"]
            val bankName = record["NAME"]
            val address = record["ADDRESS"]
            val countryISO2 = record["COUNTRY ISO2 CODE"]
            val countryName = record["COUNTRY NAME"]
            val isHeadquarter = swiftCode.endsWith("XXX")

            val swift = SwiftCode(
                address,
                bankName,
                countryISO2,
                countryName,
                isHeadquarter,
                swiftCode
            )

            swiftCodes.add(swift)
        }

        repository.saveAll(swiftCodes)
    }
}
