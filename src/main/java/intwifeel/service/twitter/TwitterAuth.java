package intwifeel.service.twitter;

import org.springframework.stereotype.Component;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Component
public class TwitterAuth {

    private final Twitter twitter;

    public TwitterAuth() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("vbZg352rqYLxAgvdsISxNlnCA")
                .setOAuthConsumerSecret("LqkeatfqJdVbO1dzDI27fd14TtLbnh8Z930vorLf6KQVE2M4QR")
                .setOAuthAccessToken("3121240089-7BiKCJw7W6XZaUpSJIPAK3dvANruiNqnTHFMUmv")
                .setOAuthAccessTokenSecret("wDOPXEwooZ4wSkds0hIaAUrO8nIHhY58z3SevAIJF7DAw");
        TwitterFactory tf = new TwitterFactory(cb.build());

        twitter = tf.getInstance();
    }
    public Twitter getTwitter() {
        return twitter;
    }
}
