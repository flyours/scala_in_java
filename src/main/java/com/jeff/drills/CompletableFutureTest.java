package com.jeff.drills;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureTest {
    private static final Logger logger = LoggerFactory.getLogger(CompletableFutureTest.class);
    
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        logger.debug("start");
        CompletableFuture<String> contentsCF = readPage();
        CompletableFuture<List<String>> linksCF = contentsCF.thenApply(CompletableFutureTest::getLinks);

        CompletableFuture<Void> completionStage = linksCF.thenAccept(list -> {
            String a = null;
            logger.debug("a.toString()");
        });

        CompletableFuture<Void> completionStage2 = readPage().thenApply(CompletableFutureTest::getLinks).thenAcceptAsync(list -> {
            logger.debug("second" + list);
        });
        // This will NOT cause an exception to be thrown, because
        // the part that was passed to "thenAccept" will NOT be
        // evaluated (it will be executed, but the exception will
        // not show up)
        logger.debug("after linksCF.thenAccept");
        List<String> result = linksCF.get();
        logger.debug("Got " + result);


        // This will cause the exception to be thrown and
        // wrapped into an ExecutionException. The cause
        // of this ExecutionException can be obtained:
        try {
            completionStage.get();
        } catch (ExecutionException e) {
            logger.debug("Cought " + e);
            Throwable cause = e.getCause();
            logger.debug("cause: " + cause);
        }

        // Alternatively, the exception may be handled by
        // the future directly:
        completionStage.exceptionally(e -> {
            logger.debug("Future exceptionally finished: " + e);
            return null;
        });

        try {
            completionStage.get();
        } catch (Throwable t) {
            logger.debug("Already handled by the future " + t);
        }

        completionStage2.handleAsync((a, b) -> {
            logger.debug("last handle");
            return null;
        });
        dummySleep(10000);
    }

    private static List<String> getLinks(String s) {
        logger.debug("Getting links...");
        List<String> links = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            links.add("link" + i);
        }
        dummySleep(1000);
        return links;
    }

    private static CompletableFuture<String> readPage() {
        return CompletableFuture.supplyAsync(() -> {
            logger.debug("Getting page...");
            dummySleep(1000);
            return "page";
        });
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