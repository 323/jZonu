import java.util.*;
import java.io.*;
import java.io.UnsupportedEncodingException; 
import org.apache.commons.lang3.StringUtils;
import java.sql.*;

class jZonu {
    public static void main(String[] args) {
        //jZonu - A Java re-write of Zonu
        //Coded by 323/tritutri
        HTTPRequest request = new HTTPRequest();
        String searchstring1, searchstring2;
        String[] frontPageSources = new String[15]; //14 because 15 posts
        String[][] frontPageContent = new String[15][7]; //14 because 15 posts, and 6 for the 7 things needed to be saved
        String source;
        request.setURL("http://www.secretareaofvipquality.net/saovq/");
        source = request.get();
        //max threads on the front page of saovq is 15
        for(int i=0; i<15; i++) {
            searchstring1 = "<a name=\"" + (i+1) + "\"></a>";
            frontPageSources[i] = StringUtils.substringBetween(source, searchstring1, "<div class=\"finalreplies\">"); //Get the source of that post only
            frontPageContent[i][0] = StringUtils.substringBetween(frontPageSources[i], "<h2><a href=\"/saovq/kareha.pl/", "/l50\" rel=\"nofollow\">"); //get thread number from URL
            frontPageContent[i][1] = StringUtils.substringBetween(frontPageSources[i], "rel=\"nofollow\">", "<small>"); //get thread name
            frontPageContent[i][2] = StringUtils.substringBetween(frontPageSources[i], "<small>(", ")</small>"); //get number of replies
            frontPageContent[i][3] = StringUtils.substringBetween(frontPageSources[i], "1</a></span> Name:  <span class=\"postername\">", "</span>"); //get poster name
            frontPageContent[i][4] = StringUtils.substringBetween(frontPageSources[i], "<span class=\"postertrip\">", "</span>"); //get poster tripcode
            frontPageContent[i][5] = StringUtils.substringBetween(frontPageSources[i], "</span> : ", "  <span class=\"delete"); //get post date
            frontPageContent[i][6] = StringUtils.substringBetween(frontPageSources[i], "replytext\">", "</div> </div>"); //get post content
        }
        
    }
}