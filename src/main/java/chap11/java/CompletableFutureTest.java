package chap11.java;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        log("start");
        CompletableFuture<String> contentsCF = readPage();
        CompletableFuture<List<String>> linksCF = contentsCF.thenApply(CompletableFutureTest::getLinks);

        CompletableFuture<Void> completionStage = linksCF.thenAccept(list -> {
            String a = null;
            log("a.toString()");
        });

        CompletableFuture<Void> completionStage2 = readPage().thenApply(CompletableFutureTest::getLinks).thenAcceptAsync(list -> {
            log("second" + list);
        });
        // This will NOT cause an exception to be thrown, because
        // the part that was passed to "thenAccept" will NOT be
        // evaluated (it will be executed, but the exception will
        // not show up)
        log("after linksCF.thenAccept");
        List<String> result = linksCF.get();
        log("Got " + result);


        // This will cause the exception to be thrown and
        // wrapped into an ExecutionException. The cause
        // of this ExecutionException can be obtained:
        try {
            completionStage.get();
        } catch (ExecutionException e) {
            log("Cought " + e);
            Throwable cause = e.getCause();
            log("cause: " + cause);
        }

        // Alternatively, the exception may be handled by
        // the future directly:
        completionStage.exceptionally(e -> {
            log("Future exceptionally finished: " + e);
            return null;
        });

        try {
            completionStage.get();
        } catch (Throwable t) {
            log("Already handled by the future " + t);
        }

        completionStage2.handleAsync((a, b) -> {
            log("last handle");
            return null;
        });
        dummySleep(10000);
    }

    private static List<String> getLinks(String s) {
        log("Getting links...");
        List<String> links = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            links.add("link" + i);
        }
        dummySleep(1000);
        return links;
    }

    private static CompletableFuture<String> readPage() {
        return CompletableFuture.supplyAsync(() -> {
            log("Getting page...");
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

    private static void log(String msg) {
        System.out.format("[%s] (%s) %s\n", new Date().toString(), Thread.currentThread().getName(), msg);
    }
}