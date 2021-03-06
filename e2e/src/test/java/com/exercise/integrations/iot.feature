Feature: Testing IOT Rest API
  Background:
    * def oneDayPastFunc =
    """
    function() {
      var DateTimeFormatter = Java.type("java.time.format.DateTimeFormatter");
      var dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      var LocalDateTime = Java.type("java.time.LocalDateTime");
      var localDate = LocalDateTime.now().minusDays(1);
      return dtf.format(localDate);
    }
    """
    * def oneDayNextFunc =
    """
    function() {
      var DateTimeFormatter = Java.type("java.time.format.DateTimeFormatter");
      var dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      var LocalDateTime = Java.type("java.time.LocalDateTime");
      var localDate = LocalDateTime.now().plusDays(1);
      return dtf.format(localDate);
    }
    """
    * def oneDayPast = callonce oneDayPastFunc
    * def oneDayNext = callonce oneDayNextFunc
    * url baseUrl = 'http://localhost:8089'
    * def devicePath = '/api/devices/'
    * def uuid = function(){ return java.util.UUID.randomUUID() + '' }
    * def uuid2 = function(){ return java.util.UUID.randomUUID() + '' }
    * def deviceId = callonce uuid
    * def deviceId2 = callonce uuid2
    * def deviceIdUrl = devicePath + deviceId
    * def deviceId2Url = devicePath + deviceId2

  Scenario: Missing deviceId should return bad request
    Given path devicePath
    And request {"NotDeviceId": '#(deviceId)', "latitude": 41.25, "longitude": -120.9762, "data": {"humidity": 123, "temperature": {"unit": "C", "value": "23.3"}}}
    When method post
    Then status 400

  Scenario: Add data for new device
    Given path devicePath
    And request {"deviceId": '#(deviceId)', "latitude": 41.25, "longitude": -120.9762, "data": {"humidity": 123, "temperature": {"unit": "C", "value": "23.3"}}}
    When method post
    Then status 202
    And match $.deviceId == deviceId
    And match $.timestamp == '#present'

  Scenario: Add data for existing device
    Given path devicePath
    And request {"deviceId": '#(deviceId)', "latitude": 41.25, "longitude": -120.9762, "data": {"humidity": 123, "temperature": {"unit": "F", "value": "167.3"}}}
    When method post
    Then status 202
    And match $.deviceId == deviceId
    And match $.timestamp == '#present'

  Scenario: Add data for second device
    Given path devicePath
    And request {"deviceId": '#(deviceId2)', "latitude": 41.25, "longitude": -1.9762, "data": {"new_field": 123}}
    When method post
    Then status 202
    And match $.deviceId == deviceId2
    And match $.timestamp == '#present'

  Scenario: Add another data for second device
    Given path devicePath
    And request {"deviceId": '#(deviceId2)', "latitude": 41.25, "longitude": -1.9762, "data": {"new_field": 153}}
    When method post
    Then status 202
    And match $.deviceId == deviceId2
    And match $.timestamp == '#present'

  @parallel=false
  Scenario: Retrieve data from given deviceId
    * url baseUrl = 'http://localhost:8090'
    * def sleep = function(pause){ java.lang.Thread.sleep(pause*1000) }
    * call sleep 1
    Given path deviceIdUrl
    And param fromDate = oneDayPast
    And param toDate = oneDayNext
    When method GET
    Then status 200
    And match $.deviceId == deviceId
    And match $.latitude == 41.25
    And match $.longitude == -120.9762
    And match $.data == '#[2]'

  @parallel=false
  Scenario: Retrieve data from second device
    * url baseUrl = 'http://localhost:8090'
    * def sleep = function(pause){ java.lang.Thread.sleep(pause*1000) }
    * call sleep 1
    Given path deviceId2Url
    And param fromDate = oneDayPast
    And param toDate = oneDayNext
    When method GET
    Then status 200
    And match $.deviceId == deviceId2
    And match $.latitude == 41.25
    And match $.longitude == -1.9762
    And match $.data == '#[2]'
    And match $.data == [{"new_field": 123, "timestamp": "#string"}, {"new_field": 153, "timestamp": "#string"}]
    And print oneDayNext
