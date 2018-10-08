# Score DashBoard

Before running the code make sure the zookeeper server is running, by executing the command from zookeeper ensemble:
        
        bash zkServer.sh start 

**Program can be executed as follows:**
        
1. Clone the code from github repository

2. Run **make** in the path the folder is downloaded.

3. Once make is done, Run   **ant**
                
4. In one of the terminal run watcher and in other terminal(s) run player as follows:

**Run Watcher :** 
                      
    ./run.sh watcher [ip address:port] [count of number of scores to be displayed]
        
Note:  number of scores to be displayed should be an integer
    
**Run  Player :**
    **a. Automated process**
            
    ./run.sh player [ip address:port] [player name] [count of number of scores to be displayed] [delay between posting scores] [score mean]
        
The process is automated such that player keeps giving scores og the count number of times given as input argument. Once the count is done player exits the game. When player is active ** next to player is shown while printing the scores. When the player exits the game it takes upto two tick times to update the scores and "**" is removed.
Even when the player exits the game , ** next to the player is disappeared
        
**b. Just to create a player**
    
    ./run.sh player [ip address:port] [player name]
    
            
    
 
    
    
