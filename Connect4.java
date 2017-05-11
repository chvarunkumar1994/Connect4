/** Assignment 4 - To develop a Connect 4 Game using multi-threading concepts 
 * Making methods notify() and wait()
 * Player 1 and 2 win when they place their coins consecutively four times either
 * VERTICALLY OR DIAGNOLLY
 * */

//Importing java standard libraries
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
import java.util.concurrent.ExecutorService;


//Defining Game class as per the requirements of Assignment4
public class Game {
    
    //Defining a set of member variables 
    private static int GameBoard[][];
    private static int insertions;
    private static boolean checkSwitch, checkCompletion, print;
    private static Random r;
    
    //Creating the main method to start execution of the Game
    public static synchronized void main(String[] args) throws InterruptedException {
        int i, j;
        
        //Initializing the Game Board
        GameBoard = new int[7][8];
        for( i = 0; i < 7; i++)
            for( j = 0; j < 8; j++)
                GameBoard [i][j] = 0;
        
        //A set of condition variables for the threads to communicate
         insertions = 0;
         checkSwitch = false;
         checkCompletion = false;
         r = new Random();
         
         //Creating a ThreadPool for the Players and Referee
         ExecutorService executorService = Executors.newCachedThreadPool();
        
         //Creating a Referee thread and two PlayerNos
         executorService.execute(new Referee());
         executorService.execute(new Player(1));
         Thread.sleep(1000);
         executorService.execute(new Player(2));
         
         //Awaiting their termination
         Thread.sleep(1000);
         executorService.shutdown();
         executorService.awaitTermination(1, TimeUnit.MINUTES);
    }
    
    
    /**
     * Defining a set of setter and getter methods to access 
     * private the member variables  
     */
    
    
    public static int getRandom(){
        return r.nextInt(7);
    }
    
    public static int getInsertions(){
        return insertions;
    }
    
    public static boolean getCheckSwitch(){
        return checkSwitch;
    }
    
    public static void setInsertions(int e){
        insertions = e;
    }
    
    public static void setCheckSwitch(boolean p){
        checkSwitch = p;
    }
    
      public static boolean getCheckCompletion(){
        return checkCompletion;
    }
    
    public static void setCheckCompletion(boolean e){
        checkCompletion = e;
    }

    public static  int getGameBoard(int i, int j){
        return GameBoard[i][j];
    }
    
    /**
     *
     * @param i - Position of row  on the Game Board
     * @param j - Position of column on the Game Board
     * @param value - Value to be placed on the Board
     * @throws InterruptedException
     */
    public static void setGameBoard(int i, int j, int value) {
        GameBoard[i][j] = value;
    }
    
}

//Defining a Referee for the creation of a referee thread
class Referee implements Runnable{
    
