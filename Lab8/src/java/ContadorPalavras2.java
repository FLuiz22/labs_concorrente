import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContadorPalavras2 {
    public static void main(String[] args) throws IOException, InterruptedException {
	    System.out.println("Lab8");
        
        int count = 0;
        
        List<Thread> threads = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();
        
        for(String arg : args) {
            Task task = new Task(arg);
        
            Thread thread = new Thread(task, "trabaiador");
        
            tasks.add(task);
        
            threads.add(thread);
            thread.start();
        }
    
        for(Thread t : threads) {
            t.join();
        }
    
        for(Task t : tasks) {
            count += t.getCount();
        }
        
        System.out.println("Soma de palavras: " + count);

    }

    public static class Task implements Runnable {
        private int count = 0;
        private String arq;

        public Task(String arg) {
            this.arq = arg;
        }

        @Override
        public void run() {
            try {
                this.count += contarPalavras(this.arq);
            } catch (Exception e) {

            }
        }

        public int getCount() {
            return this.count;
        }
    }

    static int contarPalavras(String nomeArquivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
        int count = 0;
        String linha;
        while ((linha = br.readLine()) != null) {
            count += linha.split("\\s+").length;
        }
        br.close();
        return count;
    }
}