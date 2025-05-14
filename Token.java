public class Token {
    public String tipo;
    public String lexema;
    public int posicao;

    public Token(String tipo, String lexema, int posicao) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.posicao = posicao;
    }

    @Override
    public String toString() {
        return tipo + " " + posicao;
    }
}
