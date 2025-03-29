import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ContadorPalavras4 {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
	    System.out.println("Lab8 - 4");

        int total = 0;

        List<Future<Integer>> results = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        
        for(String arg : args) {
             Callable<Integer> task = () -> {
                int count = 0;

                try {
                    count = contarPalavras(arg);
                } catch (Exception e) {

                }
                return count;
            };

            Future<Integer> result = executorService.submit(task);

            results.add(result);
        }

        executorService.shutdown();
        
        for(Future<Integer> r : results) {
            total += r.get();
        }
        
        System.out.println("Soma de palavras: " + total);

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