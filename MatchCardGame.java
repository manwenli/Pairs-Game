import java.util.*;
public class MatchCardGame {
    public final int n;
    
    //variables, card and alphabet string
    public Card[] card_arr;
    char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    
    //number of flips to be updated
    public int flip = 0;
    
    
    //constructor
    public MatchCardGame(int n){
        this.n = n;
        card_arr = new Card[n];
        //create array of card objects
        for(int i = 0; i < n; i++){
            card_arr[i] = new Card(alphabet[i/4]);
        }
    }
    
    //converts the state of the board to an appropriate String representation.
    public String boardToString (){
        String state = "";
        int i = 0;
        while(i<n){
            for(int j = 0; j < 4; j++){
                if (card_arr[i+j].face == true){
                    state = state + card_arr[i+j].value +"(" + (i+j) + ")" + "|";
                }
                else {
                    state = state + "X"+"(" + (i+j) + ")" + "|";
                }
            }
            state = state + "\n";
            i+=4;
        }
        return state;
    }
    
    
    //plays card number i.
    //If card i cannot be played because it’s face-up, or if i is an invalid card number, then return false.
    public boolean flip(int i){
        
        if((i>=n) || (i<0))
            return false;
        else if( card_arr[i].face == true )
            return false;
        else{
            card_arr[i].face = true;
            flip++;
            return true;
        }
    }
    
    //returns true if the previous pair was a match and returns false otherwise.
    public boolean wasMatch(){
        
        List<Card> faceup = new ArrayList<Card>();
        List<Character> faceup_value = new ArrayList<Character>();
        boolean flag = false;
        //find faceup cards
        for(int i = 0; i < n; i++){
            if(card_arr[i].matched == false && card_arr[i].face == true){
                faceup.add(card_arr[i]);
                faceup_value.add(card_arr[i].value);
            }
        }
        if(faceup_value.get(0)==faceup_value.get(1)){
            Card temp1 = faceup.get(0);
            Card temp2 = faceup.get(1);
            temp1.matched = true;
            temp2.matched = true;
            faceup = null;
            flag = true;
        }
        else{
            
            faceup = null;
        }
        return flag;
    }
    
    //reverts the a mismatched pair to face-down position
    public void flipMismatch (){
        //find faceup cards
        for(int i = 0; i < n; i++){
            if(card_arr[i].face == true && card_arr[i].matched == false){
                card_arr[i].face = false;
            }
        }
    }
    
    //returns true if all cards have been matched and the game is over
    public boolean gameOver (){
        boolean over = true;
        for(int i = 0; i < n; i++){
            if (card_arr[i].matched == false){
                over = false;
                break;
            }
        }
        return over;
    }
    
    //returns the total number of card flips that have been performed so far
    public int getFlips (){
        return flip;
    }
    
    
    
    
    //shuffle the the order of the cards array using random
    public void shuffleCards (){
        long seed = System.currentTimeMillis ();
        for(int i = n-1; i >=0; --i){
            Random generator = new Random(seed); 
            int j = generator.nextInt(i+1);
            Card temp = card_arr[i];
            card_arr[i] = card_arr[j];
            card_arr[j] = temp;
        } 
    }
    
    
    //function return size of game.
    int get_n(){return n;}
    
}


class Card {
    
    //True if number faceup, false if number facedown.
    boolean face = false;
    boolean matched = false;
    
    //value
    char value;
    
    //constructor
    Card (char inputvalue){
        value = inputvalue;
    }
    
}
