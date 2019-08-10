package com.jeff.drills;

import java.util.Random;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * some points:
 * 1. ThreadPoolExecutor thread is created only when first task come
 * 2. CompletionService internal has task completion queue which store future in completion order
 */

public class CompletionServiceTest {
    private static AtomicInteger index =new AtomicInteger(1);
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CompletionService<Integer> cs = new ExecutorCompletionService<>(executor);
        cs.submit(CompletionServiceTest::getPrice);
        cs.submit(CompletionServiceTest::getPrice);
        cs.submit(CompletionServiceTest::getPrice);

        for (int i = 0; i < 3; i++) {
            Integer r = cs.take().get();
            System.out.println(r);
        }

        System.out.println("main end");
    }

    private static Integer getPrice() {
        int andIncrement = index.getAndIncrement();
        System.out.println("start:"+ andIncrement);
        Random random=new Random();
        int i=random.nextInt(1000);
        try {
            Thread.sleep(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("index: "+andIncrement+" sleep:"+i);
        return i;
    }
}
