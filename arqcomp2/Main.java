import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

        String diretorio = "E:\\";

        for (int i = 1; i <= 10; i++) {
            String numero = String.format("%02d", i);
            String nomeEntrada = "TESTE-" + numero + ".txt";
            String nomeSaida = "TESTE-" + numero + "-RESULTADO.txt";
            int resultado = 0;

            Path caminhoEntrada = Paths.get(diretorio, nomeEntrada);
            Path caminhoSaida = Paths.get(diretorio, nomeSaida);

            try (
                BufferedReader leitor = Files.newBufferedReader(caminhoEntrada, StandardCharsets.UTF_8);
                BufferedWriter escritor = Files.newBufferedWriter(caminhoSaida, StandardCharsets.UTF_8)
            ){
                String linha;
                Execução objeto = new Execução();
                while ((linha = leitor.readLine()) != null) {
                    objeto.adicionarInstrucao(linha);
                }
                resultado = objeto.calculandoCiclos();
                escritor.write(String.valueOf(resultado)); 
                escritor.newLine();
                System.out.println("Gerado: " + nomeSaida);

            } catch (IOException e) {
                System.err.println("Erro ao processar " + nomeEntrada + ": " + e.getMessage());
            }
        }

        System.out.println("Todos os arquivos foram processados.");
    }
}
