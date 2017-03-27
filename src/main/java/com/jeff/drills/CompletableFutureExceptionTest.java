package com.jeff.drills;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureExceptionTest {
    private static final Logger logger = LoggerFactory.getLogger(CompletableFutureExceptionTest.class);

    public static void main(String[] args) {
        logger.debug("start");

        try{
            test();
        }catch (Exception e){
            logger.error("in main",e);
        }

        logger.debug("done");
        dummySleep(5000);
    }

    private static void test() {
        try{
            CompletableFuture.supplyAsync(()->{
                logger.info("start in supplyAsync");
                dummySleep(1000);
                throw new RuntimeException("in supplyAsync");
            }).handle((res,e)->{
                logger.error("get exception: ",e);
                throw new RuntimeException("in handle",e);
            });
        } catch (Exception e) {
            //useless code here
            logger.error("outside error", e);
            throw e;
        }
    }


    private static void dummySleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

}