# RPS Design

We can have a common Weapons that will represent all the data and methods that are common to all the possible weapons. There will be a data field in the class to define what type of weapon it is. In addition, this class contains data to define all of the rules of the game's weapons interaction, including which weapons trump which other weapons in a given match. It will contain various methods which help the Game object manage the state of the RPS game.

There will also be a player class that will be an astract of the player subclasses. These

subclasses will represent the different types of players such as computer generated versus user input. The computer as a player will be able to learn from its outcomes in order to better predict a winning weapon in subsequent turns.

We will also have a Game class that manages updating the state of player interactions through time. For example, it could keep track of score (such as best of 3,5,7 etc.) and then display win/loss screens based which player won. The Game class will first call a function that will set each players move. The method will be in the Player class and the Player objects created in the Game class will be used to call them - Player.setWeapon(Weapon w). 

Then in another method in Game will update the win state. This will call a method from the Weapons class where we need to pass in both the players weapons - whoWon(Player1.weapon, Player2.weapon). Determining who will win allow us to update the score. 

The players will be in a List so that we can easily extend the amount of players in the game. 


# Use Cases
    1) A new game is started with two players, their scores are reset to 0.

    Create two new players in game class.
    Call private reset(List Players) in game class to reset info from previous rounds. 
    
    2) A player chooses his RPS weapon for which he wants to use in the second round.
    
    In Player.java, call public setWeapon(Player p) to set current weapon of player. setWeapon(Player p) will 
    be a void method that will update an instance variable in a Player object. 
    
    3) Given two players choices, one player wins the round and their scores are updated
      From Game.java, the weapon choices can be feed into Weapons.java that will call findWinner(). 
    4) A new choice is added to an existing game and its relationship to all the other choices is updated.
    
    In Weapons.java, create a new weapon object and add it to rules definition. 

    5) A new game is added to the system, with its own relationships for its all its "weapons".
    
    We would just add a new Weapons class that will define the new rules set. Then you can just update the Player class in setWeapon to use the new class. 
