// TABELA DE SÍMBOLOS:
// Utiliza HashMap para armazenar identificadores únicos (como variáveis).
// Cada identificador recebe uma posição numérica única.
// Permite consultar rapidamente se já existe e qual é a sua posição.
// O HashMap facilita isso de forma simples e eficiente.

import java.util.HashMap; 


public class TabelaSimbolos {
    private HashMap<String, Integer> tabela = new HashMap<>();
    private int contador = 0;

    public int adicionar(String lexema) {
        if (!tabela.containsKey(lexema)) {
            tabela.put(lexema, contador++);
        }
        return tabela.get(lexema);
    }

    public boolean contem(String lexema) {
        return tabela.containsKey(lexema);
    }

    public int getPosicao(String lexema) {
        return tabela.getOrDefault(lexema, -1);
    }
}