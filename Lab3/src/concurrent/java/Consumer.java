import java.util.concurrent.Semaphore;

class Consumer implements Runnable{
    private final Buffer buffer;
    private final int id;
    private Semaphore mutex;
    private Semaphore full;
    private Semaphore empty;
    
    public Consumer(int id, Buffer buffer, Semaphore mutex, Semaphore full, Semaphore empty) {
        this.id = id;
        this.buffer = buffer;
        this.full = full;
        this.empty = empty;
        this.mutex = mutex;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                empty.acquire();
                mutex.acquire();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            int item = buffer.remove();
            System.out.println("Consumer " + id + " consumed item " + item);

            mutex.release();
            full.release();
        }
    }
}