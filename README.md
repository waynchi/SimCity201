team30
======

SimCity201 Project Repository for CS 201 students

Vikrant Singhal:
My contribution:
Entire Housing Scenario:
1. Creation and implementation of 4 roles: HousingResidentRole, HousingRepairManRole, HousingRenterRole and HousingOwnerRole.
2. GUIs and interfaces for each of the above roles.
3. Objects in the background, which include, Item, House (which can be of two types: Villa and Apartment), Apartments (the apartment complex), and GUIs for each one of them.
4. Unit tests for all the roles mentioned in 1.
5. Manual construction of house layout.
6. Over 4200 lines of code (excluding repeating or empty part of interfaces and mock agents).

Partially/unimplemented parts:
1. The RepairShop has been created, and is supposed to be used by the RepairMan, which it does. But details of repair shop (for example: layout of the shop) yet to be covered.
2. The resident(HousingResidentRole) does not purchase things to cook from the market.
3. A resident can run over objects.
4. This implementation allows only one resident per house.
5. Not modified my restaurant for V1, hence not integrated it in V1.

Parts not integrated in the project:
1. The repair man exists, but doesn't actually do his job, to reduce complexity of PeopleAgent's scheduler for v1. The repair man's functionality, however, has been fully tested, and it works properly.
2. Renter and owner interactions, although fully tested and implemented, haven't been put inside V1 for the sake of simplicity.
3. Apartment complex(Apartments) has been implemented, and the GUIs of roles have been fully adapted and tested for the concept. But for the sake of simplicity, it has been left out.

Improvements for v2:
1. Implementing A* algorithm to prevent residents from colliding with objects or going through walls.
2. Resident's need for markets, to be created.
3. Integration of the parts that had been already implemented (for example: the concept of apartment complex(Apartments), the job of repair man(HousingRepairManRole)), but excluded from v1.
4. My own restaurant has to be modified and integrated in V2.
