Feature: Movement Service API Tests

  Background:
    * url baseUrl
    * def root = '/api/movimientos'
    * header Content-Type = 'application/json'

    * def LocalDateTime = Java.type('java.time.LocalDateTime')
    * def DateTimeFormatter = Java.type('java.time.format.DateTimeFormatter')
    * def formatter = DateTimeFormatter.ofPattern('yyyy-MM-dd\'T\'HH:mm:ss')

    * def yesterdayTimestamp = formatter.format(LocalDateTime.now().minusDays(1))
    * def nowTimestamp = formatter.format(LocalDateTime.now().plusHours(1))

  Scenario: Create a new account
    * def accountRequest =
    """
    {
      "clienteId": 1,
      "numeroCuenta": "1234567",
      "tipoCuenta": "AHORRO",
      "saldoInicial": 0.00,
      "estado": true
    }
    """
    Given path '/api/cuentas'
    And request accountRequest
    When method POST
    Then status 201
    And match response contains
    """
    {
      "clienteId": "#number",
      "numeroCuenta": "#string",
      "tipoCuenta": "#string",
      "saldoInicial": "#number",
      "saldoActual": "#number",
      "estado": "#boolean",
      "createdAt":null,
      "updatedAt":null
    }
    """

  Scenario: Create a new movement
    * def movementRequest =
    """
    {
      "cuentaId": "1234567",
      "tipoMovimiento": "CREDITO",
      "valor": 500.00,
      "saldo": 500.00,
      "descripcion": "Test deposit"
    }
    """
    Given path root
    And request movementRequest
    When method POST
    Then status 201
    And match response contains
    """
    {
      "id": "#number",
      "cuentaId": "#string",
      "fecha": '#present',
      "tipoMovimiento": "#string",
      "valor": "#number",
      "saldo": "#number",
      "descripcion": "#string",
      "createdAt": '#present'
    }
    """
    * def movementId = response.id

  Scenario: Get movement by ID
    * def movementId = 1
    Given path root + '/' + movementId
    When method GET
    Then status 200
    And match response contains
    """
    {
      "id": "#number",
      "cuentaId": "#string",
      "fecha": '#present',
      "tipoMovimiento": "#string",
      "valor": "#number",
      "saldo": "#number",
      "descripcion": "#string",
      "createdAt": '#present'
    }
    """

  Scenario: Get movements by account ID
    * def accountId = "1234567"
    Given path  root + '/account/' + accountId
    When method GET
    Then status 200
    And match response == '#[]'
    And match each response contains
    """
    {
      "id": "#number",
      "cuentaId": "#string",
      "fecha": '#present',
      "tipoMovimiento": "#string",
      "valor": "#number",
      "saldo": "#number",
      "descripcion": "#string",
      "createdAt": '#present'
    }
    """

  Scenario: Get movements by account ID and date range
    * def accountId = "1234567"
    Given path  root + '/account/' + accountId + '/date-range'
    And param startDate = yesterdayTimestamp
    And param endDate = nowTimestamp
    When method GET
    Then status 200
    And match response == '#[]'
    And assert response.length == 1

  Scenario: Get movements by account ID and type
    * def accountId = "1234567"
    * def movementType = 'CREDITO'
    Given path  root + '/account/' + accountId + '/type/' + movementType
    When method GET
    Then status 200
    And match response == '#[]'
    And assert response.length == 1

  Scenario: Delete movement
    * def movementId = 1
    Given path  root + '/' + movementId
    When method DELETE
    Then status 204
