import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
	    System.out.println("Lab9 ...");
        ArrayBlockingQueue<Integer> numeros = new ArrayBlockingQueue<>(10000);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
            for(int i = 0; i < 10000; i++) {
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
                    Thread.sleep(500);
                    Integer r = numeros.poll(600, TimeUnit.MILLISECONDS);
                    if(r == null) {
                        break;
                    }
                    System.out.println("Número: " + r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        executorService.shutdown();
    }
}