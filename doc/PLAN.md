# Simulation Plan

### Introduction

Our team is trying to create a cellular automota simulation that is
extensible enough to simulate various different situations. More specifically, we will be sure to implement five of simulations listed in the basic implentation document. This is the area where it is most flexibile - the variety of scenarios that can be implemented. One area
where it is open is where user input is required to display what specific simulation they desire. Closed classes can be ones related specifically to implementing the changing of state of cells after each time step. 

An area that should be closed is how each specific simulation is implmented. For example, the Game of life simulation has a specific set of rules to how each cell updates its internal state. This implementation should be hidden from other classes.  

### Overview
There are two main large scale components to the code. The first has to be a UI component which displays the cells and their updates in real time to the user. The second is the back-end simulation portion which will handle the logic of updates the state of each cell based on certain parameters and rules. Both of these components can be broken down furthur into subcomponents. 

The simulation portion is larger and can be broken down. The first component is to load the the simulation from an XML file. The XML file will be formatted to a specification that our team designs, and will contain the necessary information to start a simulation. The second component involves setting up the initial simulation state. The XML file will determine this and this initial state will be displayed when the user selects that simulation from the drowdown. The third and most complicated component is the simulation runner. Based on specific rules stated in the program and XML document, this code will update the state of each of the cells in the simulation after each discrete time interval based on the previous state of the simulation board. This is where the UI component will come into play and will visualize the changing of state in the program. The UI components will also allow users to select the simulations they would like to run and displays text output to warn of any errors. Simulation time is also displayed to the user.

Two different data structures that are candidates to represent the grid of cells are a 2D array and a graph. The implementation of these in our design will use an abstract class Grid.java such that the specifics of the underlying data structure are not revealed. The interface of this class would include methods public void initialize(), public Grid update(), public int getState(int x, int y). Grid.java would have subclasses corresponding to these data structures, 2DArray.java and Graph.java. Both of these subclasses would then have implementations according to their specific data structures.

### User Interface
![](https://i.imgur.com/wFCJclI.png)
The user will use their mouse to navigate each component. The user Interface will have the following components: 
1) a menu for the user to chose a simulation to run
2) a window for the user to view the simulaition in real time
3) a window to view the output of the simulation (inluding any erroneous data)
4) options for the user to start, stop, and exit the simulation

The user can also upload their own formatted XML file. 

![](https://i.imgur.com/5g6AOac.jpg)


### Design Details
The user interface component will contain what is displayed to the GUI and handle the decisions made by the user. This component will have the following sub-sections:
1) one to set up the application including methods that will set up the scene and stage
2) one to display the data collected from the simulation. This section will be dependant on the back-end component. 
3) one to handle the input given by the user (such displaying the specific simulation chosen by the )


In order to implement the grid of cells, it is possible to use a 2D array or graph to hold the current state of each grid position. Using a 2D array, the cells will be updated by iterating over each grid position and examining the values of an entry's neighbors. These values will then determine the new state of the cell currently being examined. In order to keep one set of updates independent of the next, a copy of the original grid will be made and used as reference for each round of updates. This copy will ensure that the previous round's values are the ones affecting the update, rather than allowing an update of the current round to influence the next. The reason behind this decision is it prevents the simulation form being affected by the update order - for example by begining in the top  left or bottom right corner each time and moving sequentially up or down. The 2D array is preferred over a graph (implemented by map) for precisely this reason, the ease of making independent updates. Specific method calls would be implemented as follows under this schema:
1) public Grid update(): makes a copy of the grid, iterates over the cells of the copy and at each index updates the original. Returns updated original grid object.
2) public void initialize(): defines the update rules of the grid for a grid instance. Returns void.
3) public int getState(int x, int y): Indexes into array at specified location. Returns the state of a cell at a given index. 



### Use Cases with Game of Life as an Example

We will have the same implementation for all of cells, even if it's a edge cell. So the exceutution for the first two use cases are the same. We will make a copy of the orginal grid, use the copy as the reference, and update the orginal. We will check the states of each of the eight possible neighbors. Then for each of the possible neighbors, it will check is that neighbor acually exists. If it does then it will take the state of that neighbor into consideration based on the rules of the simulation. For Game of Life, we will check to see if that neighbor is dead or alive and add it to a counter if alive. After the program iterates through all eight possible neighbors, the program will decide on how to update the current cell. For Game of Life, the program will check if the current cell is dead or alive, and check the number of alive neighbors. After the program decides how to update the current cell, it will update the orginal grid and move on to the next cell. 

At the end of the main for loop that iterates through all of the cells in the grid, the orginal grid will be complelty updated. We can then send the orginal grid to the UI componant to display the result graphically. For setting the simulation parameters we will use a built in Java parser to get information from the tags. Then there will be methods that are specific to the probabilities of the games that can take the data from the parsed info and set the correct values. The public method from where the XML file is parsed will call a public method in the specific Grid implementation that sets the value. In order to load a new XML file we will have a UI element the user can click to select their properly formatted file. The public method from the UI class will then send that file to the game Simulation class. This class will send that data via public methods from the Grid class that set parameters based on parsed XML. Finally, the old simulation will be cleared and the new simuation will displayed by a call from the Simulation class to a public method in the UI class. 

## add the last two use cases

### Design Considerations

Our team considered two different designs in regards to how a our program will take read XML files and be extensible. The first idea was that we can assume the user will submit and XML file using our format to create a simulation that they desire. A pro for this is ease of use because all the user must do is format the XML file properly. However, a con would be lack of extensibility because they are limited to how our program will run. This led to our second idea which is that the user can upload their own properly formatted XML (with some potential small changes) but they may have to edit some classes in the program. The pro for this is that our program will be much more flexible in allowing for changes but the con is that it requires the use to understand our code well enough to make proper class edits. 

Another design decision made with respect to the data structure is the use of a 2DArray or Graph. In order to implement the 2D array, we chose to use the copy as reference system described in design details. An assumption made in this design is that a cell will never need to be updated independently of its neighbors, as initialize() updates the entire grid at once. Additionally, we assume the size of the grid will be such that creating a reference copy is not computationally infeasible. Other assumptions are with regards to the grid specifications itself, as a 2D grid forces us to create a quadrilateral shape to hold our grid. 


### Team Responsibilities 

This is a team project and all three team members will have responsibility over the high level implementation of each feature. Since the Simulation portion of the project is much more intensive, we will have two people (Achintya, Doherty) who will have primary focus on this and Sarah will be the secondary. On the UI side, Sarah will have the primary focus on that and the other two members will fill secondary roles. These roles are flexible and each team member can contribute to any part of the project. 