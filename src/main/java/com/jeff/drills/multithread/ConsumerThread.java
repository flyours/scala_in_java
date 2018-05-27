package com.jeff.drills.multithread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerThread extends Thread {
    private static final Logger LOGGER=LoggerFactory.getLogger(ConsumerThread.class);

    private final List<Integer> stack;
    private static AtomicInteger threadNum=new AtomicInteger(0);

    public ConsumerThread(List<Integer> stack) {
        this.stack = stack;
        this.setName("ConsumerThread"+threadNum.incrementAndGet());
    }

    @Override
    public void run() {
        synchronized (stack) {
            try {
                while(stack.isEmpty()) {
                    LOGGER.info("consumer wait ...");
                    stack.wait();
                    LOGGER.info("waking up...");
                    Thread.sleep(100L);
                    LOGGER.info("try to wake up, empty? {}",stack.isEmpty());
                }
                LOGGER.info("thread wake up {}",stack.get(0));
                stack.remove(0);
                stack.notifyAll();
                LOGGER.info("consumer notify done");
                Thread.sleep(1000L);
                LOGGER.info("sleep done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
