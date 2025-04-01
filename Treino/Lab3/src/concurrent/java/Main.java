import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 5) {
            System.out.println("Use: java Main <num_producers> <max_items_per_producer> <producing_time> <num_consumers> <consuming_time>");
            return;
        }
        
        int numProducers = Integer.parseInt(args[0]);
        int maxItemsPerProducer = Integer.parseInt(args[1]);
        int producingTime = Integer.parseInt(args[2]);
        int numConsumers = Integer.parseInt(args[3]);
        int consumingTime = Integer.parseInt(args[4]);
        
        Semaphore mutex = new Semaphore(1);
        Semaphore full = new Semaphore(maxItemsPerProducer);
        Semaphore empty = new Semaphore(0);

        ExecutorService prodThread = Executors.newFixedThreadPool(numProducers);
        ExecutorService consThread = Executors.newFixedThreadPool(numConsumers);
        
        Buffer buffer = new Buffer();
        
        for (int i = 1; i <= numProducers; i++) {
            int id = i;
            
            Runnable producer = () -> {
                while(true) {
                    int item = (int) (Math.random() * 100);
                    System.out.println("Producer " + id + " produced item " + item);

                    try {
                        full.acquire();
                        mutex.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    buffer.put(item);

                    mutex.release();
                    empty.release();
                }
            };

            prodThread.execute(producer);
        }
        
        for (int i = 1; i <= numConsumers; i++) {
            int id = i;

            Runnable consumer = () -> {
                while (true) {
                    try {
                        empty.acquire();
                        mutex.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
        
                    int item = buffer.remove();
                    System.out.println("Consumer " + id + " consumed item " + item);
        
                    mutex.release();
                    full.release();
                }
            };

            consThread.execute(consumer);
        }

        prodThread.shutdown();
        consThread.shutdown();
        // prodThread.awaitTermination(producingTime, TimeUnit.MILLISECONDS);
        // consThread.awaitTermination(consumingTime, TimeUnit.MILLISECONDS);
    }
}