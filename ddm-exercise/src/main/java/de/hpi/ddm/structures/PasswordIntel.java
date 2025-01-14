package de.hpi.ddm.structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import scala.Char;

@Data @AllArgsConstructor
public class PasswordIntel implements Serializable {

	private static final long serialVersionUID = 1704338361682959287L;
	
    private String alphabet;
    private String user;
    private int id;
    private String pwdHash;
    private String pwdSolution;
    private int pwdLength;
    private String[] hintHashes;
    private String[] hintSolutions;
    private int uncrackedHashCounter;
    private HashSet<Character> knownFalseChars;
    
    public PasswordIntel(String[] csvLine) {
        this.id = Integer.parseInt(csvLine[0]);
        this.user = csvLine[1];
        this.alphabet = csvLine[2];
        this.pwdLength = Integer.parseInt(csvLine[3]);
        this.pwdHash = csvLine[4];
        this.hintHashes = Arrays.copyOfRange(csvLine, 5, csvLine.length);
        this.hintSolutions = new String[this.hintHashes.length];
        this.pwdSolution = null;
        this.uncrackedHashCounter = this.hintHashes.length;
        this.knownFalseChars = new HashSet<Character>();
    }

    public char addFalseChar(String clearText, String hash){
        // Find the index of the cracked hint and add it to the hint solutions.
        for(int i = 0; i < hintHashes.length; ++i){
            if(hintHashes[i].equals(hash)){
                this.hintSolutions[i] = clearText;
                break;
            }
        }
        // Identify the missing char from the alphabet in the clear text and add it to the know false char hashset.
        int missing =  -1;
        for(int i = 0; i < alphabet.length(); ++i){
            boolean found = false;
            for(int j = 0; j < clearText.length(); ++j){
                char b = clearText.charAt(j);
                if(alphabet.charAt(i) == b){
                    found = true;
                }
            }
            if(!found){
                missing = i;
                break;
            }
        }
        this.knownFalseChars.add(alphabet.charAt(missing));
        return alphabet.charAt(missing);
    }

    public void setPwdClearText(String pwdClearText){
        this.pwdSolution = pwdClearText;
    }

    public char[] getAlphabetOfPwd(){
        String reducedAlphabet = "";
        for(char letter : alphabet.toCharArray()){
            if(!knownFalseChars.contains(letter)){
                reducedAlphabet += Character.toString(letter);
            }
        }
        return reducedAlphabet.toCharArray();
    }

    private String stringArrayToString(String[] arr){
        String str = "";
        for(int i = 0; i < arr.length; ++i){
            str += arr[i];
            if(i < arr.length - 1){
                str += ";";
            }
        }
        return str;
    }

    public String toString(){
        String str = "" + this.id + ";" + this.user + ";" + this.alphabet + ";" + this.pwdLength + ";" + this.pwdSolution + ";" + this.stringArrayToString(this.hintSolutions);
        return str;
    }
}
