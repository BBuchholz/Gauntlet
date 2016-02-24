package com.nineworldsdeep.gauntlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

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

    //NEW METHODS 2015-12-30
    public boolean isAtomic(String input) {
        String temp = input;

        while(temp.length() > 0 && startsWithKeyValTag(temp))
        {
            String key = getFirstKey(temp);
            temp = trimKeyVal(key, temp);
        }

        return temp.trim().length() == 0;
    }


    /**
     * will return a copy of the input string
     * with the last single tag matching the given key removed
     * @param key the key to find (do not include value)
     * @param input the string to trim the keyVal tag from
     * @return a new instance of the input string with the last single tag removed
     */
    public String trimLastKeyVal(String key, String input) {

        String value = extractLastOne(key, input);
        String keyVal = key + "={" + value + "}";
        String temp = replaceLast(input, Pattern.quote(keyVal), " ");

        //clear any double spaces that result
        while (temp.contains("  "))
        {
            temp = temp.replace("  ", " ");
        }

        return temp.trim();
    }

    public String replaceLast(String text, String regex, String replacement) {
        //FROM: http://stackoverflow.com/questions/2282728/java-replacelast
        return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ")", replacement);
    }

    public String trimKeyVal(String key, String input) {

        String value = extract(key, input);
        String keyVal = key + "={" + value + "}";
        String temp = input.replaceFirst(Pattern.quote(keyVal), " ");

        //clear any double spaces that result
        while(temp.contains("  "))
        {
            temp = temp.replace("  ", " ");
        }

        return temp.trim();
    }

    public String getFirstKey(String input) {

        if (!input.contains("={"))
        {
            return null;
        }

//        String firstSegment = input.Split(new string[] { "={" },
//            StringSplitOptions.None)[0];

        String firstSegment = input.split("=\\{")[0];

        String[] words = firstSegment.split(" ");
        String lastWord = words[words.length - 1];

        return lastWord.trim();
    }

    public boolean startsWithKeyValTag(String input) {

        String trimmed = input.trim();

        if (trimmed.indexOf("={") < 0)
        {
            return false;
        }

        if (trimmed.indexOf(" ") < 0)
        {
            return true;
        }

        return trimmed.indexOf("={") < trimmed.indexOf(" ");
    }

    private String extractLastOne(String key, String input) {

        String openKey = key + "={";

        int startIndex = input.lastIndexOf(openKey);

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

    /**
     * replaces value of first key found with valueToSet. if
     * key is not found, appends it to the end
     * @param key
     * @param valueToSet
     * @return
     */
    public String setFirst(String key, String valueToSet, String input) {

        String value = extractOne(key, input);
        String keyVal = key + "={" + value + "}";
        String newKeyVal = key + "={" + valueToSet + "}";

        String replaced =
                input.replaceFirst(Pattern.quote(keyVal),
                                   newKeyVal);

        return replaced;
    }
}
