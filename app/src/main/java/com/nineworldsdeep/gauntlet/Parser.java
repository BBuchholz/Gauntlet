package com.nineworldsdeep.gauntlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by brent on 11/30/15.
 */
public class Parser {

    public String extract(String nestedKey, String input){

        if(validateNestedKey(nestedKey) && validate(input)){

            Stack<String> keyStack = getInvertedKeyStack(nestedKey);

            String currentNestedLevelString = input;

            while(keyStack.size() > 0){

                String key = keyStack.pop();

                currentNestedLevelString =
                        extractOne(key, currentNestedLevelString);
            }

            return currentNestedLevelString;

        }else{

            return null;
        }
    }

    String extractOne(String key, String input){

        String openKey = key + "={";

        int startIndex = input.indexOf(openKey);

        if(startIndex > -1){

            int innerContentStartIndex =
                    startIndex + openKey.length();
            int innerContentEndIndex =
                    input.length() - 1; //just a temp value

            int nestingLevel = 0;
            boolean closed = false;

            for(int i = startIndex; !closed; i++){

                if(Character.compare('{', input.charAt(i)) == 0){

                    nestingLevel++;
                }

                if(Character.compare('}', input.charAt(i)) == 0){

                    nestingLevel--;

                    if(nestingLevel == 0){
                        closed = true;
                        innerContentEndIndex = i;
                    }
                }
            }

            return input.substring(innerContentStartIndex,
                    innerContentEndIndex);
        }

        return null;
    }

    Stack<String> getInvertedKeyStack(String nestedKey){

        Stack<String> s = new Stack();

        String[] keys = nestedKey.split("/");

        for(int i = keys.length - 1; i >= 0; i--){

            s.push(keys[i]);
        }

        return s;
    }

    public boolean validateNestedKey(String nestedKey){

        return validateNonEmptyKeyNodes(nestedKey);
    }

    boolean validateNonEmptyKeyNodes(String nestedKey){

        return !nestedKey.contains("//");
    }

    public boolean validate(String input){
        return validateOpenKeyFormat(input) &&
                validateMatchBraces(input);
    }

    boolean validateOpenKeyFormat(String input){

        List<Integer> openBraceIndexes = new ArrayList<>();

        for(int i = 0; i < input.length(); i++){

            char c = input.charAt(i);

            if(Character.compare(c, '{') == 0){
                openBraceIndexes.add(i);
            }
        }

        boolean valid = true;

        for(Integer i : openBraceIndexes){

            if(i < 2){
                return false;
            }

            char alphaNumeric = input.charAt(i - 2);
            char equals = input.charAt(i - 1);

            if(!(Character.isAlphabetic(alphaNumeric) ||
                    Character.isDigit(alphaNumeric)
            ) ||
                    !(Character.compare(equals, '=') == 0)){

                valid = false;
            }
        }

        return valid;
    }

    boolean validateMatchBraces(String input){

        int openBracesCount = 0;
        int closingBracesCount = 0;

        for(char c : input.toCharArray()){

            if(Character.compare(c, '{') == 0)
                openBracesCount++;
            if(Character.compare(c, '}') == 0)
                closingBracesCount++;
        }

        return openBracesCount == closingBracesCount;
    }
}
