import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class Sum {
    private static int total = 0;
    private static HashMap<Integer, List<String>> inguais = new HashMap();
    
        public static int sum(FileInputStream fis) throws IOException {
            
            int byteRead;
            int sum = 0;
            
            while ((byteRead = fis.read()) != -1) {
                sum += byteRead;
            }
    
            return sum;
        }
    
        public static FileInputStream convert(String path) throws IOException {
    
            Path filePath = Paths.get(path);
            if (Files.isRegularFile(filePath)) {
                FileInputStream fis = new FileInputStream(filePath.toString());
                return fis;
            } else {
                throw new RuntimeException("Non-regular file: " + path);
            }
        }
    
        public static class Task implements Runnable {
            private FileInputStream fis;
            private int soma;
            private Semaphore multiplex;
            private Semaphore mutex;
            private String name;
    
            public Task(FileInputStream fis, Semaphore multiplex, Semaphore mutex, String name) {
                this.fis = fis;
                this.soma = 0;
                this.multiplex = multiplex;
                this.mutex = mutex;
                this.name = name;
            }
    
            @Override
            public void run() {
                int byteRead;
                try {
                    this.multiplex.acquire();
                    while ((byteRead = fis.read()) != -1) {
                        this.soma += byteRead;
                    }
                    this.mutex.acquire();

                        total += this.soma;

                        if(inguais.containsKey(getSoma())) {
                            inguais.get(getSoma()).add(getFileName());
                        } else {
                            inguais.put(getSoma(), new ArrayList<>());
                            inguais.get(getSoma()).add(getFileName());
                        }

                    this.mutex.release();
                    this.multiplex.release();
            } catch(IOException e) {

            } catch(InterruptedException e) {

            }
        }

        public int getSoma() {
            return this.soma;
        }

        public String getFileName() {
            return this.name;
        }


    }

    public static void main(String[] args) throws Exception {
        List<Task> tasks = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        Semaphore multiplex = new Semaphore(args.length / 2);
        Semaphore mutex = new Semaphore(1);


        if (args.length < 1) {
            System.err.println("Usage: java Sum filepath1 filepath2 filepathN");
            System.exit(1);
        }

        for (String path : args) {
            FileInputStream fis = convert(path);
            Task task = new Task(fis, multiplex, mutex, path);
            Thread t = new Thread(task, "thread");
            tasks.add(task);
            t.start();
            threads.add(t);
        }

        for(Thread t : threads) {
            t.join();
        }

        for(Task t : tasks) {
            System.out.println(t.getFileName() + ": " + t.getSoma());
        }


        Set<Integer> keys = inguais.keySet();

        
        System.out.println("Soma total: " + total + "\n");

        for(Integer k : keys) {
            if(inguais.get(k).size() > 1) {
                String out = "";

                for(String s : inguais.get(k)) {
                    out += s + " ";
                }
                
                System.out.println(k + " " + out.trim() + "\n");
            }
        }
    }
}
