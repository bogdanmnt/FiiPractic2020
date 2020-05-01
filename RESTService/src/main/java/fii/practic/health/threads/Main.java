package fii.practic.health.threads;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        List<MyThread> threads = Stream.iterate(0, n -> n + 1)
                .map(MyThread::new)
                .limit(10)
                .collect(Collectors.toList());

       // threads.forEach(Thread::start);

        List<MyRunnable> runnables = Stream.iterate(0, n -> n + 1)
                .map(MyRunnable::new)
                .limit(10)
                .collect(Collectors.toList());

      //  runnables.stream().map(Thread::new).forEach(Thread::start);

        ExecutorService executor = Executors.newFixedThreadPool(3);

       // runnables.forEach(executor::execute);

      //  executor.shutdown();

        List<Callable<String>> callables = Stream.iterate(0, n -> n + 1)
                .map(MyCallable::new)
                .limit(10)
                .collect(Collectors.toList());

        try {
            List<Future<String>> futures = executor.invokeAll(callables);

            for (Future<String> future : futures) {
                System.out.println(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

    }
}
