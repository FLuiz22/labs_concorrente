import java.util.concurrent.Semaphore;

class Producer implements Runnable{
    private final Buffer buffer;
    private final int id;
    private Semaphore mutex;
    private Semaphore full;
    private Semaphore empty;
    
    public Producer(int id, Buffer buffer, Semaphore mutex, Semaphore full, Semaphore empty) {
        this.id = id;
        this.buffer = buffer;
        this.full = full;
        this.empty = empty;
        this.mutex = mutex;
    }
    
    @Override
    public void run() {
        while (true) {
            
            int item = (int) (Math.random() * 100);
            System.out.println("Producer " + id + " produced item " + item);

            try {
                full.acquire();
                mutex.acquire();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            buffer.put(item);

            mutex.release();
            empty.release();
        }
    }
}
