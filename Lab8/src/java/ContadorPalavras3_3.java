import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ContadorPalavras3_3 {
    private static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws IOException, InterruptedException {
	    System.out.println("Lab8 - 3.3");

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        
        for(String arg : args) {
            Runnable runnableTask = () -> {
                try {
                    count.addAndGet(contarPalavras(arg));
                } catch (Exception e) {

                }
            };

            executorService.execute(runnableTask);
        }

        executorService.shutdown();
        executorService.awaitTermination(60, TimeUnit.SECONDS);
        
        System.out.println("Soma de palavras: " + count.get());

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