package com.example.SwiftCodeParser.api.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "swift_codes")
data class SwiftCode (
    val address: String,
    val bankName: String,
    val countryISO2: String,
    val countryName: String,
    val isHeadquarter: Boolean,
    @Id
    val swiftCode: String
)