    private int count;
    //Method that performs implementation of the referee threads to be performed
    public synchronized void RefereeTask() throws InterruptedException {
        int PlayerNo, ContinousPlays, NoOfPlays = 0, i, j, k, CheckWinner;
           
            while(true)
            {
                /****INITIALIZING THE GAME***/
                if(Game.getCheckCompletion())
                    break;
                while(Game.getCheckSwitch())
                    Thread.sleep((long)1000);
                if(NoOfPlays == 0)
                {
                    //Acknowledging the start of a Game
                    System.out.print("\nGAME BEGINS NOW !!!");
                    System.out.flush();
                    NoOfPlays++;
                    Game.setCheckSwitch(true);
                    Thread.sleep((long)1000);
                    notify();
                    Thread.sleep((long)1000);
                    continue;
                 }

                //Acknowledging game after completion of every round
                else
                 {
                     System.out.print("\n****Next Round of Play****" );
                      System.out.flush();
                     NoOfPlays++;
                     Thread.sleep(1000);
                 }
                


                //Attaining locks for checking and accessing the GameBoard
                CheckWinner = 0;
             
                    

                    Thread.sleep(100);   

                    /**Checking if any PlayerNo has won the game*/ 
                        if(CheckWinner == 0)
                        {
                           //Checking if any PlayerNo has inserted 4 balls vertically
                            for(j = 0; ( (j < 7) && (CheckWinner == 0) ); j++)
                            {
                                 ContinousPlays = 0;
                                 PlayerNo = 0; 
                                 for(i = 5; i >= 0; i--)
                                {

                                    if((PlayerNo!=0) && (PlayerNo == (Game.getGameBoard(i, j))))
                                        ContinousPlays++;
                                    else
                                    {
                                        ContinousPlays = 1;    

                                        //optimizing
                                        if( i < 3)
                                         break;
                                    }
                                    PlayerNo = (Game.getGameBoard(i, j));


                                    if(ContinousPlays == 4)
                                    {
                                      CheckWinner = PlayerNo;
                                      System.out.print("\nPlayer "+ PlayerNo+" has won!!!" );
                                      // System.out.flush();
                                      System.out.print("\nGame Over\n");
                                       System.exit(0);
                                       System.out.flush();
                                      Game.setCheckSwitch(true);
                                      Game.setCheckCompletion(true);
                                      break;
                                     
                                    }
                                }
                            }   
                        }    

                    //Checking if any PlayerNo has inserted 4 balls along the right inclined diagnol
                    if(CheckWinner == 0)
                    {    
                        //first half of right inclined diagnol
                        for(k = 3, ContinousPlays = 0, PlayerNo = 0; ( (k < 7) && (CheckWinner == 0) ); k++)
                            for(j = k, ContinousPlays = 0, PlayerNo = 0 ; ((j >= 0) && (CheckWinner == 0) ); j--)
                            {
                                for(i = 0; ( (i < 6) && (j >= 0) ); i++, j--)
                                {
                                    if( (PlayerNo != 0) && (PlayerNo == (Game.getGameBoard(i, j))) )
                                        ContinousPlays++;
                                    else
                                    {
                                        ContinousPlays = 1;    


                                    }    
                                    PlayerNo = (Game.getGameBoard(i, j));


                                    if(ContinousPlays == 4)
                                    {
                                        CheckWinner = PlayerNo;
                                        Game.setCheckCompletion(true);;
                                        System.out.print("\nPlayer" +PlayerNo +" has won!!!");
                                         System.out.flush();
                                        System.out.print("\nGame Over\n");
                                         System.exit(0);
                                         System.out.flush();
                                        break;
                                    }
                                }
                            }  

                           //second half of right inclined diagnol
                           for(k = 1, ContinousPlays = 0, PlayerNo = 0; ( (k < 4) && (CheckWinner == 0) ); k++)
                            for(j = 6, ContinousPlays = 0, PlayerNo = 0 ; ((j > 0) && (CheckWinner == 0) );  )
                            {
                                for(i = 1; ( (i < 6) && (j >= 0) ); i++, j--)
                                {
                                    if( (PlayerNo != 0) && (PlayerNo == (Game.getGameBoard(i, j))) )
                                        ContinousPlays++;
                                    else
                                    {
                                        ContinousPlays = 1; 
                                    }    
                                    PlayerNo = (Game.getGameBoard(i, j));


                                    if(ContinousPlays == 4)
                                    {
                                        CheckWinner = PlayerNo;
                                       Game.setCheckCompletion(true);
                                        System.out.print("\nPlayer" +PlayerNo +"has won!!!");
                                        System.out.print("\nGame Over\n");
                                        System.exit(0);
                                        System.out.flush();
                                        break;
                                    }
                                }
                            }
                     }     


                    //Checking if any PlayerNo has inserted 4 balls consectively along the left inclined diagnol
                    if(CheckWinner == 0)
                    {    
                        //first half of left inclined diagnol
                        for(k = 3, ContinousPlays = 0, PlayerNo = 0; ( (k < 7) && (CheckWinner == 0) ); k++)
                            for(j = k, ContinousPlays = 0, PlayerNo = 0 ; ((j >= 0) && (CheckWinner == 0) ); j--)
                            {
                                for(i = 5; ( (i >= 0) && (j >= 0) ); i--, j--)
                                {
                                    if( (PlayerNo != 0) && (PlayerNo == (Game.getGameBoard(i, j))) )
                                        ContinousPlays++;
                                    else
                                    {
                                        ContinousPlays = 1;    


                                    }    
                                    PlayerNo = (Game.getGameBoard(i, j));


                                    if(ContinousPlays == 4)
                                    {
                                        CheckWinner = PlayerNo;
                                        Game.setCheckCompletion(true);
                                        System.out.print("\nPlayer" +PlayerNo +" has won!!!");
                                         //System.out.flush();
                                        System.out.print("\nGame Over\n");
                                         System.exit(0);
                                         System.out.flush();
                                        break;
                                    }
                                }
                            }  

                            //second half of left inclined diagnol
                            for(k = 2;  ((k < 5) && (CheckWinner == 0)); k++)
                            for(j = 6, ContinousPlays = 0, PlayerNo = 0 ; ((j >= 0) && (CheckWinner == 0) ); )
                            {
                                for(i = 4; ( (i >= 0) && (j >= 0) ); i--, j--)
                                {
                                    if( (PlayerNo != 0) && (PlayerNo == (Game.getGameBoard(i, j))) )
                                        ContinousPlays++;
                                    else
                                    {
                                        ContinousPlays = 1;    


                                    }    
                                    PlayerNo = (Game.getGameBoard(i, j));


                                    if(ContinousPlays == 4)
                                    {
                                        CheckWinner = PlayerNo;
                                        Game.setCheckCompletion(true);
                                        System.out.print("\nPlayer %d" +PlayerNo +"has won!!!");
                                         //System.out.flush();
                                        System.out.print("\nGame Over\n");
                                         System.exit(0);
                                         System.out.flush();
                                        break;
                                    }
                                }
                            }  
                   }    

                   //If some PlayerNo has won, releasing locks and exiting the threads                             
                   if(CheckWinner != 0)
                   {

                       Game.setCheckSwitch(true);
                       Game.setCheckCompletion(true);
                       return;
                   }


                   //If the GameBoards are full and ends in a draw
                   else if(NoOfPlays >= 42)
                   {
                       System.out.print("\n Board is full. Its a draw!!\n");
                       Game.setCheckSwitch(true);
                       Game.setCheckCompletion(true);
                       return;
                   }

                   //If there is more space on the GameBoard to play
                   else 
                   {
                       System.out.print("\nNo PlayerNo has won, let the play resume!!\n");
                      //Thread.sleep((long)100);  
                   }
                 Game.setCheckSwitch(true);
                 Thread.sleep((long)1000);
                 notify();
                 Thread.sleep((long)1000);  
                }//end of infinite while
            }//check
    
