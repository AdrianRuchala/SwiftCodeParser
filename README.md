# SwiftCodeParser

Swift Code Parser is a Kotlin Spring Boot application that parses SWIFT codes from a CSV file, stores
them in a PostgreSQL database, and exposes a REST API to retrieve SWIFT code information, add a new
SWIFT code or delete existing SWIFT code. The application runs on http://localhost:8080.

## Technologies Used:

- Kotlin
- Spring Boot
- PostgreSQL
- Docker
- Apache Commons CSV

## Prerequisites
- Docker
- Docker Compose

## Setup

1. Clone the repository:

```bash
git clone https://github.com/AdrianRuchala/SwiftCodeParser.git
```

2. Go to the project folder:

```bash
cd SwiftCodeParser
```

## Run application

In the project folder run:

```bash
docker compose up
```

The application will be available at: http://localhost:8080

## Run tests

To run application test run:

```bash
docker compose -f docker-compose-test.yml up
```

Test results will be available at: SwiftCodeParser/build/reports/tests/test/index.html

## API Documentation

### GET /v1/swift-codes/{swiftCode}

Retrieve SWIFT code information by SWIFT code.

Example request for headquarter SWIFT code:

```bash
curl -X GET http://localhost:8080/v1/swift-codes/ALBPPLPWXXX
```

Response:

```bash
{
  "address": "LOPUSZANSKA BUSINESS PARK LOPUSZANSKA 38 D WARSZAWA, MAZOWIECKIE, 02-232",
  "bankName": "ALIOR BANK SPOLKA AKCYJNA",
  "countryISO2": "PL",
  "countryName": "POLAND",
  "isHeadquarter": true,
  "swiftCode": "ALBPPLPWXXX",
  "branches": [
    {
      "address": "LOPUSZANSKA BUSINESS PARK LOPUSZANSKA 38 D WARSZAWA, MAZOWIECKIE, 02-232",
      "bankName": "ALIOR BANK SPOLKA AKCYJNA",
      "countryISO2": "PL",
      "isHeadQuarter": false,
      "swiftCode": "ALBPPLPWCUS"
    }
  ]
}
```

Example request for branch SWIFT code:

```bash
curl -X GET http://localhost:8080/v1/swift-codes/ALBPPLPWCUS
```

Response:

```bash
{
  "address": "LOPUSZANSKA BUSINESS PARK LOPUSZANSKA 38 D WARSZAWA, MAZOWIECKIE, 02-232",
  "bankName": "ALIOR BANK SPOLKA AKCYJNA",
  "countryISO2": "PL",
  "countryName": "POLAND",
  "isHeadquarter": false,
  "swiftCode": "ALBPPLPWCUS"
}
```

Response for not existing SWIFT CODE:

```bash
{
  "message": "SWIFT Code doesn't exist"
}
```

### GET /v1/swift-codes/country/{countryISO2code}

Retrieve SWIFT code information by country ISO2 code.

Example request:

```bash
curl -X GET http://localhost:8080/v1/swift-codes/country/PL
```

Response for correct country ISO2 code:

```bash
{
  "countryISO2": "PL",
  "countryName": "POLAND",
  "swiftCodes": [
    {
      "address": "STRZEGOMSKA 42C  WROCLAW, DOLNOSLASKIE, 53-611",
      "bankName": "SANTANDER CONSUMER BANK SPOLKA AKCYJNA",
      "countryISO2": "PL",
      "isHeadquarter": true,
      "swiftCode": "AIPOPLP1XXX"
    }, ...
  ]
}
```

Response for incorrect country ISO2 code:

```bash
{
  "message": "SWIFT Codes list is empty"
}
```

### POST /v1/swift-codes

Add new SWIFT code entries to the database for a specific country.

Example request:

```bash
curl -X POST "http://localhost:8080/v1/swift-codes" ^
-H "Content-Type: application/json" ^
-d "{\"address\": \"Test\", \"bankName\": \"test\", \"countryISO2\": \"TE\", \"countryName\": \"TEST\", \"isHeadquarter\": true, \"swiftCode\": \"TESTAAAAXXX\"}"
```

Response if SWIFT code is added successfully:

```bash
{
  "message":"SWIFT Code added successfully"
}
```

Response if SWIFT code already exists:

```bash
{
  "message":"SWIFT Code already exists"
}
```

### DELETE /v1/swift-codes/{swift-code}

Delete SWIFT code data if SWIFT code matches the one in database.

Example request:

```bash
curl -X DELETE "http://localhost:8080/v1/swift-codes/TESTAAAAXXX"
```

Response if SWIFT code is deleted successfully:

```bash
{
  "message":"SWIFT Code deleted successfully"
}
```

Response if SWIFT code doesn't exists:

```bash
{
  "message":"SWIFT Code doesn't exist"
}
```
