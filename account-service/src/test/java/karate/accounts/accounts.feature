Feature: Account Service API Tests

  Background:
    * url baseUrl
    * def root = '/api/cuentas'
    * header Content-Type = 'application/json'

  Scenario: Create a new account
    * def accountRequest =
    """
    {
      "clienteId": 1,
      "numeroCuenta": "123456789",
      "tipoCuenta": "AHORRO",
      "saldoInicial": 1000.00,
      "estado": true
    }
    """
    Given path root
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

  Scenario: Get account by number
    * def accountNumber = "123456789"
    Given path root + '/number/' + accountNumber
    When method GET
    Then status 200
    And match response contains
    """
    {
      "clienteId": "#number",
      "numeroCuenta": "#string",
      "tipoCuenta": "#string",
      "saldoInicial": "#number",
      "saldoActual": "#number",
      "estado": "#boolean",
      "createdAt": '#present',
      "updatedAt": '#present'
    }
    """

  Scenario: Get accounts by client ID
    * def clientId = 1
    Given path root + '/client/' + clientId
    When method GET
    Then status 200
    And match response == '#[]'
    And match each response contains
    """
    {
      "clienteId": "#number",
      "numeroCuenta": "#string",
      "tipoCuenta": "#string",
      "saldoInicial": "#number",
      "saldoActual": "#number",
      "estado": "#boolean",
      "createdAt": '#present',
      "updatedAt": '#present'
    }
    """

  Scenario: Get all accounts
    Given path root
    When method GET
    Then status 200
    And match response == '#[]'

  Scenario: Update account status
    * def accountId = "123456789"
    * def updateRequest =
    """
    {
      "estado": false
    }
    """
    Given path root + '/' + accountId
    And request updateRequest
    When method PATCH
    Then status 200
    And match response.estado == false

  Scenario: Delete account
    * def accountId = "123456789"
    Given path root + '/' + accountId
    When method DELETE
    Then status 204
