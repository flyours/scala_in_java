package com.jeff.drills.countwords;


import akka.actor.UntypedActor;
import com.jeff.scala.drills.countwords.FileToCount;
import com.jeff.scala.drills.countwords.WordCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordCountWorker extends UntypedActor {
    private Logger logger = LoggerFactory.getLogger(WordCountWorker.class);
    @Override
    public void onReceive(Object message) {
        if (message instanceof FileToCount) {
            FileToCount c = (FileToCount) message;
            Integer count = c.countWords();
            getSender().tell(new WordCount(c.url(), count), getSelf());
            logger.debug("send back to master done.", new Throwable("WordCountWorker"));
        } else {
            throw new IllegalArgumentException("Unknown message: " + message);
        }
    }
}