import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
	    System.out.println("Lab9 ...");
        ArrayBlockingQueue<Integer> numeros = new ArrayBlockingQueue<>(1);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
            while(true) {
                try {
                    Thread.sleep(500);
                    numeros.put(new Random().nextInt(10) + 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        executorService.submit(() -> {
            while(true) {
                try {
                    Thread.sleep(5000);
                    System.out.println("Número: " + numeros.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        executorService.shutdown();
    }
}