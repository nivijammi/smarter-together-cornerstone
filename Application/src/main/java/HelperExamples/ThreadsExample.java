package HelperExamples;

import java.util.concurrent.*;

/**
 * Both callable task and a CompletableFuture task that both perform a computation
 * and return the value 42 after a delay of 2 seconds.
 */

public class ThreadsExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Callable Example
        Callable<Integer> callableTask = () -> {
            System.out.println("Callable task running...");
            TimeUnit.SECONDS.sleep(2);
            return 42;
        };

        Future<Integer> future = executorService.submit(callableTask);

        // Blocking call to get the result
        int result = future.get();
        System.out.println("Callable task result: " + result);

        // CompletableFuture example
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("CompletableFuture task running...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 42;
        });

        // Non-blocking callback to handle the result
        completableFuture.thenAcceptAsync(res -> {
            System.out.println("CompletableFuture task result: " + res);
        });

        // Do other tasks while CompletableFuture is executing

        // Shutdown the executor service
        executorService.shutdown();
    }

}
