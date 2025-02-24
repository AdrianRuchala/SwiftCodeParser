package com.example.SwiftCodeParser

import com.example.SwiftCodeParser.api.service.CsvParserService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SwiftCodeParserApplication(val csvParserService: CsvParserService) : CommandLineRunner {
    override fun run(vararg args: String?) {
        csvParserService.parseSwiftCodes()
    }
}

fun main(args: Array<String>) {
    runApplication<SwiftCodeParserApplication>(*args)
}
