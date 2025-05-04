Sql queries for data collection

1. Query to show all available cars:
SELECT CarID, Make, Model, Year, DailyCost
FROM Cars
WHERE Available = true;

2. Query to show all cars of a specific make
SELECT CarID, Make, Model, Year, DailyCost, Available
FROM Cars
WHERE Make = 'Ford';

3. Query to show all cars under a specific price
SELECT CarID, Make, Model, Year, DailyCost, Available
FROM Cars
WHERE DailyCost < 50;

4. Query to show all customers with a specific name
SELECT CustomerID, Name, License, Phone
FROM Customers
WHERE Name LIKE '* Stark';

5. Query to show all customers with a specific phone number
SELECT CustomerID, Name, License, Phone
FROM Customers
WHERE Phone = '555-5555';
