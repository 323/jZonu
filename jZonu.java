import java.util.*;
import java.io.*;
import java.io.UnsupportedEncodingException; 
import org.apache.commons.lang3.StringUtils;
import java.sql.*;

class jZonu {
    //jZonu - A Java re-write of Zonu
    //Coded by 323/tritutri
    Connection connect = null;
    Statement statement = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    public void main(String[] args) throws Exception {
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
        try {
            Class.forName("com.mysql.jdbc.Driver"); //load jdbc driver
            connect = DriverManager.getConnection("jdbc:mysql://localhost/feedback?" + "user=root2&password=root2"); //connect to sql db
            statement = connect.createStatement(); //allow issuing sql queries to db
            preparedStatement = connect.prepareStatement("insert into CHANARCHIVES.SAOVQ values (?, ?, ?, ?, ?, ?, ?)");
            //later, put the whole SQL part into a loop so it saves all of the content
            preparedStatement.setString(0, frontPageContent[0][0]);
            preparedStatement.setString(1, frontPageContent[0][1]);
            preparedStatement.setString(2, frontPageContent[0][2]);
            preparedStatement.setString(3, frontPageContent[0][3]);
            preparedStatement.setString(4, frontPageContent[0][4]);
            preparedStatement.setString(5, frontPageContent[0][5]);
            preparedStatement.setString(6, frontPageContent[0][6]);
            
            preparedStatement = connect.prepareStatement("SELECT ThreadNumber, ThreadName, NumOfReplies, PosterName, PosterTripcode, PostDate, PostContent from CHANARCHIVES.SAOVQ");
            resultSet = preparedStatement.executeQuery();
            writeResultSet(resultSet);
            
            resultSet = statement.executeQuery("select * from CHANARCHIVES.SAOVQ");
            writeMetaData(resultSet);
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }
    //Just some testing stuff
    private void writeMetaData(ResultSet resultSet) throws SQLException {
        System.out.println("The columns in the table are: ");
        System.out.println("Table : " + resultSet.getMetaData().getTableName(1));
        for (int i=1; i<=resultSet.getMetaData().getColumnCount(); i++){
            System.out.println("Column " + i + " " + resultSet.getMetaData().getColumnName(i));
        }
    }
    //More testing stuff
    private void writeResultSet(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String threadnumber = resultSet.getString("ThreadNumber");
            String threadname = resultSet.getString("ThreadName");
            String numofreplies = resultSet.getString("NumOfReplies");
            String postername = resultSet.getString("PosterName");
            String postertrip = resultSet.getString("PosterTripcode");
            String postdate = resultSet.getString("PostDate");
            String postcontent = resultSet.getString("PostContent");
            System.out.println("Thread Num: " + threadnumber);
            System.out.println("Thread Name: " + threadname);
            System.out.println("Number of Replies: " + numofreplies);
            System.out.println("Poster Name: " + postername);
            System.out.println("Poster Trip: " + postertrip);
            System.out.println("Post Date: " + postdate);
            System.out.println("Post Content: " + postcontent);
        }
    }
    //to close resultSet
    private void close() {
         try {
             if (resultSet != null) {
                 resultSet.close();
             }
             if (statement != null) {
                 statement.close();
             }
             if (connect != null) {
                 connect.close();
             }
        } catch (Exception e) {
            
        }
    }
}