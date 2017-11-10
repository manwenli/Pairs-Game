import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.Math;


public class PlayCard {
    
    //function that generates new unique index for the smart AI
    static int generate_index(List<Integer> num_arr, int my_n){
        //long seed = System.currentTimeMillis ();
        boolean same_num = true;
        int random_number = 0;
        while(same_num){
            int temp_size = 0;
            Random generator = new java.util.Random();
            random_number =  generator.nextInt(my_n);
            for(int i = 0; i < num_arr.size(); i++){
                if(num_arr.get(i) == random_number){
                    break;
                }
                
                temp_size++;
            }
            if(temp_size==num_arr.size()){
                same_num = false;
            }
        }
        return random_number;
    }
    
    
    //bad AI
    //plays the game by flipping a legal random card.
    //returns the total number of flips.
    public static int playRandom(MatchCardGame g){
        
        //get the number of cards in the game
        int size = g.get_n();
        
        while(!g.gameOver()){
            
            //generate first index
            Random generator1 = new java.util.Random();
            
            //System.out.println("rand_1 generated \n");
            
            while(!g.flip( generator1.nextInt(size))) {}
            //System.out.println(g.boardToString());
            //generate second index, different from index 1
            Random generator2 = new java.util.Random();
            
            while(!g.flip( generator2.nextInt(size))) {}
            //System.out.println(g.boardToString());
            
            //compare
            if(!g.wasMatch()){
                g.flipMismatch();
                //System.out.println(g.boardToString());
                
            }
            
        }
        return g.getFlips();
    }
    
    
    //smart AI. Can memorize value of each card flipped.
    //returns the total number of flips
    public static int playGood(MatchCardGame g){
        //create container for flipped cards
        List<Integer> record_flipped = new ArrayList<Integer>();
        
        int size = g.get_n();
        
        while(!g.gameOver()){
            //first two cards
            if(record_flipped.size() == 0){
                //first card
                Random generator1 = new Random();
                int num_1 = generator1.nextInt(size);
                //flip and add to record
                g.flip(num_1);
                //System.out.println(g.boardToString());
                record_flipped.add(num_1);
                //second card
                int num_2 = generate_index(record_flipped,size);
                //flip and add to record
                g.flip(num_2);
                //System.out.println(g.boardToString());
                record_flipped.add(num_2);
                //compare
                if(g.card_arr[num_1].value == g.card_arr[num_2].value){
                    g.card_arr[num_1].matched = true;
                    g.card_arr[num_2].matched = true;
                }
                else{
                    g.flipMismatch();
                    //System.out.println(g.boardToString());
                }
            }
            else{ //after filling in 2 cards in the record_flipped
                //generate the odd card
                int num_3 = generate_index(record_flipped, size);
                //flip
                g.flip(num_3);
                //System.out.println(g.boardToString());
                //add the odd number card to record
                record_flipped.add(num_3);
                
                //compare with those unmatched cards in the record
                boolean found_match = false;
                for(int i = 0; i < record_flipped.size()-1; i++){
                    
                    //if there is a match, break loop
                    if(g.card_arr[record_flipped.get(i)].matched == false && g.card_arr[num_3].value == g.card_arr[record_flipped.get(i)].value){
                        //flip the matched card in the record
                        g.flip(record_flipped.get(i));
                        //System.out.println(g.boardToString());
                        //add the odd number card to record
                        //record_flipped.add(num_3);
                        //mark the two cards as matched
                        g.card_arr[num_3].matched = true;
                        g.card_arr[record_flipped.get(i)].matched = true;
                        found_match = true;
                        break;
                    }
                }//if found a match, enters while loop again if not gameover.
                
                
                //if there is no match,flip card 3
                //and randomly flip another card.
                if(found_match == false){
                    //add the odd number card to record
                    //record_flipped.add(num_3);
                    //g.flipMismatch();
                    int num_4 = generate_index(record_flipped, size);
                    g.flip(num_4);
                    //System.out.println(g.boardToString());
                    //add to record
                    record_flipped.add(num_4);
                    //compare with the odd number card.
                    //if matched
                    if(g.card_arr[num_4].value == g.card_arr[num_3].value){
                        //mark the two cards as matched
                        g.card_arr[num_4].matched = true;
                        g.card_arr[num_3].matched = true;
                    }
                    else{ //if not matched
                        //flip mismatched cards
                        g.flipMismatch();
                        //System.out.println(g.boardToString());
                        
                        for(int i = 0; i < record_flipped.size()-1; i++){
                            
                            //if there is a match, break loop
                            if(g.card_arr[record_flipped.get(i)].matched == false && g.card_arr[num_4].value == g.card_arr[record_flipped.get(i)].value){
                                //flip the matched card in the record
                                g.flip(num_4);
                                //System.out.println(g.boardToString());
                                
                                g.flip(record_flipped.get(i));
                                //System.out.println(g.boardToString());
                                //mark the two cards as matched
                                g.card_arr[num_4].matched = true;
                                g.card_arr[record_flipped.get(i)].matched = true;
                                break;
                            }
                        }
                        //add the even number card to record
                        //record_flipped.add(num_4);
                        
                    }
                }
                
            }
        }
        return g.getFlips();
    }
    
