package com.icesoft.icefaces.samples.taxi.util;

/**
 * Convience class used to provide various common manipulation methods and functionality
 */
public class StringResource {
      
      /**
       * Method to fix the case of the passed string
       * This will basically ensure only the first letter of each word is capitalized
       *
       *@param the string to capitalize
       *@return capitalized result
       */
      public static String fixCapitalization(String inString) {
          StringBuffer str = new StringBuffer(inString.trim());
          
          if (str.length() == 0) {
              return str.toString();
          }
          
          Character nextChar;
          int i = 0;
          nextChar = new Character(str.charAt(i));
          while (i < str.length()) {
              str.setCharAt(i++, Character.toUpperCase(nextChar.charValue()));
              
              if (i == str.length()) {
                  return str.toString();
              }
              
              // Look for whitespace
              nextChar = new Character (str.charAt(i));
              while (i < str.length()-2 && !Character.isWhitespace(nextChar.charValue())) {
                  nextChar = new Character (str.charAt(++i));
              }
              
              if (!Character.isWhitespace(nextChar.charValue())) {
                  // If not whitespace, we must be at end of string
                  return str.toString();
              }
              
              // Remove all but first whitespace
              nextChar = new Character (str.charAt(++i));
              while (i < str.length() && Character.isWhitespace(nextChar.charValue())) {
                  str.deleteCharAt(i);
                  nextChar = new Character (str.charAt(i));
              }
          }
          
          return str.toString();
      }
}
