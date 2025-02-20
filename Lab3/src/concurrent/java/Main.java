import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

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
        
        List<Thread> threads = new ArrayList<>();
        
        Semaphore mutex = new Semaphore(1);
        Semaphore full = new Semaphore(maxItemsPerProducer);
        Semaphore empty = new Semaphore(0);
        
        Buffer buffer = new Buffer();
        
        for (int i = 1; i <= numProducers; i++) {
            Producer producer = new Producer(i, buffer, mutex, full, empty);
            Thread t = new Thread(producer, "produtor");
            t.start();
            threads.add(t);
        }
        
        for (int i = 1; i <= numConsumers; i++) {
            Consumer consumer = new Consumer(i, buffer, mutex, full, empty);
            Thread t = new Thread(consumer, "consumidor");
            t.start();
            threads.add(t);
        }

        for(Thread t : threads) {
            t.join();
        }
    }
}
