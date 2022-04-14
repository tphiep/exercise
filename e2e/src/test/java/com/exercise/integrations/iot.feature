#Feature: Testing IOT Rest API
#  Background:
#    * url iotServiceUrl + '/devices'
#
#  Scenario: Testing save device data
#    Given request {"device": "xyz123", "latitude": 41.25, "longitude": -120.9762, "data": "humidity": 123, "temperature": {"unit": "C", "value": "23.3"}}
#    When method post
#    Then status 201
#
#    Given path id
#    When method get
#    Then status 200
#    And match response == {"device": "xyz123", "latitude": 41.25, "longitude": -120.9762, "data": {"humidity": 123, "temperature": {"unit": "C", "value": "23.3"}, "timestamp":"2018-08-16T02:00:39.000Z"}}

Feature:
  Background:
    * url baseUrl = 'http://localhost:8089'
    * def pingPath = '/api/ping/'

  Scenario: Ping to service
    Given path pingPath
    When method get
    Then status 200
    And match response == {status:200}