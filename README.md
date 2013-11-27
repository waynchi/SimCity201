team30
======

SimCity201 Project Repository for CS 201 students

# Overall Project Completion

Unlike many other groups, our project is a fully autonomous SimCity, which we are incredibly proud of. 
Not only that, but we have implemented a method involving arraylists and stoplights to completely avoid collisions with vehicles within the city (This can be tested with a button in our Gui)
Currently, our SimCity will run through each day over and over again autonomously deciding what to do.
+ Here are a list of some, but not all, of the most notable actions that a person can do throughout the day.
1. Choice between going to restaurant or eating at home
2. Idling at home (And doing various actions such as playing fussball, reading, watching T.V. etc.) 
3. Waking up and Doing Morning Actions as well as going to sleep.
4. Buying a car if they do not have a car and have money.
5. Withdrawing, depositing, or requesting a loan from the bank.
6. Going to work places either on feet or on car (if they have it).

+ So far, all of these actions will work and show up within either the main gui or the individual guis.

+ Here are a list of some of the features that are working, but not implemented fully for V1
1. People Riding the Bus (working but not implemented)
2. RepairMan for Housing (working but not implemented)
3. Special Waiter (working but not implemented)
4. Apartment Complex (Working but not implemented).
5. Renter and Owner Interaction (Working but not implemented)

+ Here are a list of some, but not all, of the new features we will add for V2
1. Robbers robbing the bank
2. More houses, More Restaurants, and More Buildings
3. A* within the individual buildings
4. Buying food from the market to cook at home.
5. Adding People directly into the City (we only have it partially ready)
6. Collision detection for pedestrians (We have it halfway setup)
7. Better graphical animation and images

We are not limited to the above, but these are some of the things that we will be sure to do first. We will of course finish all of V2 requirements in the end. 

For Further info on the city gui please access: [City Gui](https://github.com/usc-csci201-fall2013/team30/wiki/City-GUI)

If there are any other suggetions, please post on our issue tracker!

# How To Use Our SimCity

Our SimCity runs autonomously. As a result, there is no configuration Panel.
However, there is a config.txt located in the src folder (src/config.txt) that allows for creations of people. 
Every single person will have certain roles (such as Resident, Restaurant Customer, Bank Customer, etc.) However, the configuration file allows for special descriptions that either add a job to the person, or force them to act in a certain manner.
Here is the format of the config file:
+ 1,Description/Job,Name,JobStartTime,JobEndTime
+ 1,RestaurantCook,James,1000,1900

+Special notes
 1. Some random functions of the people agent have been taken away in order to ensure that it will always occur (since we do not have a configuration panel yet). For example, the market chance is 100% if the person has enough money, rather than only 10% as it will be.
 2. The Config File is NOT gauranteed to work. The config file that will be pushed will show all of our features fully, but certain things may break the program. For example, taking away vital characters (such as the host or the cook) will naturally cause the program to freeze and fail. Another example is the restaurant; because we only have one restaurant and the timer is so fast, if too many people crowd towards the restaurant, it will fail (because there might be 10 people waiting in line while the restaurant host,cook, and cashier need to go home because their job has ended, forcing them to stay at the restaurant and ruin their sleep cycle).
 3. Please enjoy our SimCity :) 



## Contribution and Distribution

+ Vikrant Singhal
..*Housing Logic
..*Housing Gui
+ Eric Strode
..*Bank Logic
..*Bank Gui
..*Main Gui Layout
..*Trace Log
+ Yinyi Chen
..*Restaurant Logic
..*Restaurant Gui
..*Market Logic
..*Market Gui
+ Peppy Sisay
..*Transportation Logic (Vehicles and Bus)
..*Transportation Gui
..*SimCity Gui and Layout
+ Zack Tanner
..*Transportation Logic (Vehicles)
..*Transportation Gui
..*City Configuration
..*Person Gui
+ Wayne Chi
..*People Logic
..*People Gui
..*City Configuration

+ I (Wayne Chi, the Team Leader) believe that, although there were differences in skill level, everyone gave 200%  to this project (The extra hundred percent being derived from our sleep) and did an approximately equal amount of work.
+ Not only that, but the roles and contributions we have are loosely defined as we all helped each other out throughout the project, with most people fixing and editing code from all throughout the project.
