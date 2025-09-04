Feature: Client API integration tests

  Background:
    * def uuid = function() { return java.util.UUID.randomUUID() + '' }
    Given url baseUrl
    And header Accept = 'application/json'
    And header Content-Type = 'application/json'
    * def root = '/api/clientes'
    * def makePayload =
    """
    function(ident, name, estado) {
      return {
        persona: {
          identificacion: ident,
          nombre: name,
          genero: 'M',
          edad: 30,
          direccion: 'Some Address',
          telefono: '123-456-7890'
        },
        contrasena: 'pwd-123',
        estado: estado
      };
    }
    """

  Scenario: CRUD happy path
    # Create
    * def ident = 'KAR-' + uuid().substring(0, 8)
    * def payload = makePayload(ident, 'John Tester', true)
    Given path root
    And request payload
    When method post
    Then status 201
    And match response.clienteid == '#number'
    And match response.persona.identificacion == ident
    And match response.contrasena == null
    * def id = response.clienteid

    # Get by id
    Given path root, id
    When method get
    Then status 200
    And match response.clienteid == id
    And match response.persona.identificacion == ident
    And match response.contrasena == null

    # Get by identificacion
    Given path root, 'identificacion', ident
    When method get
    Then status 200
    And match response.clienteid == id

    # List all should contain the created client
    Given path root
    When method get
    Then status 200
    And match response[*].clienteid contains id
    And match response[*].persona.identificacion contains ident


    # Filter by estado
    Given path root
    And param estado = true
    When method get
    Then status 200
    And match response[*].clienteid contains id
    And match response[*].persona.identificacion contains ident


    # Update partial fields; blank password should be ignored by service
    * def patch =
    """
    {
      "persona": {
        "identificacion": ident,
        "nombre": "New Name",
        "telefono": "999-999-9999"
      },
      "contrasena": "   ",
      "estado": false
    }
    """
    Given path root, id
    And request patch
    When method put
    Then status 200
    And match response.persona.nombre == 'New Name'
    And match response.persona.telefono == '999-999-9999'
    And match response.estado == false
    And match response.contrasena == null

    # Delete
    Given path root, id
    When method delete
    Then status 204

    # Verify not found after delete
    Given path root, id
    When method get
    Then status 404
    And match response.status == 404
    And match response.message contains 'not found'

  Scenario: Creating a client with duplicate identificacion should fail
    * def dupIdent = 'KAR-' + uuid().substring(0, 8)
    * def payload1 = makePayload(dupIdent, 'Alice One', true)
    * def payload2 = makePayload(dupIdent, 'Alice Two', true)

    # First create succeeds
    Given path root
    And request payload1
    When method post
    Then status 201
    * def createdId = response.clienteid

    # Second create fails with 400
    Given path root
    And request payload2
    When method post
    Then status 400
    And match response.status == 400
    And match response.message contains 'already exists'