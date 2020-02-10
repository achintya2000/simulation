Simulation
====

This project implements a cellular automata simulator.

Names: Sarah Gregorich, Doherty Guirand, Achintya Kumar

### Timeline

Start Date: Jan 30

Finish Date: Feb 9

Hours Spent: ~75

### Primary Roles


### Resources Used
- JavaFx JavaDoc
- Ruleset documents for all the simulations
- XML reader and builder guides online

### Running the Program

Main class: UI.java

Data files needed: 

Features implemented:
1.  List of available Simulations
    1. Game of Life
    2. Percolation
    3. Segregation
    4. Fire
    5. Wa-Tor
    6. RPS
3. Simulation
    1. Allow for different arrangements of neighbors for both triangles and square grids.
    2. Grid location shapes include squares and triangles. 
    3. Allow for finite and toroidal grid edge types .
    4. We also implemented an additional simulation - Rock Paper Scissors
4. Configuration
    1. Read in an XML file that will contain the initial settings for a simulation.
    Includes type of simulation, author, and title. Data items include grid width
    and height and initial locations for cell types. 
    2. Implemented error checking for the XML file. It will display in an alert
    box the field from the XML that was the cause of error. It checks for correct
    data fields and if data exists. 
    3. Allow for simulation initial configurations to be set by both a list
    of specific locations/states and by random initialization.
    4. Allow for users to save their current state of their simulation run into
    an XML file and load it in to start again later. 
    5. Allow for simulation to be styled. Includes grid outline style and size
    of each grid location.
5. Visualization
    1. Allow users to interact with the simulation dynamically to change the values of its parameters
    2. 


### Notes/Assumptions

Assumptions or Simplifications: 

Interesting data files: All of the XML files are in Resources and display how
our configuration is formatted. Additionally, the XML files labeled BAD show
examples of cases where exception handling will display an error to the user
about which exact key value is causing the error.

Known Bugs:

Extra credit:


### Impressions

