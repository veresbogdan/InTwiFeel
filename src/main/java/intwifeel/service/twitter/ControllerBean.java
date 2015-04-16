package intwifeel.service.twitter;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;


import java.io.Serializable;

/**
 * Created by Admin on 23.03.2015.
 */

public class ControllerBean implements Serializable {

    private String statusText;



    public String getStatusText() {
        // The factory instance is re-useable and thread safe.
        // Create new twitter instance

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("vbZg352rqYLxAgvdsISxNlnCA")
                .setOAuthConsumerSecret("LqkeatfqJdVbO1dzDI27fd14TtLbnh8Z930vorLf6KQVE2M4QR")
                .setOAuthAccessToken("3121240089-7BiKCJw7W6XZaUpSJIPAK3dvANruiNqnTHFMUmv")
                .setOAuthAccessTokenSecret("wDOPXEwooZ4wSkds0hIaAUrO8nIHhY58z3SevAIJF7DAw"); 
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        Query query = new Query("Apple Watch");
        try {
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
                statusText += status.getText();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
}
