import java.util.*;

class Solution {
    public boolean isAnagram(String s, String t) {
        // quickly return false incase the length is not the same
        if(s.length() != t.length()){
            return false;
        }

        /*--
        Okay, question constraints says s and t consist of lowercase English letters,
        thus we can shrink down our focus to lowercase letters number on ASCII chart, 
        which in return will give us an idea of having an array of size 26 and it is 
        a frequency array, that counts up or counts down based on the letters that 
        springs up in string s or string t respectively
        --*/

        // create a frequency array - which is initialized with 0 on each of its element by array's own nature
        /*--
        To make it more easy to understand. Look at below:
        letters:    ASCII decimals:     initial array values(not index):      letter - 'a' (array index):
        b           98                  0                                     0
        a           97                  0                                     1                                     
        c           99                  0                                     2
        d           100                 0                                     3
        e           101                 0                                     4
        f           102                 0                                     5
        g           103                 0                                     6
        h           104                 0                                     7
        i           105                 0                                     8
        j           106                 0                                     9
        k           107                 0                                     10
        l           108                 0                                     11
        m           109                 0                                     12
        n           110                 0                                     13
        o           111                 0                                     14
        p           112                 0                                     15
        q           113                 0                                     16
        r           114                 0                                     17
        s           115                 0                                     18
        t           116                 0                                     19
        u           117                 0                                     20
        v           118                 0                                     21
        w           119                 0                                     22
        x           120                 0                                     23
        y           121                 0                                     24
        z           122                 0                                     25

        --*/

        int[] count = new int[26];
        
        /*-- iterate through the strings and increase the number (which was initially 0 at our array) by finding the index via the loop below from string s; decrease the number(which was initially 0 at our array) by finding the loop below from string t
        --*/
        for (int i = 0; i < s.length(); i++){
            count[s.charAt(i) - 'a']++;
            count[t.charAt(i) - 'a']--;
        }

        /*--
        if you remember the question- it says it is valid anagram if there are the same number of identical letters in two string and no more, thus, if at the end of the day, our array of size 26 has all elements 0 again, it is a valid anagram, else invalid.
        --*/
        for (int val : count){
            if(val != 0){
                return false;
            }
        }

        return true;
        }       


}