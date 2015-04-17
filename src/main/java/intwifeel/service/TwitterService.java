package intwifeel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;

@Service
public class TwitterService extends BaseService {

    @Autowired
    private Twitter twitter;

    //TODO this must be asynchronous
    public void searchForTweet(String word, Integer no) {
        Query query = new Query(word);
        query.setCount(no);
        try {
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
