package view;

import service.GradeDePalavras;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JogoCacaPalavrasView extends JFrame {

    private GradeDePalavras gradePalavras;
    private JTable tabela;
    private DefaultTableCellRenderer[][] renderers;
    private Point pontoInicial, pontoFinal;

    public JogoCacaPalavrasView() {
        setTitle("Caça-Palavras de Jogadores de Basquete");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] palavras = { "JORDAN", "BRYANT", "CURRY", "LEBRON", "SHAQ" };
        this.gradePalavras = new GradeDePalavras(palavras);
        montarInterface();
        setVisible(true);
    }

    public void montarInterface() {
        int TAM = gradePalavras.getTamanho();
        String[] colunas = new String[TAM];
        for (int i = 0; i < TAM; i++) colunas[i] = String.valueOf(i + 1);

        String[][] dados = new String[TAM][TAM];
        renderers  = new DefaultTableCellRenderer[TAM][TAM];

        char[][] grade = gradePalavras.getGrade();
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                dados[i][j] = String.valueOf(grade[i][j]);
                renderers[i][j] = new DefaultTableCellRenderer();
                renderers[i][j].setHorizontalAlignment(JLabel.CENTER);
            }
        }

        tabela = new JTable(dados, colunas) {
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (renderers[row][column].getBackground() != null) {
                    c.setBackground(renderers[row][column].getBackground());
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela.setRowHeight(40);
        tabela.setFont(new Font("Arial", Font.BOLD, 20));

        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        JLabel dicas = new JLabel("Palavras: " + String.join(", ", gradePalavras.getPalavras()));
        dicas.setFont(new Font("Arial", Font.PLAIN, 16));
        add(dicas, BorderLayout.NORTH);

        tabela.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                pontoInicial = tabela.getCellRect(tabela.rowAtPoint(e.getPoint()), tabela.columnAtPoint(e.getPoint()), true).getLocation();
            }

            public void mouseReleased(MouseEvent e) {
                pontoFinal = tabela.getCellRect(tabela.rowAtPoint(e.getPoint()), tabela.columnAtPoint(e.getPoint()), true).getLocation();
                processarSelecao();
            }
        });
    }

    private void processarSelecao() {
        int linhaIni = tabela.rowAtPoint(pontoInicial);
        int colIni = tabela.columnAtPoint(pontoInicial);
        int linhaFim = tabela.rowAtPoint(pontoFinal);
        int colFim = tabela.columnAtPoint(pontoFinal);

        if (linhaIni == linhaFim) {
            // Horizontal
            int inicio = Math.min(colIni, colFim);
            int fim = Math.max(colIni, colFim);
            StringBuilder sb = new StringBuilder();
            for (int i = inicio; i <= fim; i++) {
                sb.append(gradePalavras.getGrade()[linhaIni][i]);
            }
            verificarPalavra(sb.toString(), linhaIni, inicio, "HORIZONTAL", fim - inicio + 1);
        } else if (colIni == colFim) {
            // Vertical
            int inicio = Math.min(linhaIni, linhaFim);
            int fim = Math.max(linhaIni, linhaFim);
            StringBuilder sb = new StringBuilder();
            for (int i = inicio; i <= fim; i++) {
                sb.append(gradePalavras.getGrade()[i][colIni]);
            }
            verificarPalavra(sb.toString(), inicio, colIni, "VERTICAL", fim - inicio + 1);
        }
    }

    private void verificarPalavra(String palavraSelecionada, int linha, int coluna, String direcao, int tamanho) {
        if (gradePalavras.getPalavrasRestantes().contains(palavraSelecionada)) {
            for (int i = 0; i < tamanho; i++) {
                if (direcao.equals("HORIZONTAL")) {
                    renderers[linha][coluna + i].setBackground(Color.GREEN);
                } else {
                    renderers[linha + i][coluna].setBackground(Color.GREEN);
                }
            }
            gradePalavras.removerPalavra(palavraSelecionada);
            tabela.repaint();
            JOptionPane.showMessageDialog(this, "Palavra encontrada: " + palavraSelecionada);

            if (gradePalavras.todasEncontradas()) {
                JOptionPane.showMessageDialog(this, "Parabéns! Você encontrou todas as palavras!");
            }
        }
    }

}
