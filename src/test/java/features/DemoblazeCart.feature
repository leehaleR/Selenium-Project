Feature: Cart Functionality

  Background:
    Given The user is in "https://www.demoblaze.com/index.html"

  Scenario:
    Given the user is in the login page
    When they enter "admin" and "admin" as credentials
    And they select and add a "Samsung galaxy s6" to cart
    And proceed to checkout and fill in random data in fields
    Then return the purchase ID