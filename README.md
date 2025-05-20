# Integrantes
- Lucas Barbosa 2207306
- Beatriz Zuim 22204926
- João Berg 2107012
- Thomaz Ortiz 2201143

# Compilador Simples (.POR → Tabela de Tokens)

Este projeto é um analisador léxico simples para arquivos `.POR`. Ele lê o código-fonte, identifica os tokens e gera uma tabela de símbolos, exibindo o resultado na tela e salvando em `saida.TEM`.

## Funcionalidades

- Interface gráfica para abrir arquivos `.POR`
- Análise léxica: identifica palavras reservadas, identificadores, operadores, números, strings, etc.
- Geração de tabela de símbolos para identificadores
- Exporta a sequência de tokens para o arquivo `saida.TEM`

## Como usar

1. Compile o projeto:
   ```sh
   javac Compilador.java
   ```

2. Execute o programa:
   ```sh
   java Compilador
   ```

3. Na interface, clique em **Abrir arquivo** e selecione um arquivo `.POR` (exemplo: `exemplo.POR`).

4. O resultado da análise léxica aparecerá na tela e será salvo em `saida.TEM`.

## Exemplo de entrada (`exemplo.POR`)

```por
inteiro:idade;
idade <- 18;
se idade > 17 então
    escreva("Maior de idade");
fim_se
```

## Exemplo de saída (`saida.TEM`)

```
TIPO -1
ID 0
PONTOEVIRGULA -1
ID 0
LOGMENOR -1
OPMENOS -1
NUMINT -1
PONTOEVIRGULA -1
SE -1
ID 0
LOGMAIOR -1
NUMINT -1
ID 1
ID 2
ESCREVA -1
PARAB -1
STRING -1
PARFE -1
PONTOEVIRGULA -1
FIMSE -1
EOF -1
```

## Estrutura do Projeto

- `Compilador.java`: Código-fonte principal do analisador léxico.
- `exemplo.POR`: Exemplo de código-fonte de entrada.
- `saida.TEM`: Saída gerada com a lista de tokens.

## Observações

- Apenas identificadores são adicionados à tabela de símbolos.
- O projeto pode ser expandido para análise sintática e semântica.

---