    //Overriding the run() method of Runnable interface for the threads to execute
    @Override
    public void run(){
        try {
            RefereeTask();
        } 
        catch (InterruptedException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//End of run() method of Referee class
}//End of Referee class


/**Defining a Player Class to create Player Threads
 */

class Player implements Runnable{
    //Member variables to store the PlayerNo number
    private final int PlayerNo;
    
    //Fully Parameterized constructor
    public Player(int p){
        
        PlayerNo = p;
        
    }
   
    //Method that executes the functionality of the PlayerNo thread
    public synchronized void play() throws InterruptedException{
              int i, j, ColumnNo;
              boolean insert;
  
        //continue playing unless game is over
        while(true)
        {
                //waits until referee has completed checking  
              //  System.out.println("Before while");
                 synchronized(this){
                 while( (Game.getCheckSwitch()) != true){
                     
                     if(PlayerNo == 2)
                        wait(23261);
                     else
                         wait(100);
                 }
                 }
                //  System.out.println("After while");
                  insert = false;
                  if(Game.getCheckCompletion())
                    break;
                  while( (insert == false) && (Game.getCheckCompletion() == false))
                  {
                        /**randomly generating ColumnNoition of insertion onto the GameBoard***/
                      // System.out.println("asdlkg" + PlayerNo);
                       ColumnNo = Game.getRandom();

                       //Inserting into the GameBoard
                       for(i = 5; i >= 0; i--)
                        {
                             if( (Game.getGameBoard(i, ColumnNo)) == 0){
                                 Game.setGameBoard(i, ColumnNo, PlayerNo);
                                 insert = true;
                                // System.out.println("Inserted by" + PlayerNo + "  " + ColumnNo);
                                 break;
                             } 
                        }

                    }       
                  
                  //Printing the Game Board when an insertion occurs
                 if(insert == true)
                 {
                     
                     System.out.flush();
                     Thread.sleep(500);
                     System.out.print("\n****Printing Board****\n");
                      System.out.flush();
                     for(i = 0; i < 6; i++)
                     {
                         System.out.print("***************************************************\n");
                          System.out.flush();
                         for (j = 0; j < 7; j++)
                         {
                             switch (Game.getGameBoard(i, j)) {
                                 case 0:
                                     System.out.print("**     ");
                                      System.out.flush();
                                     break;
                                 case 1:
                                     //Printing "Y - Yellow" for Player 1
                                     System.out.print("**  Y  ");
                                      System.out.flush();
                                     break;
                                 case 2:
                                     //Printing "R - Red" for Player 2
                                     System.out.print("**  R  ");
                                      System.out.flush();
                                     break;
                                 default:
                                     System.out.print("**  " + (Game.getGameBoard(i, j)) + "  ");
                                      System.out.flush();
                                     //Incase there are more PlayerNos, used for printing their turns
                                     break;
                             }
                         }

                         System.out.print("**\n");
                          System.out.flush();
                     }
                     System.out.print("***************************************************\n"
                                        + "\nPlayer 1  - 'Y' (yellow)"
                                        + "\nPlayer 2  - 'R' (red)\n" 
                                        + "\nPlayer "+ (PlayerNo)+" has played!\n" );
                     System.out.flush();
                     Thread.sleep((long)(1000));

                 }

             //Releasing lock and letting the PlayerNos contend for lock once again
             Game.setCheckSwitch(false);
             notify();
   
             //Thread.sleep((long)(1000*PlayerNo));

               if(insert == false)
                 break;
            }//End of infinite while
    } //End of play() method  
        
   
    //Overriding the method of Runnable interface for the threads to execute
    @Override
    public void run() {
        
        try {
            play();
        } catch (InterruptedException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

