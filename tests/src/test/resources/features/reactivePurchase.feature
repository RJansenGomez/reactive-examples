Feature: Purchases made with framework less reactive programming and coroutines (without project reactor)

Scenario: New purchase is completed for a customer
  Given the stock service has enough stock for the purchase made by customer "randomId1"
  When a purchase made by "randomId1" is processed
  Then the stock is deducted
  And the bill service stores the purchase made by customer "randomId1"
  And the finance service increase the BV with the purchase made by customer "randomId1"
  And the notifications service send all the order notifications