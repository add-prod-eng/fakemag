Feature: User Feature
  As a user, I want to perform user operations

  @UserFeature
  Scenario: Retrieve all users successfully
    Given the user database is populated
    When I request all users
    Then I should receive a list of users