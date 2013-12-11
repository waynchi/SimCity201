CSCI201 - Team30
=======
SimCity201 Project Repository for CS 201 students

# Overall Project Completion

Unlike many other groups, our project is a fully autonomous SimCity, which we are incredibly proud of. 
Not only that, but we have implemented a method involving arraylists and stoplights to completely avoid collisions with vehicles within the city (This can be tested with a button in our Gui)
Currently, our SimCity will run through each day over and over again autonomously deciding what to do.

Here are a list of some, but not all, of the most notable actions that a person can do throughout the day.

1. Choice between going to restaurant or eating at home
2. Idling at home (And doing various actions such as playing fussball, reading, watching T.V. etc.) 
3. Waking up and Doing Morning Actions as well as going to sleep.
4. Buying a car if they do not have a car and have money.
5. Withdrawing, depositing, or requesting a loan from the bank.
6. Going to work places either on feet or on car (if they have it).
7. People Riding the Bus 
8. Special Waiter
9. Apartment Complex
10. Robbers robbing the bank
11. More houses, More Restaurants, and More Buildings
12. Adding People directly into the City 
13. Collision detection for pedestrians 
14. Better graphical animation and images

+ So far, all of these actions will work and show up within either the main gui or the individual guis.

For Further info on the city gui please access: [City Gui](https://github.com/usc-csci201-fall2013/team30/wiki/City-GUI)

If there are any other suggestions, please post on our issue tracker!

# How To Use Our SimCity

V2.1

Our SimCity runs autonomously. As a result, there is no configuration Panel.
However, there is a config.txt located in the src folder (src/config.txt) that allows for creations of people. 
Every single person will have certain roles (such as Resident, Restaurant Customer, Bank Customer, etc.) However, the configuration file allows for special descriptions that either add a job to the person, or force them to act in a certain manner.
Here is the format of the config file:
+ 1,Description/Job,Name,JobStartTime,JobEndTime
+ 1,RestaurantCook,James,1000,1900

To enter a building, simply boot up the application and select one of the buildings (click on it directly in the gui). This should adjust the bottom left panel of your screen
to show what is occurring inside the building. 

On the bottom right section of the screen, we have provided several features. You can add a person to the city (Currently in the works), and you can also add fake vehicles to demonstrate how
the city handles pathing and collision detection. 

+ Special notes
 1. Some random functions of the people agent have been taken away in order to ensure that it will always occur (since we do not have a configuration panel yet). For example, the market chance is 100% if the person has enough money, rather than only 10% as it will be.
 2. The Config File is NOT gauranteed to work. The config file that will be pushed will show all of our features fully, but certain things may break the program. For example, taking away vital characters (such as the host or the cook) will naturally cause the program to freeze and fail. Another example is the restaurant; because we only have one restaurant and the timer is so fast, if too many people crowd towards the restaurant, it will fail (because there might be 10 people waiting in line while the restaurant host,cook, and cashier need to go home because their job has ended, forcing them to stay at the restaurant and ruin their sleep cycle).
 3. It takes time for the City to Start up. The count in the log, is the current time. They wake up at 800. It is a cycle of 2400.
 4. The yellow block zooming around is our bus. The various lamp posts are its bus stops.
 5. Although you can read our log directly, it will be populated with a lot of messages (along with the constant timer/clock), making it somewhat hard to read. We have provided a trace log in the gui for your convenience. This trace log not only keeps track of all the messages, but if you scroll to the right, you can choose which messages to be shown or hidden. 
 6. Please enjoy our SimCity :) 
 
V2.2

As per V2.1, you can still start the SimCity through the config file.
However, now we have now implemented many new methods to run our city as well as modify it. You can find these in the scenarios tab.

#1 importance
Clear World Function. This clears the world of all people, guis, etc. This is vital as it will allow you to completely restart the simulation for normativeBaselineA, normativeBaselineB, and the original config file. 
I understand that this is no longer a necessity since the professor has stated that we can just restart the simulation, but I am still incredibly proud of it.
However, if it does not work, all you have to do is copy the normativeBaselineA or normativeBaselineB into config.txt and restart the simulation.

1) Scenarios that require clear world. We have a multitude of buttons that cover almost all the scenarios. Normative Scenario B, Normative Scenario A, and Start from Config must have the world cleared before they are executed.
2) Other Scenarios. All of other scenarios can be run while the city itself is running. Since these scenarios require specific conditions (For e.g. car crash scenario would require cars to actually be in the city, or it may cause errors) we will control it ourselves and demonstrate it during our presentation.

3) Add People Button. This button allows you to add people directly into the SimCity! There is a drop down menu for jobs; you may also assign other variables such as name, money, and if they have a car or not.





## Contribution and Distribution

* Vikrant Singhal
  * Housing Logic
  * Housing Gui
  * Restaurant Special Waiter
* Eric Strode
  * Bank Logic
  * Bank Gui
  * Main Gui Layout
  * Trace Log
  * Design
* Yinyi Chen
  * Restaurant Logic
  * Restaurant Gui
  * Market Logic
  * Normative Scenario C
  * Market Gui
* Peppy Sisay
  * Transportation Logic (Vehicles)
  * Transportation Logic (Bus)
  * Transportation Gui
  * SimCity Gui and Layout
* Zack Tanner
  * Transportation Logic (Vehicles)
  * Transportation Gui
  * City Configuration
  * Person Gui
  * Add Person Function
* Wayne Chi
  * People Logic
  * People Gui
  * City Configuration
  * Organizing and Combining Features
  * Normative Scenarios A and B

+ I (Wayne Chi, the Team Leader) believe that, although there were differences in skill level, everyone gave 200%  to this project (The extra hundred percent being derived from our sleep) and did an approximately equal amount of work.
+ Not only that, but the roles and contributions we have are loosely defined as we all helped each other out throughout the project, with most people fixing and editing code from all throughout the project.
it 
+ Also, in the end, since it's hard for people to jump in on other tasks (such as my peopleAgent), some people spent a lot of time helping debug which was also useful.
