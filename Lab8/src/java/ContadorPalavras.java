import java.io.*;

public class ContadorPalavras {
    public static void main(String[] args) throws IOException {
	    System.out.println("Lab8 - 1");

        int count = 0;

        for(String arg : args) {
            count += contarPalavras(arg);
        }

        System.out.println("Soma de palavras: " + count);

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