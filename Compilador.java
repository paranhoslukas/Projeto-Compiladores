// Lucas Barbosa 2207306
// Beatriz Zuim 22204926
// João Berg 2107012
// Thomaz Ortiz 2201143

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Compilador extends JFrame {

    JTextArea outputArea = new JTextArea(20, 50);

    public Compilador() {
        super("Analisador Léxico, Sintático e Semântico");

        JButton abrirBtn = new JButton("Abrir arquivo");
        abrirBtn.addActionListener(e -> escolherArquivo());

        JPanel painel = new JPanel();
        painel.add(abrirBtn);

        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);

        this.add(painel, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    private void escolherArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();
            analisarArquivo(arquivo);
        }
    }

    private void analisarArquivo(File arquivo) {
        try {
           Scanner scanner = new Scanner(new InputStreamReader(new FileInputStream(arquivo), "UTF-8"));
            StringBuilder resultado = new StringBuilder();
            TabelaSimbolos tabela = new TabelaSimbolos();
            java.util.List<String> listaDeTokens = new ArrayList<>();
            java.util.List<String> lexemas = new ArrayList<>();

            Map<String, String> palavrasReservadas = new HashMap<>();
            palavrasReservadas.put("se", "SE");
            palavrasReservadas.put("então", "ENTAO");
            palavrasReservadas.put("senão", "SENAO");
            palavrasReservadas.put("fim_se", "FIMSE");
            palavrasReservadas.put("para", "PARA");
            palavrasReservadas.put("passo", "PASSO");
            palavrasReservadas.put("até", "ATE");
            palavrasReservadas.put("fim_para", "FIMPARA");
            palavrasReservadas.put("leia", "LEIA");
            palavrasReservadas.put("escreva", "ESCREVA");
            palavrasReservadas.put("inteiro", "TIPO");
            palavrasReservadas.put("e", "E");
            palavrasReservadas.put("ou", "OU");
            palavrasReservadas.put("não", "NAO");

        Pattern pattern = Pattern.compile(
    "\"[^\"]*\"|[\\p{L}_][\\p{L}_0-9]*|[0-9]+|<=|>=|<>|<|>|=|<-|\\+|\\-|\\*|/|\\(|\\)|;"
);



            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                Matcher matcher = pattern.matcher(linha);

                while (matcher.find()) {
                    String lexema = matcher.group();
                    String tipo;

                    if (palavrasReservadas.containsKey(lexema)) {
                        tipo = palavrasReservadas.get(lexema);
                    } else if (lexema.matches("[a-zA-Z_][a-zA-Z_0-9]*")) {
                        tipo = "ID";
                    } else if (lexema.matches("[0-9]+")) {
                        tipo = "NUMINT";
                    } else if (lexema.matches("\"[^\"]*\"")) {
                        tipo = "STRING";
                    } else {
                        switch (lexema) {
                            case "<-": tipo = "ATR"; break;
                            case "+": tipo = "OPMAIS"; break;
                            case "-": tipo = "OPMENOS"; break;
                            case "*": tipo = "OPMULTI"; break;
                            case "/": tipo = "OPDIVI"; break;
                            case "<": tipo = "LOGMENOR"; break;
                            case ">": tipo = "LOGMAIOR"; break;
                            case "<=": tipo = "LOGMENORIGUAL"; break;
                            case ">=": tipo = "LOGMAIORIGUAL"; break;
                            case "=": tipo = "LOGIGUAL"; break;
                            case "<>": tipo = "LOGDIFF"; break;
                            case "(": tipo = "PARAB"; break;
                            case ")": tipo = "PARFE"; break;
                            case ";": tipo = "PONTOEVIRGULA"; break;
                            default: tipo = "DESCONHECIDO"; break;
                        }
                    }

                    int pos = tipo.equals("ID") ? tabela.adicionar(lexema) : -1;
                    resultado.append(tipo).append(" ").append(pos).append("\n");
                    listaDeTokens.add(tipo);
                    lexemas.add(lexema);
                }
            }

            resultado.append("EOF -1\n");
            outputArea.setText(resultado.toString());
            scanner.close();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("saida.TEM"))) {
                writer.write(resultado.toString());
            }

            analisarSintaticamente(listaDeTokens);
            analisarSemantica(listaDeTokens, lexemas);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler o arquivo.");
        }
    }

    private void analisarSintaticamente(java.util.List<String> tokens) {
        System.out.println("[Análise Sintática]");
        for (int i = 0; i < tokens.size() - 2; i++) {
            if (tokens.get(i).equals("TIPO") &&
                tokens.get(i + 1).equals("ID")) {
                System.out.println("✓ Declaração de variável encontrada.");
            }
        }

        for (int i = 0; i < tokens.size() - 3; i++) {
            if (tokens.get(i).equals("ID") &&
                tokens.get(i + 1).equals("ATR") &&
                tokens.get(i + 2).equals("NUMINT")) {
                System.out.println("✓ Atribuição válida.");
            }
        }

        System.out.println("[Fim da Análise Sintática]");
    }

    private void analisarSemantica(java.util.List<String> tokens, java.util.List<String> lexemas) {
        System.out.println("[Análise Semântica]");

        Set<String> declaradas = new HashSet<>();
        Set<String> palavrasReservadas = new HashSet<>(Arrays.asList(
            "se", "então", "senão", "fim_se", "para", "passo", "até", "fim_para",
            "leia", "escreva", "inteiro", "e", "ou", "não"
        ));

        // Encontra IDs declarados (com ou sem ponto e vírgula)
        for (int i = 0; i < tokens.size() - 1; i++) {
            if (tokens.get(i).equals("TIPO") &&
                tokens.get(i + 1).equals("ID")) {
                declaradas.add(lexemas.get(i + 1));
            }
        }

        // Verifica se IDs usados foram declarados
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).equals("ID")) {
                String nome = lexemas.get(i);
                if (palavrasReservadas.contains(nome)) continue;
                if (!declaradas.contains(nome)) {
                    System.out.println("⚠ Erro semântico: identificador \"" + nome + "\" usado sem declaração.");
                }
            }
        }

        System.out.println("[Fim da Análise Semântica]");
    }

    static class TabelaSimbolos {
        private HashMap<String, Integer> tabela = new HashMap<>();
        private int contador = 0;

        public int adicionar(String lexema) {
            if (!tabela.containsKey(lexema)) {
                tabela.put(lexema, contador++);
            }
            return tabela.get(lexema);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Compilador().setVisible(true));
    }
}
