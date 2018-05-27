package com.jeff.drills.multithread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerThread extends Thread {
    private static final Logger LOGGER=LoggerFactory.getLogger(ProducerThread.class);
    private static AtomicInteger threadNum=new AtomicInteger(0);

    private final List<Integer> stack;

    private volatile int index=0;

    public ProducerThread(List<Integer> stack) {
        this.stack = stack;
        this.setName("ProducerThread"+threadNum.incrementAndGet());
    }

    @Override
    public void run() {
        synchronized (stack) {
            try {
                LOGGER.info("producer wait ...");
                while(stack.size() == 1) {
                    stack.wait();
                }

                stack.add(index++);
                stack.notifyAll();
                LOGGER.info("produce notify done: {}",index-1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
