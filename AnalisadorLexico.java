import java.io.*;
import java.util.*;
import java.util.regex.*;

public class AnalisadorLexico {
    private static final Map<String, String> PALAVRAS_RESERVADAS = new HashMap<>();

    static {
        PALAVRAS_RESERVADAS.put("se", "SE");
        PALAVRAS_RESERVADAS.put("então", "ENTAO");
        PALAVRAS_RESERVADAS.put("senão", "SENAO");
        PALAVRAS_RESERVADAS.put("fim_se", "FIMSE");
        PALAVRAS_RESERVADAS.put("para", "PARA");
        PALAVRAS_RESERVADAS.put("passo", "PASSO");
        PALAVRAS_RESERVADAS.put("até", "ATE");
        PALAVRAS_RESERVADAS.put("fim_para", "FIMPARA");
        PALAVRAS_RESERVADAS.put("leia", "LEIA");
        PALAVRAS_RESERVADAS.put("escreva", "ESCREVA");
        PALAVRAS_RESERVADAS.put("inteiro", "TIPO");
        PALAVRAS_RESERVADAS.put("e", "E");
        PALAVRAS_RESERVADAS.put("ou", "OU");
        PALAVRAS_RESERVADAS.put("não", "NAO");
    }

    public static void main(String[] args) throws IOException {
        File entrada = new File("exemplo.POR");
        File saida = new File("saida.TEM");
        Scanner scanner = new Scanner(entrada);
        BufferedWriter writer = new BufferedWriter(new FileWriter(saida));
        TabelaSimbolos tabela = new TabelaSimbolos();

        Pattern pattern = Pattern.compile(
            "\"[^\"]*\"|[a-zA-Z_][a-zA-Z_0-9]*|[0-9]+|<=|>=|<>|<|>|=|<-|\\+|\\-|\\*|/|\\(|\\)|;"
        );

        while (scanner.hasNextLine()) {
            String linha = scanner.nextLine();
            Matcher matcher = pattern.matcher(linha);

            while (matcher.find()) {
                String lexema = matcher.group();

                String tipo;
                if (PALAVRAS_RESERVADAS.containsKey(lexema)) {
                    tipo = PALAVRAS_RESERVADAS.get(lexema);
                } else if (lexema.matches("[a-zA-Z_][a-zA-Z_0-9]*")) {
                    tipo = "ID";
                } else if (lexema.matches("[0-9]+")) {
                    tipo = "NUMINT";
                } else if (lexema.matches("\"[^\"]*\"")) {
                    tipo = "STRING";
                } else {
                    tipo = switch (lexema) {
                        case "<-" -> "ATR";
                        case "+" -> "OPMAIS";
                        case "-" -> "OPMENOS";
                        case "*" -> "OPMULTI";
                        case "/" -> "OPDIVI";
                        case "<" -> "LOGMENOR";
                        case ">" -> "LOGMAIOR";
                        case "<=" -> "LOGMENORIGUAL";
                        case ">=" -> "LOGMAIORIGUAL";
                        case "=" -> "LOGIGUAL";
                        case "<>" -> "LOGDIFF";
                        case "(" -> "PARAB";
                        case ")" -> "PARFE";
                        case ";" -> "PONTOEVIRGULA";
                        default -> "DESCONHECIDO";
                    };
                }

                int posicao = tipo.equals("ID") ? tabela.adicionar(lexema) : -1;
                writer.write(tipo + " " + posicao + "\n");
            }
        }

        // Adiciona o token especial de fim de arquivo
        writer.write("EOF -1\n");

        writer.close();
        scanner.close();
        System.out.println("Análise léxica concluída.");
    }
}
