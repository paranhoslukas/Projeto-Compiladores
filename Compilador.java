//Lucas Barbosa 2207306
//Beatriz Zuim 22204926
//João Berg 2107012
//Thomaz Ortiz 2201143

import javax.swing.*;     // Componentes da interface gráfica (Swing)
import java.awt.*;        // Gerenciadores de layout e componentes de alto nível
import java.io.*;         // Leitura e gravação de arquivos (.POR e .TEM)
import java.util.*;       // Estruturas como HashMap, Scanner, List, Map
import java.util.regex.*; // Expressões regulares usadas para identificar tokens


public class Compilador extends JFrame { // Classe principal do compilador

    JTextArea outputArea = new JTextArea(20, 50); // Área de texto para exibir os resultados

    public Compilador() {// Construtor da classe Compilador
        
        // Configuração da janela
        super("Analisador Lexico");

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

    
    private void escolherArquivo() { // Método para abrir o seletor de arquivos
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();
            analisarArquivo(arquivo);
        }
    }

    private void analisarArquivo(File arquivo) { // Método para analisar o arquivo selecionado
        try {
            Scanner scanner = new Scanner(arquivo);
            StringBuilder resultado = new StringBuilder();
            TabelaSimbolos tabela = new TabelaSimbolos();

            // Criando mapa de palavras reservadas 
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
                "\"[^\"]*\"|[a-zA-Z_][a-zA-Z_0-9]*|[0-9]+|<=|>=|<>|<|>|=|<-|\\+|\\-|\\*|/|\\(|\\)|;"
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
                        // switch tradicional
                        switch (lexema) {
                            case "<-":
                                tipo = "ATR";
                                break;
                            case "+":
                                tipo = "OPMAIS";
                                break;
                            case "-":
                                tipo = "OPMENOS";
                                break;
                            case "*":
                                tipo = "OPMULTI";
                                break;
                            case "/":
                                tipo = "OPDIVI";
                                break;
                            case "<":
                                tipo = "LOGMENOR";
                                break;
                            case ">":
                                tipo = "LOGMAIOR";
                                break;
                            case "<=":
                                tipo = "LOGMENORIGUAL";
                                break;
                            case ">=":
                                tipo = "LOGMAIORIGUAL";
                                break;
                            case "=":
                                tipo = "LOGIGUAL";
                                break;
                            case "<>":
                                tipo = "LOGDIFF";
                                break;
                            case "(":
                                tipo = "PARAB";
                                break;
                            case ")":
                                tipo = "PARFE";
                                break;
                            case ";":
                                tipo = "PONTOEVIRGULA";
                                break;
                            default:
                                tipo = "DESCONHECIDO";
                                break;
                        }
                    }

                    int pos = tipo.equals("ID") ? tabela.adicionar(lexema) : -1;
                    resultado.append(tipo).append(" ").append(pos).append("\n");
                }
            }

            resultado.append("EOF -1\n");
            outputArea.setText(resultado.toString());
            scanner.close();

            // Salva em arquivo também
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("saida.TEM"))) {
                writer.write(resultado.toString());
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao ler o arquivo.");
        }
    }

    
    static class TabelaSimbolos {// Classe da tabela de símbolos (usando HashMap)
        private HashMap<String, Integer> tabela = new HashMap<>();
        private int contador = 0;

        public int adicionar(String lexema) {
            if (!tabela.containsKey(lexema)) {
                tabela.put(lexema, contador++);
            }
            return tabela.get(lexema);
        }
    }

    public static void main(String[] args) { // Método principal para executar o compilador
        SwingUtilities.invokeLater(() -> new Compilador().setVisible(true));
    }
}
