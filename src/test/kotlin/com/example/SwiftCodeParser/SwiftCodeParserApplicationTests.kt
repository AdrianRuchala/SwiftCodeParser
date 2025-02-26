package com.example.SwiftCodeParser

import com.example.SwiftCodeParser.api.model.SwiftCode
import com.example.SwiftCodeParser.api.repository.SwiftCodeParserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureMockMvc
class SwiftCodeParserApplicationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var repository: SwiftCodeParserRepository

    @Test
    fun shouldReturnErrorMessageWhenSWIFTCodeIsNotFound() {
        `when`(repository.findBySwiftCode("SOMESWIFTCODE123")).thenReturn(emptyList())

        mockMvc.perform(get("/v1/swift-codes/SOMESWIFTCODE123"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("SWIFT Code doesn't exist"))
    }

    @Test
    fun shouldReturnSWIFTCodeDetails() {
        val swiftCode = SwiftCode(
            "Some address",
            "Some bank name",
            "SO",
            "Some country name",
            false,
            "SOMESWIFTCODE123"
        )
        `when`(repository.findBySwiftCode("SOMESWIFTCODE123")).thenReturn(listOf(swiftCode))

        mockMvc.perform(get("/v1/swift-codes/SOMESWIFTCODE123"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.address").value("Some address"))
            .andExpect(jsonPath("$.bankName").value("Some bank name"))
            .andExpect(jsonPath("$.countryISO2").value("SO"))
            .andExpect(jsonPath("$.countryName").value("SOME COUNTRY NAME"))
            .andExpect(jsonPath("$.isHeadquarter").value(false))
            .andExpect(jsonPath("$.swiftCode").value("SOMESWIFTCODE123"))
    }

    @Test
    fun shouldReturnErrorMessageWhenSWIFTCodesForCountryIsNotFound() {
        `when`(repository.findByCountryISO2("SOMECOUNTRYISO")).thenReturn(emptyList())

        mockMvc.perform(get("/v1/swift-codes/SOMECOUNTRYISO"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("SWIFT Code doesn't exist"))
    }

    @Test
    fun shouldReturnSWIFTCodesListForCountry() {
        val swiftCode1 = SwiftCode(
            "Some address 1",
            "Some bank name 1",
            "SO",
            "Some country name",
            false,
            "SOMESWIFTCODE123"
        )

        val swiftCode2 = SwiftCode(
            "Some address 2",
            "Some bank name 2",
            "SO",
            "Some country name",
            false,
            "SOMESWIFTCODE321"
        )

        `when`(repository.findByCountryISO2("SO")).thenReturn(
            listOf(swiftCode1, swiftCode2)
        )

        mockMvc.perform(get("/v1/swift-codes/country/SO"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.countryISO2").value("SO"))
            .andExpect(jsonPath("$.countryName").value("SOME COUNTRY NAME"))
            .andExpect(jsonPath("$.swiftCodes[0].address").value("Some address 1"))
            .andExpect(jsonPath("$.swiftCodes[0].bankName").value("Some bank name 1"))
            .andExpect(jsonPath("$.swiftCodes[0].countryISO2").value("SO"))
            .andExpect(jsonPath("$.swiftCodes[0].isHeadquarter").value(false))
            .andExpect(jsonPath("$.swiftCodes[0].swiftCode").value("SOMESWIFTCODE123"))
            .andExpect(jsonPath("$.swiftCodes[1].address").value("Some address 2"))
            .andExpect(jsonPath("$.swiftCodes[1].bankName").value("Some bank name 2"))
            .andExpect(jsonPath("$.swiftCodes[1].countryISO2").value("SO"))
            .andExpect(jsonPath("$.swiftCodes[1].isHeadquarter").value(false))
            .andExpect(jsonPath("$.swiftCodes[1].swiftCode").value("SOMESWIFTCODE321"))
    }

    @Test
    fun shouldReturnErrorMessageWhenSwiftCodeAlreadyExists() {
        given(repository.existsById("SWIFTCODE123")).willReturn(true)

        val requestBody = """
            {
                "address": "Some bank address",
                "bankName": "Some bank name",
                "countryISO2": "SO",
                "countryName": "Some country name",
                "isHeadquarter": false,
                "swiftCode": "SWIFTCODE123"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/v1/swift-codes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("SWIFT Code already exists"))
    }

    @Test
    fun shouldAddANewSWIFTCode() {
        given(repository.existsById("SOMESWIFTCODE123")).willReturn(false)

        val requestBody = """
            {
                "address": "Some bank address",
                "bankName": "Some bank name",
                "countryISO2": "SO",
                "countryName": "Some country name",
                "isHeadquarter": false,
                "swiftCode": "SWIFTCODE123"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/v1/swift-codes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("SWIFT Code added successfully"))
    }

    @Test
    fun shouldReturnErrorMessageWhenSWIFTCodeDoesNotExist() {
        given(repository.existsById("SOMESWIFTCODE123")).willReturn(false)

        mockMvc.perform(delete("/v1/swift-codes/SOMESWIFTCODE123"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("SWIFT Code doesn't exist"))

    }

    @Test
    fun shouldDeleteSWIFTCode() {
        given(repository.existsById("SOMESWIFTCODE123")).willReturn(true)

        mockMvc.perform(delete("/v1/swift-codes/SOMESWIFTCODE123"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.message").value("SWIFT Code deleted successfully"))
    }

    @Test
    fun shouldReturnThatBankIsAHeadquarter() {
        val swiftCode = "SOMESWIFTCODEXXX"

        assertTrue(swiftCode.endsWith("XXX"), "The string should ends with XXX")
    }

    @Test
    fun shouldReturnThatBankIsNotAHeadquarter() {
        val swiftCode = "SOMESWIFTCODE123"

        assertFalse(swiftCode.endsWith("XXX"), "The string should ends with XXX")
    }

    @Test
    fun shouldReturnThatCountryISO2IsUppercase() {
        val countryISO2 = "so"

        assertEquals("SO", countryISO2.uppercase(), "The string should be uppercase")
    }

    @Test
    fun shouldReturnThatCountryNameIsUppercase() {
        val countryISO2 = "Some country name"

        assertEquals("SOME COUNTRY NAME", countryISO2.uppercase(), "The string should be uppercase")
    }

    @Test
    fun shouldReturnThatSWIFTCodeIsEmpty() {
        val swiftCode = ""

        assertFalse(swiftCode.isNotEmpty(), "The string should not be empty")
    }

    @Test
    fun shouldReturnThatSWIFTCodeIsNotEmpty() {
        val swiftCode = "SOMESWIFTCODE123"

        assertTrue(swiftCode.isNotEmpty(), "The string should not be empty")
    }

    @Test
    fun shouldReturnThatFilenameHasCorrectExtension() {
        val fileName = "Interns_2025_SWIFT_CODES - Sheet1.csv"

        assertTrue(fileName.endsWith(".csv"), "Filename extension should be CSV")
    }

    @Test
    fun shouldReturnThatFilenameHasIncorrectExtension() {
        val fileName = "Interns_2025_SWIFT_CODES - Sheet1"

        assertFalse(fileName.endsWith(".csv"), "Filename extension should be CSV")
    }

    @Test
    fun shouldReturnThatCodesAreAssociated() {
        val swiftCode1 = "ABCDEFGHXXX"
        val swiftCode2 = "ABCDEFGH123"

        assertEquals(
            swiftCode1.take(8),
            swiftCode2.take(8),
            "The first 8 letter should be the same for associated banks"
        )
    }

    @Test
    fun shouldReturnThatCodesAreNotAssociated() {
        val swiftCode1 = "ABCDEFGHXXX"
        val swiftCode2 = "AGSGASGA123"

        assertNotEquals(
            swiftCode1.take(8),
            swiftCode2.take(8),
            "The first 8 letter should be the same for associated banks"
        )
    }

    @Test
    fun shouldReturnThatSWIFTCodeHasCorrectLength() {
        val swiftCode = "ABCDEFGH123"

        assertTrue(swiftCode.length == 8 || swiftCode.length == 11, "SWIFT Code should have 8 or 11 characters")
    }

    @Test
    fun shouldReturnThatSWIFTCodeHasIncorrectLength() {
        val swiftCode = "ABCD123"

        assertFalse(swiftCode.length == 8 || swiftCode.length == 11, "SWIFT Code should have 8 or 11 characters")
    }

    @Test
    fun shouldReturnThatCountryISO2CodeHasCorrectLength() {
        val countryISO2 = "US"

        assertEquals(2, countryISO2.length, "Country ISO2 Code should have 2 characters")
    }

    @Test
    fun shouldReturnThatCountryISO2CodeHasIncorrectLength() {
        val countryISO2 = "USA"

        assertNotEquals(2, countryISO2.length, "Country ISO2 Code should have 2 characters")
    }
}
