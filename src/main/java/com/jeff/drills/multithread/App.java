package com.jeff.drills.multithread;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception {
        List<Integer> stack=new ArrayList<>();

        new ConsumerThread(stack).start();
        new ConsumerThread(stack).start();
        new ConsumerThread(stack).start();
        new ConsumerThread(stack).start();
        Thread.sleep(1000L);

        new ProducerThread(stack).start();
    }
}
