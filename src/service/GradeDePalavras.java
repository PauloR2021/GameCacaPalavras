package service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GradeDePalavras {
    private final int TAMANHO = 10;
    private char[][] grade = new char[TAMANHO][TAMANHO];
    private String[] palavras;
    private Set<String> palavrasRestantes = new HashSet<>();

    public GradeDePalavras(String[] palavras) {
        this.palavras = palavras;
        palavrasRestantes.addAll(Arrays.asList(palavras));
        gerarGrade();
    }

    private void gerarGrade() {
        for (int i = 0; i < TAMANHO; i++) {
            Arrays.fill(grade[i], ' ');
        }

        Random rand = new Random();
        for (String palavra : palavras) {
            boolean colocada = false;
            while (!colocada) {
                int linha = rand.nextInt(TAMANHO);
                int coluna = rand.nextInt(TAMANHO);
                int direcao = rand.nextInt(2); // 0-horizontal, 1-vertical

                if (direcao == 0 && coluna + palavra.length() <= TAMANHO) {
                    boolean cabe = true;
                    for (int i = 0; i < palavra.length(); i++) {
                        if (grade[linha][coluna + i] != ' ' && grade[linha][coluna + i] != palavra.charAt(i)) {
                            cabe = false;
                            break;
                        }
                    }
                    if (cabe) {
                        for (int i = 0; i < palavra.length(); i++) {
                            grade[linha][coluna + i] = palavra.charAt(i);
                        }
                        colocada = true;
                    }
                } else if (direcao == 1 && linha + palavra.length() <= TAMANHO) {
                    boolean cabe = true;
                    for (int i = 0; i < palavra.length(); i++) {
                        if (grade[linha + i][coluna] != ' ' && grade[linha + i][coluna] != palavra.charAt(i)) {
                            cabe = false;
                            break;
                        }
                    }
                    if (cabe) {
                        for (int i = 0; i < palavra.length(); i++) {
                            grade[linha + i][coluna] = palavra.charAt(i);
                        }
                        colocada = true;
                    }
                }
            }
        }

        // Preencher os espaços vazios
        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                if (grade[i][j] == ' ') {
                    grade[i][j] = (char) ('A' + rand.nextInt(26));
                }
            }
        }
    }

    public char[][] getGrade() {
        return grade;
    }

    public int getTamanho() {
        return TAMANHO;
    }

    public String[] getPalavras() {
        return palavras;
    }

    public Set<String> getPalavrasRestantes() {
        return palavrasRestantes;
    }

    public void removerPalavra(String palavra) {
        palavrasRestantes.remove(palavra);
    }

    public boolean todasEncontradas() {
        return palavrasRestantes.isEmpty();
    }

}
