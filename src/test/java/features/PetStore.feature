#Author: Sayon Das

Feature: Verify PET store API functionality

  Scenario: Verify user is able to add a new pet to the store
    Given API is set to variable
    When user adds a new pet information using "Add-PET" API
    Then the response status code of "PET-POST" API is 200
    And validate "PET-POST" API response