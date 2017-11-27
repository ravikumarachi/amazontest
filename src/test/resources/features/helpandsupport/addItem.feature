Feature:
  Adding a item into the basket

  Scenario: Add a item to basket
    Given I am on Amazon site
    When I search for "apple iphone 6s 64gb space grey"
    And add the cheapest item to the basket
    Then "apple iphone 6s 64gb space grey" should be added to the basket

  Scenario: Add a iphone accessary to basket
    Given I am on Amazon site
    When I search for "iphone case"
    And add the cheapest item to the basket
    Then I should see total price in the basket

#Test GITHUB