    //plays shuffled MatchCardGames of size 32 a total of N times using playRandom method
    //returns the average number of flips to complete the games
    public static double randomMC(int N){
        MatchCardGame g = new MatchCardGame(32);
        double count_flips = 0;
        for(int i = 0; i < N; i++){
            g.shuffleCards();
            count_flips += playRandom(g);
        }
        return count_flips/N;
        
    }
    
    
    
    //plays shuffled MatchCardGames of size 32 a total of N times using playGood method
    //returns the average number of flips to complete the games
    public static double goodMC(int N){
        MatchCardGame g = new MatchCardGame(32);
        double count_flips = 0;
        for(int i = 0; i < N; i++){
            g.shuffleCards();
            count_flips += playGood(g);
        }
        return count_flips/N;
        
    }
    
    
    
    public static void main(String[] args) {
        //set up reader to take inputs
        java.util.Scanner reader = new java.util.Scanner (System.in);
        
        int n = 16; //game size
        
        MatchCardGame g1 = new MatchCardGame(n);
        
        
        g1.shuffleCards();
        
        /*
         while(!g1.gameOver()) {
         //print board status
         System.out.println(g1.boardToString());
         
         //ask for a card to flip until we get a valid one
         System.out.println("Which card to play?");
         while(!g1.flip(reader.nextInt())) {}
         
         //print board status
         System.out.println(g1.boardToString());
         
         //ask for a card to flip until we get a valid one
         while(!g1.flip(reader.nextInt())) {}
         
         //say whether the 2 cards were a match
         if(g1.wasMatch()) {
	        System.out.println("Was a match!");
         } else {
	        //print board to show mismatched cards
	        System.out.println(g1.boardToString());		
	        System.out.println("Was not a match.");
	        //flip back the mismatched cards
	        g1.flipMismatch();
         }
         }
         
         //Report the score
         System.out.println("The game took " + g1.getFlips() + " flips.");
        */ 
        
        //Using the AIs
        int count;
        MatchCardGame g2 = new MatchCardGame(n);
        g2.shuffleCards();
        count = playRandom(g2);
        System.out.println("The bad AI took " + count + " flips.");
        MatchCardGame g3 = new MatchCardGame(n);
        g3.shuffleCards();
        count = playGood(g3);
        System.out.println("The good AI took " + count + " flips.");
        
        
        
        //Using MCs
        int N = 1000;
        System.out.println("The bad AI took " + randomMC(N) + " flips on average.");
        System.out.println("The good AI took " + goodMC(N) + " flips on average.");
        
    }
    
}
