
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programapief;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author rafae
 */
public final class telaPrincipal extends javax.swing.JFrame {

    conexoes conexao = new conexoes();

    //variaveis sql
    Connection connection;
    Statement statement;
    public ResultSet result2, result3, result4, result5, result6, result7;

    DefaultListModel modelo;
    int Enter = 0;
    String[] Codig;

    //variaveis para arrastar a janela
    int positionX;
    int positionY;

    public telaPrincipal() {

        initComponents();
        abrirConexao();
        popularTabela();
        icone();

        //configurar barra de pesquisa
        jList1.setVisible(false);
        jList2.setVisible(false);
        modelo = new DefaultListModel();
        jList1.setModel(modelo);
        jList2.setModel(modelo);

        //deixar os botoes transparentes
        jButton1.setBackground(new Color(0, 0, 0, 0));
        jButton4.setBackground(new Color(0, 0, 0, 0));
        jButton3.setBackground(new Color(0, 0, 0, 0));
        jButton2.setBackground(new Color(0, 0, 0, 0));
        jPanel5.setBackground(new Color(0, 0, 0, 0));

        //customizacao da barra de rolagem das tabelas
        ScrollBarCustom sp = new ScrollBarCustom();
        jScrollPane1.setVerticalScrollBar(new ScrollBarCustom());
        sp.setOrientation(JScrollBar.HORIZONTAL);
        jScrollPane1.setHorizontalScrollBar(sp);
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(20);

        //deixar o frame centralizado
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setBackground(new Color(0, 0, 0, 0));
        setBackground(new Color(0, 0, 0, 0));

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

        //deixar os elementos das celulas das tabelas centralizados
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        jTableE.setDefaultRenderer(Integer.class, centerRenderer);
        jTableC.setDefaultRenderer(Integer.class, centerRenderer);

    }

//conectar o banco de dados
    private void abrirConexao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/pief_db",
                    //"jdbc:mysql://192.168.1.107:3306/pief_db",
                    "root",
                    "root");
            statement = connection.createStatement();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        statement.close();
        connection.close();
    }

    //adicionar os dados nas tabelas 
    //ESTOQUE
    private void popularTabela() {
        ((DefaultTableModel) jTableE.getModel()).setRowCount(0);
        try {
            ResultSet result = statement.executeQuery("SELECT * FROM veiculo where status like 'on'");
            while (result.next()) {
                String linha[] = {
                    String.valueOf(result.getInt(1)),
                    result.getString(2),
                    result.getString(3),
                    String.valueOf(result.getInt(4)),
                    result.getString(5),
                    result.getString(6),
                    String.valueOf(result.getInt(7)),
                    result.getString(8),
                    String.valueOf(result.getInt(9)),
                    result.getString(10),
                    result.getString(11),
                    result.getString(12),
                    result.getString(13),
                    String.valueOf(result.getInt(14)),
                    String.valueOf(result.getInt(16))

                };
                ((DefaultTableModel) jTableE.getModel()).addRow(linha);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        }

        //adicionar os dados nas tabelas 
        //CLIENTES
        ((DefaultTableModel) jTableC.getModel()).setRowCount(0);
        try {
            ResultSet result = statement.executeQuery("SELECT * FROM cliente");
            while (result.next()) {
                String linha[] = {
                    String.valueOf(result.getInt(1)),
                    result.getString(2),
                    result.getString(3),
                    String.valueOf(result.getInt(4)),
                    result.getString(5),
                    result.getString(6),
                    result.getString(7),
                    String.valueOf(result.getDate(8)),
                    result.getString(9),};
                ((DefaultTableModel) jTableC.getModel()).addRow(linha);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        }

        try {
            ((DefaultTableModel) jTableV.getModel()).setRowCount(0);
            ResultSet result = statement.executeQuery("SELECT * FROM venda order by data desc");
            while (result.next()) {
                String linha[] = {
                    String.valueOf(result.getInt(1)),
                    result.getString(8),
                    String.valueOf(result.getInt(4)),
                    result.getString(7),
                    String.valueOf(result.getInt(5)),
                    String.valueOf(result.getDate(3)),
                    String.valueOf(result.getInt(6)),
                    String.valueOf(result.getInt(2)),};

                ((DefaultTableModel) jTableV.getModel()).addRow(linha);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        }

        int Total = jTableE.getRowCount();
        String totalString = String.valueOf(Total);
        jtotalEstoque.setText(totalString);

        int mes = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int ano = Calendar.getInstance().get(Calendar.YEAR);
         

        try {
            ResultSet result = statement.executeQuery("Select count(*) from venda where month(data) = " + mes + " and year(data) = " + ano);
            while (result.next()) {
                jtotalVenda.setText(String.valueOf(result.getInt(1)));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    //barra de pesquisa 
    public void ListaDePesquisa() {

        //----------------------------------------
        //pesquisa docliente tela estoque inserir|
        //----------------------------------------
        if (jTabbedPane1.getSelectedIndex() == 6) {

            try {
                result2 = statement.executeQuery("SELECT * FROM cliente where nome like '" + proprietarioJTF.getText() + "%' ORDER BY nome");
                modelo.removeAllElements();
                int v = 0;
                Codig = new String[4];
                while (result2.next() & v < 4) {
                    modelo.addElement(result2.getString("nome"));
                    v++;
                }
                if (v >= 1) {
                    jList1.setVisible(true);
                } else {
                    jList1.setVisible(false);
                }

            } catch (SQLException erro) {
                JOptionPane.showMessageDialog(null, "Erro ao listar dados" + erro);
            }

            //-------------------------
            //pesquisa tabela Estoque |
            //-------------------------
        } else if (jTabbedPane1.getSelectedIndex() == 1) {

            try {
                ((DefaultTableModel) jTableE.getModel()).setRowCount(0);
                result3 = statement.executeQuery("SELECT * FROM veiculo where marca like '" + jTextField1.getText() + "%' ORDER BY marca");
                int v = 0;
                Codig = new String[15];
                while (result3.next() & v < 15) {
                    String linha[] = {
                        String.valueOf(result3.getInt(1)),
                        result3.getString(2),
                        result3.getString(3),
                        String.valueOf(result3.getInt(4)),
                        result3.getString(5),
                        result3.getString(6),
                        String.valueOf(result3.getInt(7)),
                        result3.getString(8),
                        String.valueOf(result3.getInt(9)),
                        result3.getString(10),
                        result3.getString(11),
                        result3.getString(12),
                        result3.getString(13),
                        String.valueOf(result3.getInt(14)),
                        String.valueOf(result3.getInt(16))

                    };
                    ((DefaultTableModel) jTableE.getModel()).addRow(linha);

                }

            } catch (SQLException erro) {
                JOptionPane.showMessageDialog(null, "Erro ao listar dados" + erro);
            }
            //-------------------------
            //pesquisa tabela clientes|
            //-------------------------
        } else if (jTabbedPane1.getSelectedIndex() == 5) {

            try {
                ((DefaultTableModel) jTableC.getModel()).setRowCount(0);
                result4 = statement.executeQuery("SELECT * FROM cliente where nome like '" + jTextField2.getText() + "%' ORDER BY nome");
                int v = 0;
                Codig = new String[15];
                while (result4.next() & v < 15) {
                    String linha[] = {
                        String.valueOf(result4.getInt(1)),
                        result4.getString(2),
                        result4.getString(3),
                        String.valueOf(result4.getInt(4)),
                        result4.getString(5),
                        result4.getString(6),
                        result4.getString(7),
                        String.valueOf(result4.getDate(8)),
                        result4.getString(9),};
                    ((DefaultTableModel) jTableC.getModel()).addRow(linha);

                }

            } catch (SQLException erro) {
                JOptionPane.showMessageDialog(null, "Erro ao listar dados" + erro);
            }

        } else if (jTabbedPane1.getSelectedIndex() == 8) {

            try {
                result5 = statement.executeQuery("SELECT * FROM cliente where nome like '" + nomeJTFV.getText() + "%' ORDER BY nome");
                modelo.removeAllElements();
                int v = 0;
                Codig = new String[4];
                while (result5.next() & v < 4) {
                    modelo.addElement(result5.getString("nome"));
                    v++;
                }
                if (v >= 1) {
                    jList2.setVisible(true);
                } else {
                    jList2.setVisible(false);
                }

            } catch (SQLException erro) {
                JOptionPane.showMessageDialog(null, "Erro ao listar dados" + erro);
            }

        }
    }

    //alterar os icones dos botoes
    private void icone() {

        if (jTabbedPane1.getSelectedIndex() == 0) {
            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png")));
            jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
        } else if (jTabbedPane1.getSelectedIndex() == 1) {
            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png")));
            jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
        } else if (jTabbedPane1.getSelectedIndex() == 2) {
            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png")));
            jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
        } else if (jTabbedPane1.getSelectedIndex() == 3) {
            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png")));
            jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
        } else if (jTabbedPane1.getSelectedIndex() == 4) {
            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png")));
            jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
        } else if (jTabbedPane1.getSelectedIndex() == 5) {
            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png")));
            jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png")));
        }

    }

    //customizacao da scrollbar
    public class ModernScrollBarUI extends BasicScrollBarUI {

        @Override
        protected JButton createIncreaseButton(int i) {
            return new ScrollBarButton();
        }

        @Override
        protected JButton createDecreaseButton(int i) {
            return new ScrollBarButton();
        }

        @Override
        protected void paintTrack(Graphics grphcs, JComponent jc, Rectangle rctngl) {
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int orientation = scrollbar.getOrientation();
            int size;
            int x;
            int y;
            int width;
            int height;
            if (orientation == JScrollBar.VERTICAL) {
                size = rctngl.width / 2;
                x = rctngl.x + ((rctngl.width - size) / 2);
                y = rctngl.y;
                width = size;
                height = rctngl.height;
            } else {
                size = rctngl.height / 2;
                y = rctngl.y + ((rctngl.height - size) / 2);
                x = 0;
                width = rctngl.width;
                height = size;
            }
            g2.setColor(new Color(240, 240, 240));
            g2.fillRect(x, y, width, height);
        }

        @Override
        protected void paintThumb(Graphics grphcs, JComponent jc, Rectangle rctngl) {
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int x = rctngl.x;
            int y = rctngl.y;
            int width = rctngl.width;
            int height = rctngl.height;
            if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
                y += 8;
                height -= 16;
            } else {
                x += 8;
                width -= 16;
            }
            g2.setColor(scrollbar.getForeground());
            g2.fillRoundRect(x, y, width, height, 10, 10);

        }

        private class ScrollBarButton extends JButton {

            public ScrollBarButton() {
                setBorder(BorderFactory.createEmptyBorder());
            }

            @Override
            public void paint(Graphics grphcs) {
            }
        }
    }

    public class ScrollBarCustom extends JScrollBar {

        public ScrollBarCustom() {
            setUI(new ModernScrollBarUI());
            setPreferredSize(new Dimension(8, 8));
            setForeground(new Color(105, 105, 105));
            setBackground(Color.WHITE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel8 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jtotalVenda = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jtotalEstoque = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableE = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableN = new javax.swing.JTable();
        jTable2 = new javax.swing.JTable();
        jLabel36 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jButton24 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableV = new javax.swing.JTable();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableC = new javax.swing.JTable();
        jTextField2 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jButton17 = new javax.swing.JButton();
        jLabel43 = new javax.swing.JLabel();
        jButton19 = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        jButton23 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jList1 = new javax.swing.JList<>();
        proprietarioJTF = new javax.swing.JTextField();
        idclienteJTF = new javax.swing.JTextField();
        marcaJTF = new javax.swing.JTextField();
        renavamJTF = new javax.swing.JTextField();
        potenciaJTF = new javax.swing.JTextField();
        anoJTF = new javax.swing.JTextField();
        modeloJTF = new javax.swing.JTextField();
        corJTF = new javax.swing.JTextField();
        motorJTF = new javax.swing.JTextField();
        cambioJTF = new javax.swing.JTextField();
        placaJTF = new javax.swing.JTextField();
        finalPlacaJTF = new javax.swing.JTextField();
        statusJTF1 = new javax.swing.JComboBox<>();
        carroceriaJTF = new javax.swing.JComboBox<>();
        kmJTF = new javax.swing.JTextField();
        valorJTF = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        nomeJTF = new javax.swing.JTextField();
        documentoJTF = new javax.swing.JFormattedTextField();
        telefoneJTF = new javax.swing.JTextField();
        emailJTF = new javax.swing.JTextField();
        ocupacaoJTF = new javax.swing.JTextField();
        generoJTF = new javax.swing.JComboBox<>();
        tipoJTF = new javax.swing.JComboBox<>();
        jLabel44 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jLabel49 = new javax.swing.JLabel();
        nascimentoJTF = new javax.swing.JFormattedTextField();
        jButton22 = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jButton25 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jList2 = new javax.swing.JList<>();
        nomeJTFV = new javax.swing.JTextField();
        idnomeJTFV = new javax.swing.JTextField();
        veiculoJTFV = new javax.swing.JTextField();
        valorJTFV = new javax.swing.JTextField();
        vendedorJTFV = new javax.swing.JTextField();
        idvendedorJTFV = new javax.swing.JTextField();
        statusJTFV = new javax.swing.JTextField();
        dataJTFV = new javax.swing.JFormattedTextField();
        documentoJTFV = new javax.swing.JFormattedTextField();
        telefoneJTFV = new javax.swing.JTextField();
        placaJTFV = new javax.swing.JTextField();
        nascimentoJTFV = new javax.swing.JFormattedTextField();
        idveiculoJTFV = new javax.swing.JTextField();
        emailJTFV = new javax.swing.JTextField();
        ocupacaoJTFV = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jLabel62 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Data+ gestor de veiculos");
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/logoData+.png"))); // NOI18N
        getContentPane().add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 50, -1, -1));

        jPanel5.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel5MouseDragged(evt);
            }
        });
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel5MousePressed(evt);
            }
        });

        jLabel9.setForeground(new java.awt.Color(75, 75, 75));
        jLabel9.setText("RAFAEL GORAYB CORREA");

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon awesome-user-circle.png"))); // NOI18N
        jButton7.setText(" ");
        jButton7.setBorderPainted(false);
        jButton7.setContentAreaFilled(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon awesome-bell.png"))); // NOI18N
        jButton8.setText(" ");
        jButton8.setBorderPainted(false);
        jButton8.setContentAreaFilled(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(702, Short.MAX_VALUE)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton7)
                .addGap(35, 35, 35))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jButton7)
                    .addComponent(jButton8)))
        );

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 70));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(235, 235, 235));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon awesome-car-side-1.png"))); // NOI18N
        jLabel5.setText("   Estoque");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 240, 150, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(235, 235, 235));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon awesome-handshake-1.png"))); // NOI18N
        jLabel12.setText("  Negociações");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 295, 150, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(235, 235, 235));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon awesome-money-check-alt-1.png"))); // NOI18N
        jLabel13.setText("  Faturamento");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 352, 150, -1));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(235, 235, 235));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon awesome-tachometer-alt.png"))); // NOI18N
        jLabel14.setText("   Vendas");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 405, 150, -1));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(235, 235, 235));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon open-person.png"))); // NOI18N
        jLabel15.setText("   Clientes");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 461, 150, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(235, 235, 235));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon awesome-tachometer-alt.png"))); // NOI18N
        jLabel16.setText("   Dashboard");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 190, 150, -1));

        jButton5.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png"))); // NOI18N
        jButton5.setText(" ");
        jButton5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.setDefaultCapable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png"))); // NOI18N
        jButton5.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarra2.png"))); // NOI18N
        jButton5.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarra2.png"))); // NOI18N
        jButton5.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png"))); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 390, 190, 55));

        jButton6.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png"))); // NOI18N
        jButton6.setText(" ");
        jButton6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton6.setBorderPainted(false);
        jButton6.setContentAreaFilled(false);
        jButton6.setDefaultCapable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png"))); // NOI18N
        jButton6.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarra2.png"))); // NOI18N
        jButton6.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarra2.png"))); // NOI18N
        jButton6.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 445, 190, 55));

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png"))); // NOI18N
        jButton2.setText(" ");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setDefaultCapable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png"))); // NOI18N
        jButton2.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarra2.png"))); // NOI18N
        jButton2.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarra2.png"))); // NOI18N
        jButton2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 220, 190, 60));

        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png"))); // NOI18N
        jButton3.setText(" ");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setDefaultCapable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png"))); // NOI18N
        jButton3.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarra2.png"))); // NOI18N
        jButton3.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarra2.png"))); // NOI18N
        jButton3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 280, 190, 55));

        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png"))); // NOI18N
        jButton4.setText(" ");
        jButton4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setDefaultCapable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png"))); // NOI18N
        jButton4.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarra2.png"))); // NOI18N
        jButton4.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarra2.png"))); // NOI18N
        jButton4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 335, 190, 55));

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarraTransparente.png"))); // NOI18N
        jButton1.setText(" ");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setDefaultCapable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png"))); // NOI18N
        jButton1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarra2.png"))); // NOI18N
        jButton1.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoBarra2.png"))); // NOI18N
        jButton1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao barra.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 170, 190, 55));

        jLabel18.setForeground(new java.awt.Color(235, 235, 235));
        jLabel18.setText("FAQ");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 620, -1, -1));

        jLabel19.setForeground(new java.awt.Color(235, 235, 235));
        jLabel19.setText("Central de atendimento");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 640, -1, -1));

        jLabel20.setForeground(new java.awt.Color(235, 235, 235));
        jLabel20.setText("Envie uma sugestão");
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 660, -1, -1));

        jLabel10.setForeground(new java.awt.Color(235, 235, 235));
        jLabel10.setText("Suporte");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 600, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/barra lateral.png"))); // NOI18N
        jLabel1.setText(" ");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setPreferredSize(new java.awt.Dimension(190, 700));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 210, 700));

        jPanel1.setBackground(new java.awt.Color(235, 235, 235));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel42.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 0, 0));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("Vendas");
        jPanel1.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(573, 290, 80, -1));

        jtotalVenda.setFont(new java.awt.Font("Segoe UI", 1, 52)); // NOI18N
        jtotalVenda.setForeground(new java.awt.Color(0, 0, 0));
        jtotalVenda.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jtotalVenda.setText("30");
        jPanel1.add(jtotalVenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(505, 260, 80, 60));

        jLabel61.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(0, 0, 0));
        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel61.setText("no mes atual");
        jPanel1.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 330, -1, -1));

        jtotalEstoque.setFont(new java.awt.Font("Segoe UI", 1, 52)); // NOI18N
        jtotalEstoque.setForeground(new java.awt.Color(0, 0, 0));
        jtotalEstoque.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jtotalEstoque.setText("82");
        jPanel1.add(jtotalEstoque, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 260, 110, 60));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 0, 0));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("Em estoque");
        jPanel1.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 330, -1, -1));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 0, 0));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Veículos");
        jPanel1.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 290, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(75, 75, 75));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon awesome-tachometer-alt-1.png"))); // NOI18N
        jLabel2.setText(" Dashboard");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 200, 60));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/quadrado pequeno.png"))); // NOI18N
        jLabel3.setText(" ");
        jLabel3.setPreferredSize(new java.awt.Dimension(283, 185));
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(406, 204, 310, 202));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/barra esticada.png"))); // NOI18N
        jLabel4.setText(" ");
        jLabel4.setPreferredSize(new java.awt.Dimension(283, 185));
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 74, 810, 100));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/quadrado pequeno.png"))); // NOI18N
        jLabel6.setText(" ");
        jLabel6.setPreferredSize(new java.awt.Dimension(283, 185));
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 204, 310, 202));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/quadrado pequeno.png"))); // NOI18N
        jLabel7.setText(" ");
        jLabel7.setPreferredSize(new java.awt.Dimension(283, 185));
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 432, 310, 202));

        jTabbedPane1.addTab("tab1", jPanel1);

        jPanel2.setBackground(new java.awt.Color(235, 235, 235));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.setBackground(new java.awt.Color(225, 225, 225));
        jTextField1.setForeground(new java.awt.Color(51, 51, 51));
        jTextField1.setText("Pesquise aqui:");
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        jPanel2.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 234, 690, 30));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jScrollPane1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jScrollPane1.setOpaque(false);

        jTableE.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jTableE.setForeground(new java.awt.Color(51, 51, 51));
        jTableE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Marca", "Modelo", "ano", "cor", "Placa", "Km", "Motor", "Potencia", "Cambio", "Carroceria", "Renavam", "Final da placa", "Preço", "idP"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableE.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableE.setGridColor(new java.awt.Color(221, 221, 221));
        jTableE.setRowHeight(25);
        jTableE.setSelectionBackground(new java.awt.Color(153, 204, 255));
        jTableE.setSelectionForeground(new java.awt.Color(51, 51, 51));
        jTableE.setShowVerticalLines(false);
        jTableE.getTableHeader().setReorderingAllowed(false);
        jTableE.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableEMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTableEMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jTableE);
        if (jTableE.getColumnModel().getColumnCount() > 0) {
            jTableE.getColumnModel().getColumn(1).setMinWidth(150);
            jTableE.getColumnModel().getColumn(2).setPreferredWidth(200);
        }

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 280, 690, 340));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 51, 51));
        jLabel21.setText("Veículos cadastrados:");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 200, -1, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(75, 75, 75));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon awesome-car-side.png"))); // NOI18N
        jLabel17.setText(" Estoque");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 200, 60));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Quadrado grande.png"))); // NOI18N
        jLabel8.setText(" ");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 160, 810, 510));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/barra esticada.png"))); // NOI18N
        jLabel11.setText(" ");
        jLabel11.setPreferredSize(new java.awt.Dimension(283, 185));
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 74, 810, 100));

        jLabel23.setForeground(new java.awt.Color(51, 51, 51));
        jLabel23.setText("Inserir");
        jLabel23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 655, -1, 30));

        jLabel24.setForeground(new java.awt.Color(51, 51, 51));
        jLabel24.setText("Deletar");
        jLabel24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel2.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(706, 655, 40, 30));

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoTabelaG.png"))); // NOI18N
        jButton10.setText(" ");
        jButton10.setBorderPainted(false);
        jButton10.setContentAreaFilled(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 655, 130, 40));

        jButton12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton12.setForeground(new java.awt.Color(51, 51, 51));
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoTabelaP.png"))); // NOI18N
        jButton12.setText(" ");
        jButton12.setAlignmentY(0.0F);
        jButton12.setBorderPainted(false);
        jButton12.setContentAreaFilled(false);
        jButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton12.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 655, 75, 40));

        jButton16.setText("reload");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton16, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 660, -1, -1));

        jTabbedPane1.addTab("tab2", jPanel2);

        jPanel3.setBackground(new java.awt.Color(235, 235, 235));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jScrollPane4.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jScrollPane4.setOpaque(false);

        jTableN.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jTableN.setForeground(new java.awt.Color(51, 51, 51));
        jTableN.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTableN.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableN.setGridColor(new java.awt.Color(221, 221, 221));
        jTableN.setRowHeight(25);
        jTableN.setSelectionBackground(new java.awt.Color(153, 204, 255));
        jTableN.setSelectionForeground(new java.awt.Color(51, 51, 51));
        jTableN.setShowVerticalLines(false);
        jTableN.getTableHeader().setReorderingAllowed(false);
        jTableN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableNMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTableNMouseEntered(evt);
            }
        });
        jScrollPane4.setViewportView(jTableN);

        jPanel3.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 210, 300, 420));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cliente", "Veículo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jPanel3.add(jTable2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(75, 75, 75));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon awesome-handshake.png"))); // NOI18N
        jLabel36.setText(" Negociações");
        jPanel3.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 250, 60));

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/barra esticada.png"))); // NOI18N
        jLabel35.setText(" ");
        jLabel35.setPreferredSize(new java.awt.Dimension(283, 185));
        jPanel3.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 74, 810, 100));

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/telaNegociacoes.png"))); // NOI18N
        jLabel30.setText(" ");
        jPanel3.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 160, 810, 510));

        jLabel37.setForeground(new java.awt.Color(51, 51, 51));
        jLabel37.setText("Inserir");
        jLabel37.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel3.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 655, -1, 30));

        jButton15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton15.setForeground(new java.awt.Color(51, 51, 51));
        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoTabelaP.png"))); // NOI18N
        jButton15.setText(" ");
        jButton15.setAlignmentY(0.0F);
        jButton15.setBorderPainted(false);
        jButton15.setContentAreaFilled(false);
        jButton15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton15, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 655, 75, 40));

        jTabbedPane1.addTab("tab3", jPanel3);

        jPanel4.setBackground(new java.awt.Color(235, 235, 235));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 838, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 704, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab4", jPanel4);

        jPanel6.setBackground(new java.awt.Color(235, 235, 235));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel55.setForeground(new java.awt.Color(51, 51, 51));
        jLabel55.setText("Inserir");
        jLabel55.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel6.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 655, -1, 30));

        jButton24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton24.setForeground(new java.awt.Color(51, 51, 51));
        jButton24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoTabelaP.png"))); // NOI18N
        jButton24.setText(" ");
        jButton24.setAlignmentY(0.0F);
        jButton24.setBorderPainted(false);
        jButton24.setContentAreaFilled(false);
        jButton24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton24, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 655, 75, 40));

        jScrollPane5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jScrollPane5.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jScrollPane5.setOpaque(false);

        jTableV.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jTableV.setForeground(new java.awt.Color(51, 51, 51));
        jTableV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "veiculo", "id veiculo", "cliente", "id comprador", "data", "valor", "id vendedor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableV.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableV.setGridColor(new java.awt.Color(221, 221, 221));
        jTableV.setRowHeight(25);
        jTableV.setSelectionBackground(new java.awt.Color(153, 204, 255));
        jTableV.setSelectionForeground(new java.awt.Color(51, 51, 51));
        jTableV.setShowVerticalLines(false);
        jTableV.getTableHeader().setReorderingAllowed(false);
        jTableV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableVMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTableVMouseEntered(evt);
            }
        });
        jScrollPane5.setViewportView(jTableV);

        jPanel6.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 210, 300, 420));

        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/telaNegociacoes.png"))); // NOI18N
        jLabel52.setText(" ");
        jPanel6.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 160, 810, 510));

        jLabel53.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(75, 75, 75));
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon awesome-handshake.png"))); // NOI18N
        jLabel53.setText(" Vendas");
        jPanel6.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 250, 60));

        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/barra esticada.png"))); // NOI18N
        jLabel54.setText(" ");
        jLabel54.setPreferredSize(new java.awt.Dimension(283, 185));
        jPanel6.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 74, 810, 100));

        jTabbedPane1.addTab("tab5", jPanel6);

        jPanel7.setBackground(new java.awt.Color(235, 235, 235));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jScrollPane3.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jScrollPane3.setOpaque(false);

        jTableC.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jTableC.setForeground(new java.awt.Color(51, 51, 51));
        jTableC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Email", "telefone", "Documento", "ocupacao", "genero", "data Nascimento", "Tipo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableC.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTableC.setGridColor(new java.awt.Color(221, 221, 221));
        jTableC.setRowHeight(25);
        jTableC.setSelectionBackground(new java.awt.Color(153, 204, 255));
        jTableC.setSelectionForeground(new java.awt.Color(51, 51, 51));
        jTableC.setShowVerticalLines(false);
        jTableC.getTableHeader().setReorderingAllowed(false);
        jTableC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableCMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTableC);
        if (jTableC.getColumnModel().getColumnCount() > 0) {
            jTableC.getColumnModel().getColumn(1).setPreferredWidth(250);
        }

        jPanel7.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 280, 690, 340));

        jTextField2.setBackground(new java.awt.Color(225, 225, 225));
        jTextField2.setForeground(new java.awt.Color(51, 51, 51));
        jTextField2.setText("Pesquise aqui:");
        jTextField2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField2MouseClicked(evt);
            }
        });
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });
        jPanel7.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 234, 690, 30));

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Quadrado grande.png"))); // NOI18N
        jLabel38.setText(" ");
        jPanel7.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 160, 810, 510));

        jLabel41.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(75, 75, 75));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon awesome-car-side.png"))); // NOI18N
        jLabel41.setText(" Clientes");
        jPanel7.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 200, 60));

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/barra esticada.png"))); // NOI18N
        jLabel39.setText(" ");
        jLabel39.setPreferredSize(new java.awt.Dimension(283, 185));
        jPanel7.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 74, 810, 100));

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(75, 75, 75));
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon awesome-car-side.png"))); // NOI18N
        jLabel40.setText(" Estoque");
        jPanel7.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 200, 60));

        jButton17.setText("reload");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton17, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 660, -1, -1));

        jLabel43.setForeground(new java.awt.Color(51, 51, 51));
        jLabel43.setText("Inserir");
        jLabel43.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel7.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 655, -1, 30));

        jButton19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton19.setForeground(new java.awt.Color(51, 51, 51));
        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoTabelaP.png"))); // NOI18N
        jButton19.setText(" ");
        jButton19.setAlignmentY(0.0F);
        jButton19.setBorderPainted(false);
        jButton19.setContentAreaFilled(false);
        jButton19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton19.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton19, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 655, 75, 40));

        jLabel51.setForeground(new java.awt.Color(51, 51, 51));
        jLabel51.setText("Deletar");
        jLabel51.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel7.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(706, 655, 40, 30));

        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoTabelaG.png"))); // NOI18N
        jButton23.setText(" ");
        jButton23.setBorderPainted(false);
        jButton23.setContentAreaFilled(false);
        jButton23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton23, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 655, 130, 40));

        jTabbedPane1.addTab("tab6", jPanel7);

        jPanel9.setBackground(new java.awt.Color(235, 235, 235));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jList1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jPanel9.add(jList1, new org.netbeans.lib.awtextra.AbsoluteConstraints(163, 304, 490, 70));

        proprietarioJTF.setBackground(new java.awt.Color(235, 235, 235));
        proprietarioJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        proprietarioJTF.setBorder(null);
        proprietarioJTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proprietarioJTFActionPerformed(evt);
            }
        });
        proprietarioJTF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                proprietarioJTFKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                proprietarioJTFKeyReleased(evt);
            }
        });
        jPanel9.add(proprietarioJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 280, 450, 23));

        idclienteJTF.setBackground(new java.awt.Color(235, 235, 235));
        idclienteJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        idclienteJTF.setText(" ");
        idclienteJTF.setBorder(null);
        idclienteJTF.setEnabled(false);
        jPanel9.add(idclienteJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 280, 70, 23));

        marcaJTF.setBackground(new java.awt.Color(235, 235, 235));
        marcaJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        marcaJTF.setBorder(null);
        marcaJTF.setEnabled(false);
        jPanel9.add(marcaJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(171, 455, 60, 23));

        renavamJTF.setBackground(new java.awt.Color(235, 235, 235));
        renavamJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        renavamJTF.setText(" ");
        renavamJTF.setBorder(null);
        renavamJTF.setEnabled(false);
        jPanel9.add(renavamJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(171, 558, 60, 23));

        potenciaJTF.setBackground(new java.awt.Color(235, 235, 235));
        potenciaJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        potenciaJTF.setText(" ");
        potenciaJTF.setBorder(null);
        potenciaJTF.setEnabled(false);
        jPanel9.add(potenciaJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(171, 522, 60, 23));

        anoJTF.setBackground(new java.awt.Color(235, 235, 235));
        anoJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        anoJTF.setText(" ");
        anoJTF.setBorder(null);
        anoJTF.setEnabled(false);
        jPanel9.add(anoJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(171, 488, 60, 23));

        modeloJTF.setBackground(new java.awt.Color(235, 235, 235));
        modeloJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        modeloJTF.setText(" ");
        modeloJTF.setBorder(null);
        modeloJTF.setEnabled(false);
        jPanel9.add(modeloJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 455, 320, 23));

        corJTF.setBackground(new java.awt.Color(235, 235, 235));
        corJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        corJTF.setText(" ");
        corJTF.setBorder(null);
        corJTF.setEnabled(false);
        jPanel9.add(corJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 489, 40, 23));

        motorJTF.setBackground(new java.awt.Color(235, 235, 235));
        motorJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        motorJTF.setText(" ");
        motorJTF.setBorder(null);
        motorJTF.setEnabled(false);
        jPanel9.add(motorJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 523, 160, 23));

        cambioJTF.setBackground(new java.awt.Color(235, 235, 235));
        cambioJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        cambioJTF.setText(" ");
        cambioJTF.setBorder(null);
        cambioJTF.setEnabled(false);
        jPanel9.add(cambioJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 523, 75, 23));

        placaJTF.setBackground(new java.awt.Color(235, 235, 235));
        placaJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        placaJTF.setText(" ");
        placaJTF.setBorder(null);
        placaJTF.setEnabled(false);
        jPanel9.add(placaJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(308, 559, 60, 23));

        finalPlacaJTF.setBackground(new java.awt.Color(235, 235, 235));
        finalPlacaJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        finalPlacaJTF.setText(" ");
        finalPlacaJTF.setBorder(null);
        finalPlacaJTF.setEnabled(false);
        jPanel9.add(finalPlacaJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 563, 30, 23));

        statusJTF1.setBackground(new java.awt.Color(235, 235, 235));
        statusJTF1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Status", "Consignado", "Estoque", "outro" }));
        statusJTF1.setBorder(null);
        statusJTF1.setEnabled(false);
        statusJTF1.setOpaque(false);
        jPanel9.add(statusJTF1, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 240, 80, 30));

        carroceriaJTF.setBackground(new java.awt.Color(235, 235, 235));
        carroceriaJTF.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Carroceria", "hatch", "hatch convercivel", "sedan", "sedan convercivel", "suv", "suv convercivel", "esportivo", "esportivo conversivel" }));
        carroceriaJTF.setBorder(null);
        carroceriaJTF.setEnabled(false);
        carroceriaJTF.setOpaque(false);
        jPanel9.add(carroceriaJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(559, 487, 100, 30));

        kmJTF.setBackground(new java.awt.Color(235, 235, 235));
        kmJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        kmJTF.setText(" ");
        kmJTF.setBorder(null);
        kmJTF.setEnabled(false);
        jPanel9.add(kmJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(425, 489, 50, 23));

        valorJTF.setBackground(new java.awt.Color(235, 235, 235));
        valorJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        valorJTF.setText(" ");
        valorJTF.setBorder(null);
        valorJTF.setEnabled(false);
        jPanel9.add(valorJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(563, 563, 85, 23));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(75, 75, 75));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon awesome-car-side.png"))); // NOI18N
        jLabel26.setText(" Estoque\\inserir");
        jPanel9.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 280, 60));

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/telaInserir.png"))); // NOI18N
        jLabel27.setText(" ");
        jPanel9.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 160, 810, 510));

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/barra esticada.png"))); // NOI18N
        jLabel28.setText(" ");
        jLabel28.setPreferredSize(new java.awt.Dimension(283, 185));
        jPanel9.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 74, 810, 100));

        jLabel29.setForeground(new java.awt.Color(51, 51, 51));
        jLabel29.setText("Cancelar");
        jLabel29.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel9.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 656, -1, 30));

        jLabel50.setForeground(new java.awt.Color(51, 51, 51));
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel50.setText("Editar");
        jLabel50.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel9.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 655, 50, 30));

        jLabel31.setForeground(new java.awt.Color(51, 51, 51));
        jLabel31.setText("Concluir");
        jLabel31.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel9.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 656, 50, 30));

        jButton9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(51, 51, 51));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoTabelaP.png"))); // NOI18N
        jButton9.setText(" ");
        jButton9.setAlignmentY(0.0F);
        jButton9.setBorderPainted(false);
        jButton9.setContentAreaFilled(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 655, 75, 40));

        jButton11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(51, 51, 51));
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoTabelaP.png"))); // NOI18N
        jButton11.setText(" ");
        jButton11.setAlignmentY(0.0F);
        jButton11.setBorderPainted(false);
        jButton11.setContentAreaFilled(false);
        jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton11.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton11.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 655, 75, 40));

        jButton14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton14.setForeground(new java.awt.Color(51, 51, 51));
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoTabelaP.png"))); // NOI18N
        jButton14.setText(" ");
        jButton14.setAlignmentY(0.0F);
        jButton14.setBorderPainted(false);
        jButton14.setContentAreaFilled(false);
        jButton14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton14.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton14.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 655, 75, 40));

        jLabel34.setForeground(new java.awt.Color(51, 51, 51));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("Concluir alteracoes");
        jLabel34.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel9.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 655, 120, 30));

        jButton13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton13.setForeground(new java.awt.Color(51, 51, 51));
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoTabelaG.png"))); // NOI18N
        jButton13.setText(" ");
        jButton13.setAlignmentY(0.0F);
        jButton13.setBorderPainted(false);
        jButton13.setContentAreaFilled(false);
        jButton13.setEnabled(false);
        jButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 655, 120, 40));

        jTabbedPane1.addTab("tab2", jPanel9);

        jPanel10.setBackground(new java.awt.Color(235, 235, 235));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        nomeJTF.setBackground(new java.awt.Color(235, 235, 235));
        nomeJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nomeJTF.setText(" ");
        nomeJTF.setBorder(null);
        nomeJTF.setEnabled(false);
        jPanel10.add(nomeJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 280, 250, 23));

        documentoJTF.setBorder(null);
        documentoJTF.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        documentoJTF.setEnabled(false);
        documentoJTF.setOpaque(false);
        documentoJTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documentoJTFActionPerformed(evt);
            }
        });
        jPanel10.add(documentoJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 388, 70, 25));

        telefoneJTF.setBackground(new java.awt.Color(235, 235, 235));
        telefoneJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        telefoneJTF.setText(" ");
        telefoneJTF.setBorder(null);
        telefoneJTF.setEnabled(false);
        jPanel10.add(telefoneJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 352, 60, 23));

        emailJTF.setBackground(new java.awt.Color(235, 235, 235));
        emailJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        emailJTF.setText(" ");
        emailJTF.setBorder(null);
        emailJTF.setEnabled(false);
        jPanel10.add(emailJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 315, 250, 23));

        ocupacaoJTF.setBackground(new java.awt.Color(235, 235, 235));
        ocupacaoJTF.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        ocupacaoJTF.setText(" ");
        ocupacaoJTF.setBorder(null);
        ocupacaoJTF.setEnabled(false);
        ocupacaoJTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ocupacaoJTFActionPerformed(evt);
            }
        });
        jPanel10.add(ocupacaoJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 424, 60, 23));

        generoJTF.setBackground(new java.awt.Color(235, 235, 235));
        generoJTF.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "genero", "MASCULINO", "FEMININO" }));
        generoJTF.setBorder(null);
        generoJTF.setEnabled(false);
        generoJTF.setOpaque(false);
        generoJTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generoJTFActionPerformed(evt);
            }
        });
        jPanel10.add(generoJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 240, 120, 30));

        tipoJTF.setBackground(new java.awt.Color(235, 235, 235));
        tipoJTF.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CPF", "CNPJ" }));
        tipoJTF.setBorder(null);
        tipoJTF.setEnabled(false);
        tipoJTF.setOpaque(false);
        tipoJTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoJTFActionPerformed(evt);
            }
        });
        jPanel10.add(tipoJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(162, 240, 80, 30));

        jLabel44.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(75, 75, 75));
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon awesome-car-side.png"))); // NOI18N
        jLabel44.setText(" Cliente\\inserir");
        jPanel10.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 280, 60));

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/barra esticada.png"))); // NOI18N
        jLabel46.setText(" ");
        jLabel46.setPreferredSize(new java.awt.Dimension(283, 185));
        jPanel10.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 74, 810, 100));

        jLabel47.setForeground(new java.awt.Color(51, 51, 51));
        jLabel47.setText("Cancelar");
        jLabel47.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel10.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 656, -1, 30));

        jLabel48.setForeground(new java.awt.Color(51, 51, 51));
        jLabel48.setText("Concluir");
        jLabel48.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel10.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 656, 50, 30));

        jButton20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton20.setForeground(new java.awt.Color(51, 51, 51));
        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoTabelaP.png"))); // NOI18N
        jButton20.setText(" ");
        jButton20.setAlignmentY(0.0F);
        jButton20.setBorderPainted(false);
        jButton20.setContentAreaFilled(false);
        jButton20.setEnabled(false);
        jButton20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton20.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton20.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton20, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 655, 75, 40));

        jButton21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton21.setForeground(new java.awt.Color(51, 51, 51));
        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoTabelaP.png"))); // NOI18N
        jButton21.setText(" ");
        jButton21.setAlignmentY(0.0F);
        jButton21.setBorderPainted(false);
        jButton21.setContentAreaFilled(false);
        jButton21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton21.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton21.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton21, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 655, 75, 40));

        jLabel49.setForeground(new java.awt.Color(51, 51, 51));
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("Concluir alteracoes");
        jLabel49.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel10.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 656, 120, 30));

        nascimentoJTF.setBorder(null);
        nascimentoJTF.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("y/MM/dd"))));
        nascimentoJTF.setEnabled(false);
        nascimentoJTF.setOpaque(false);
        nascimentoJTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nascimentoJTFActionPerformed(evt);
            }
        });
        jPanel10.add(nascimentoJTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 350, 70, 25));

        jButton22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton22.setForeground(new java.awt.Color(51, 51, 51));
        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoTabelaG.png"))); // NOI18N
        jButton22.setText(" ");
        jButton22.setAlignmentY(0.0F);
        jButton22.setBorderPainted(false);
        jButton22.setContentAreaFilled(false);
        jButton22.setEnabled(false);
        jButton22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton22, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 656, 120, 40));

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/telaInserirCliente.png"))); // NOI18N
        jLabel45.setText(" ");
        jPanel10.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 160, 810, 510));

        jLabel56.setForeground(new java.awt.Color(51, 51, 51));
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel56.setText("Editar");
        jLabel56.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel10.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(243, 656, 60, 30));

        jButton25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton25.setForeground(new java.awt.Color(51, 51, 51));
        jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoTabelaP.png"))); // NOI18N
        jButton25.setText(" ");
        jButton25.setAlignmentY(0.0F);
        jButton25.setBorderPainted(false);
        jButton25.setContentAreaFilled(false);
        jButton25.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton25.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton25.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton25, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 655, 75, 40));

        jTabbedPane1.addTab("tab2", jPanel10);

        jPanel11.setBackground(new java.awt.Color(235, 235, 235));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jList2.setBackground(new java.awt.Color(204, 204, 204));
        jList2.setForeground(new java.awt.Color(51, 51, 51));
        jList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked(evt);
            }
        });
        jPanel11.add(jList2, new org.netbeans.lib.awtextra.AbsoluteConstraints(165, 305, 220, 90));

        nomeJTFV.setBackground(new java.awt.Color(235, 235, 235));
        nomeJTFV.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        nomeJTFV.setBorder(null);
        nomeJTFV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomeJTFVActionPerformed(evt);
            }
        });
        nomeJTFV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nomeJTFVKeyReleased(evt);
            }
        });
        jPanel11.add(nomeJTFV, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 280, 200, 23));

        idnomeJTFV.setBackground(new java.awt.Color(235, 235, 235));
        idnomeJTFV.setForeground(new java.awt.Color(51, 51, 51));
        idnomeJTFV.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        idnomeJTFV.setText(" ");
        idnomeJTFV.setBorder(null);
        idnomeJTFV.setEnabled(false);
        jPanel11.add(idnomeJTFV, new org.netbeans.lib.awtextra.AbsoluteConstraints(396, 280, 30, 23));

        veiculoJTFV.setBackground(new java.awt.Color(235, 235, 235));
        veiculoJTFV.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        veiculoJTFV.setBorder(null);
        veiculoJTFV.setEnabled(false);
        veiculoJTFV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                veiculoJTFVMouseClicked(evt);
            }
        });
        veiculoJTFV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                veiculoJTFVActionPerformed(evt);
            }
        });
        veiculoJTFV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                veiculoJTFVKeyReleased(evt);
            }
        });
        jPanel11.add(veiculoJTFV, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 467, 200, 23));

        valorJTFV.setBackground(new java.awt.Color(235, 235, 235));
        valorJTFV.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        valorJTFV.setBorder(null);
        jPanel11.add(valorJTFV, new org.netbeans.lib.awtextra.AbsoluteConstraints(627, 466, 70, 23));

        vendedorJTFV.setBackground(new java.awt.Color(235, 235, 235));
        vendedorJTFV.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        vendedorJTFV.setBorder(null);
        jPanel11.add(vendedorJTFV, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 503, 200, 23));

        idvendedorJTFV.setBackground(new java.awt.Color(235, 235, 235));
        idvendedorJTFV.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        idvendedorJTFV.setBorder(null);
        jPanel11.add(idvendedorJTFV, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 504, 30, 23));

        statusJTFV.setBackground(new java.awt.Color(235, 235, 235));
        statusJTFV.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        statusJTFV.setText(" ");
        statusJTFV.setBorder(null);
        statusJTFV.setEnabled(false);
        jPanel11.add(statusJTFV, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 510, 60, 23));

        dataJTFV.setBorder(null);
        try {
            dataJTFV.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####/##/##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataJTFV.setOpaque(false);
        dataJTFV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataJTFVActionPerformed(evt);
            }
        });
        jPanel11.add(dataJTFV, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 538, 70, 25));

        documentoJTFV.setBorder(null);
        documentoJTFV.setForeground(new java.awt.Color(51, 51, 51));
        documentoJTFV.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        documentoJTFV.setEnabled(false);
        documentoJTFV.setOpaque(false);
        documentoJTFV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                documentoJTFVActionPerformed(evt);
            }
        });
        jPanel11.add(documentoJTFV, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 388, 70, 25));

        telefoneJTFV.setBackground(new java.awt.Color(235, 235, 235));
        telefoneJTFV.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        telefoneJTFV.setText(" ");
        telefoneJTFV.setBorder(null);
        telefoneJTFV.setEnabled(false);
        jPanel11.add(telefoneJTFV, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 352, 60, 23));

        placaJTFV.setBackground(new java.awt.Color(235, 235, 235));
        placaJTFV.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        placaJTFV.setText(" ");
        placaJTFV.setBorder(null);
        placaJTFV.setEnabled(false);
        jPanel11.add(placaJTFV, new org.netbeans.lib.awtextra.AbsoluteConstraints(495, 468, 60, 23));

        nascimentoJTFV.setBorder(null);
        nascimentoJTFV.setForeground(new java.awt.Color(51, 51, 51));
        nascimentoJTFV.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("y/MM/d"))));
        nascimentoJTFV.setEnabled(false);
        nascimentoJTFV.setOpaque(false);
        nascimentoJTFV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nascimentoJTFVActionPerformed(evt);
            }
        });
        jPanel11.add(nascimentoJTFV, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 350, 70, 25));

        idveiculoJTFV.setBackground(new java.awt.Color(235, 235, 235));
        idveiculoJTFV.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        idveiculoJTFV.setBorder(null);
        idveiculoJTFV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idveiculoJTFVActionPerformed(evt);
            }
        });
        idveiculoJTFV.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                idveiculoJTFVKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                idveiculoJTFVKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                idveiculoJTFVKeyTyped(evt);
            }
        });
        jPanel11.add(idveiculoJTFV, new org.netbeans.lib.awtextra.AbsoluteConstraints(395, 468, 30, 23));

        emailJTFV.setBackground(new java.awt.Color(235, 235, 235));
        emailJTFV.setForeground(new java.awt.Color(51, 51, 51));
        emailJTFV.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        emailJTFV.setText(" ");
        emailJTFV.setBorder(null);
        emailJTFV.setEnabled(false);
        jPanel11.add(emailJTFV, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 315, 250, 23));

        ocupacaoJTFV.setBackground(new java.awt.Color(235, 235, 235));
        ocupacaoJTFV.setForeground(new java.awt.Color(51, 51, 51));
        ocupacaoJTFV.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        ocupacaoJTFV.setText(" ");
        ocupacaoJTFV.setBorder(null);
        ocupacaoJTFV.setEnabled(false);
        ocupacaoJTFV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ocupacaoJTFVActionPerformed(evt);
            }
        });
        jPanel11.add(ocupacaoJTFV, new org.netbeans.lib.awtextra.AbsoluteConstraints(342, 388, 60, 23));

        jLabel57.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(75, 75, 75));
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel57.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Icon awesome-car-side.png"))); // NOI18N
        jLabel57.setText(" Venda\\inserir");
        jPanel11.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 280, 60));

        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel58.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/barra esticada.png"))); // NOI18N
        jLabel58.setText(" ");
        jLabel58.setPreferredSize(new java.awt.Dimension(283, 185));
        jPanel11.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 74, 810, 100));

        jLabel59.setForeground(new java.awt.Color(51, 51, 51));
        jLabel59.setText("Cancelar");
        jLabel59.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel11.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(72, 656, -1, 30));

        jLabel60.setForeground(new java.awt.Color(51, 51, 51));
        jLabel60.setText("Concluir");
        jLabel60.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel11.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(164, 656, 50, 30));

        jButton26.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton26.setForeground(new java.awt.Color(51, 51, 51));
        jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoTabelaP.png"))); // NOI18N
        jButton26.setText(" ");
        jButton26.setAlignmentY(0.0F);
        jButton26.setBorderPainted(false);
        jButton26.setContentAreaFilled(false);
        jButton26.setEnabled(false);
        jButton26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton26.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton26.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton26, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 655, 75, 40));

        jButton27.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton27.setForeground(new java.awt.Color(51, 51, 51));
        jButton27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoTabelaP.png"))); // NOI18N
        jButton27.setText(" ");
        jButton27.setAlignmentY(0.0F);
        jButton27.setBorderPainted(false);
        jButton27.setContentAreaFilled(false);
        jButton27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton27.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton27.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botao tabelaP dark.png"))); // NOI18N
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton27, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 655, 75, 40));

        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/telaVenda.png"))); // NOI18N
        jLabel62.setText(" ");
        jPanel11.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 160, 810, 510));

        jTabbedPane1.addTab("tab2", jPanel11);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, -30, 840, 730));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jTabbedPane1.setSelectedIndex(0);
        icone();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jTabbedPane1.setSelectedIndex(2);
        icone();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
        icone();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jTabbedPane1.setSelectedIndex(3);
        icone();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jTabbedPane1.setSelectedIndex(5);
        icone();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jTabbedPane1.setSelectedIndex(4);
        icone();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jPanel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MousePressed
        positionX = evt.getX();
        positionY = evt.getY();
    }//GEN-LAST:event_jPanel5MousePressed

    private void jPanel5MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseDragged
        setLocation(evt.getXOnScreen() - positionX, evt.getYOnScreen() - positionY);
    }//GEN-LAST:event_jPanel5MouseDragged

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        jTabbedPane1.setSelectedIndex(6);
        jButton13.setEnabled(false);
        jButton11.setEnabled(true);

        statusJTF1.setEnabled(true);
        proprietarioJTF.setEnabled(true);
        marcaJTF.setEnabled(true);
        modeloJTF.setEnabled(true);
        anoJTF.setEnabled(true);
        corJTF.setEnabled(true);
        placaJTF.setEnabled(true);
        kmJTF.setEnabled(true);
        motorJTF.setEnabled(true);
        potenciaJTF.setEnabled(true);
        cambioJTF.setEnabled(true);
        carroceriaJTF.setEnabled(true);
        renavamJTF.setEnabled(true);
        finalPlacaJTF.setEnabled(true);
        valorJTF.setEnabled(true);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

        try {

            String marca = marcaJTF.getText();
            String modelo = modeloJTF.getText();
            String ano = anoJTF.getText();
            String status = statusJTF1.getSelectedItem().toString();
            String cor = corJTF.getText();
            String km = kmJTF.getText();
            String motor = motorJTF.getText();
            String placa = placaJTF.getText();
            String potencia = potenciaJTF.getText();
            String cambio = cambioJTF.getText();
            String carroceria = carroceriaJTF.getSelectedItem().toString();
            String renavam = renavamJTF.getText();
            String finalplaca = finalPlacaJTF.getText();
            String valor = valorJTF.getText();
            String proprietario = proprietarioJTF.getText();

            ResultSet result3 = statement.executeQuery("SELECT idcliente FROM cliente WHERE nome LIKE " + "'" + String.valueOf(jList1.getSelectedValue()) + "'");
            while (result3.next()) {
                idclienteJTF.setText(String.valueOf(result3.getInt(1)));

            }

            String idcliente = idclienteJTF.getText();
            int idclienteI = Integer.parseInt(idcliente);

            if (carroceria == "") {
                carroceria = "null";
            } else if (motor == "") {
                motor = "null";
            } else if (placa == "") {
                placa = "null";
            } else if (potencia == "") {
                potencia = "null";
            } else if (cambio == "") {
                cambio = "null";
            } else if (carroceria == "") {
                carroceria = "null";
            } else if (renavam == "") {
                renavam = "null";
            } else if (finalplaca == "") {
                finalplaca = "null";
            } else if (valor == "") {
                valor = "null";
            }

            try {
                if (proprietario.equals("") || marca.equals("") || modelo.equals("") || ano.equals("") || placa.equals("")) {
                    throw new Exception("Preencha todos os campos obrigatórios!!");
                }

                statement.executeUpdate("INSERT INTO veiculo VALUES(null,'"
                        + marca + "','"
                        + modelo + "',"
                        + ano + ",'"
                        + cor + "','"
                        + placa + "','"
                        + km + "','"
                        + motor + "','"
                        + potencia + "','"
                        + cambio + "','"
                        + carroceria + "','"
                        + renavam + "','"
                        + finalplaca + "','"
                        + valor + "','"
                        + proprietario + "','"
                        + idclienteI + "')");

                popularTabela();
                JOptionPane.showMessageDialog(null, "Veiculo inserido com sucesso!");
                limparTudo();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Ano, potencia e km devem ser números!",
                        "ERRO",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(),
                        "ERRO",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            Logger.getLogger(telaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton11ActionPerformed

    private void limparTudo() {
        marcaJTF.setText("");
        modeloJTF.setText("");
        anoJTF.setText("");
        corJTF.setText("");
        placaJTF.setText("");
        kmJTF.setText("");
        motorJTF.setText("");
        potenciaJTF.setText("");
        cambioJTF.setText("");
        carroceriaJTF.setSelectedIndex(1);
        renavamJTF.setText("");
        finalPlacaJTF.setText("");
        valorJTF.setText("");
        proprietarioJTF.setText("");
        idclienteJTF.setText("");

        nomeJTF.setText("");
        emailJTF.setText("");
        telefoneJTF.setText("");
        documentoJTF.setText("");
        ocupacaoJTF.setText("");
        generoJTF.setSelectedIndex(0);
        nascimentoJTF.setText("");
        tipoJTF.setSelectedIndex(0);

        nomeJTFV.setText("");
        idnomeJTFV.setText("");
        emailJTFV.setText("");
        telefoneJTFV.setText("");
        documentoJTFV.setText("");
        nascimentoJTFV.setText("");
        ocupacaoJTFV.setText("");
        nomeJTFV.setText("");
        placaJTFV.setText("");
        valorJTFV.setText("");
        vendedorJTFV.setText("");
        idvendedorJTFV.setText("");
        dataJTFV.setText("");
        veiculoJTFV.setText("");

    }


    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        jTabbedPane1.setSelectedIndex(1);
        limparTudo();

        statusJTF1.setEnabled(false);
        proprietarioJTF.setEnabled(false);
        marcaJTF.setEnabled(false);
        modeloJTF.setEnabled(false);
        anoJTF.setEnabled(false);
        corJTF.setEnabled(false);
        placaJTF.setEnabled(false);
        kmJTF.setEnabled(false);
        motorJTF.setEnabled(false);
        potenciaJTF.setEnabled(false);
        cambioJTF.setEnabled(false);
        carroceriaJTF.setEnabled(false);
        renavamJTF.setEnabled(false);
        finalPlacaJTF.setEnabled(false);
        valorJTF.setEnabled(false);
        jButton13.setEnabled(false);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Tem certeza que quer remover?") == JOptionPane.YES_OPTION) {
            try {
                int linha = jTableE.getSelectedRow();

                statement.executeUpdate("DELETE FROM veiculo WHERE idveiculo=" + jTableE.getValueAt(linha, 0));
                limparTudo();
                popularTabela();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(),
                        "ERRO",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTableEMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableEMouseClicked

        if (evt.getClickCount() == 2) {

            try {
                int linha = jTableE.getSelectedRow();
                String id = (String) ((DefaultTableModel) jTableE.getModel()).getValueAt(linha, 14);
                int idd = Integer.parseInt(id);

                ResultSet result1 = statement.executeQuery("SELECT nome FROM cliente WHERE idcliente LIKE " + idd);
                while (result1.next()) {
                    proprietarioJTF.setText(result1.getString(1));
                }

                marcaJTF.setText((String) ((DefaultTableModel) jTableE.getModel()).getValueAt(linha, 1));
                modeloJTF.setText((String) ((DefaultTableModel) jTableE.getModel()).getValueAt(linha, 2));
                anoJTF.setText((String) ((DefaultTableModel) jTableE.getModel()).getValueAt(linha, 3));
                corJTF.setText((String) ((DefaultTableModel) jTableE.getModel()).getValueAt(linha, 4));
                placaJTF.setText((String) ((DefaultTableModel) jTableE.getModel()).getValueAt(linha, 5));
                kmJTF.setText((String) ((DefaultTableModel) jTableE.getModel()).getValueAt(linha, 6));
                motorJTF.setText((String) ((DefaultTableModel) jTableE.getModel()).getValueAt(linha, 7));
                potenciaJTF.setText((String) ((DefaultTableModel) jTableE.getModel()).getValueAt(linha, 8));
                cambioJTF.setText((String) ((DefaultTableModel) jTableE.getModel()).getValueAt(linha, 9));
                carroceriaJTF.setName((String) ((DefaultTableModel) jTableE.getModel()).getValueAt(linha, 10));
                renavamJTF.setText((String) ((DefaultTableModel) jTableE.getModel()).getValueAt(linha, 11));
                finalPlacaJTF.setText((String) ((DefaultTableModel) jTableE.getModel()).getValueAt(linha, 12));
                valorJTF.setText((String) ((DefaultTableModel) jTableE.getModel()).getValueAt(linha, 13));
                idclienteJTF.setText(String.valueOf(idd));

                jTabbedPane1.setSelectedIndex(6);
                jButton13.setEnabled(false);
                jButton11.setEnabled(false);

            } catch (SQLException ex) {
                Logger.getLogger(telaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jTableEMouseClicked

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        String marca = marcaJTF.getText();
        String modelo = modeloJTF.getText();
        String ano = anoJTF.getText();
        String status = statusJTF1.getSelectedItem().toString();
        String cor = corJTF.getText();
        String km = kmJTF.getText();
        String motor = motorJTF.getText();
        String placa = placaJTF.getText();
        String potencia = potenciaJTF.getText();
        String cambio = cambioJTF.getText();
        String carroceria = carroceriaJTF.getSelectedItem().toString();
        String renavam = renavamJTF.getText();
        String finalplaca = finalPlacaJTF.getText();
        String valor = valorJTF.getText();
        String proprietario = proprietarioJTF.getText();
        String idcliente = idclienteJTF.getText();

        try {
            int linha = jTableE.getSelectedRow();
            if (proprietario.equals("") || marca.equals("") || modelo.equals("") || ano.equals("") || km.equals("") || placa.equals("")) {
                throw new Exception("Preencha todos os campos!!");
            }

            statement.executeUpdate("UPDATE veiculo SET "
                    + "marca= '" + marca
                    + "', modelo= '" + modelo
                    + "', ano= '" + ano
                    + "', cor= '" + cor
                    + "', km= '" + km
                    + "', motor= '" + motor
                    + "', placa= '" + placa
                    + "', potencia= '" + potencia
                    + "', cambio= '" + cambio
                    + "', carroceria= '" + carroceria
                    + "', renavam= '" + renavam
                    + "', finalplaca= '" + finalplaca
                    + "', valor= '" + valor
                    + "', proprietario= '" + proprietario
                    + "', cliente_idcliente= '" + idcliente
                    + "'" + " WHERE idveiculo= " + jTableE.getValueAt(linha, 0));

            JOptionPane.showMessageDialog(null, "Veiculo editado com sucesso!");
            limparTudo();
            popularTabela();

            jTabbedPane1.setSelectedIndex(1);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Preço e quantidade devem ser números!",
                    "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jTableCMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableCMouseClicked
        if (evt.getClickCount() == 2) {

            jTabbedPane1.setSelectedIndex(7);
            int linha = jTableC.getSelectedRow();

            nomeJTF.setText((String) ((DefaultTableModel) jTableC.getModel()).getValueAt(linha, 1));
            emailJTF.setText((String) ((DefaultTableModel) jTableC.getModel()).getValueAt(linha, 2));
            telefoneJTF.setText((String) ((DefaultTableModel) jTableC.getModel()).getValueAt(linha, 3));
            documentoJTF.setText((String) ((DefaultTableModel) jTableC.getModel()).getValueAt(linha, 4));
            ocupacaoJTF.setText((String) ((DefaultTableModel) jTableC.getModel()).getValueAt(linha, 5));
            //genero.setText((String) ((DefaultTableModel) jTableC.getModel()).getValueAt(linha, 6));
            nascimentoJTF.setText((String) ((DefaultTableModel) jTableC.getModel()).getValueAt(linha, 7));
            // tipoJTF.setText((String) ((DefaultTableModel) jTableC.getModel()).getValueAt(linha, 8));

        }
    }//GEN-LAST:event_jTableCMouseClicked

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        popularTabela();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        jTabbedPane1.setSelectedIndex(7);
        jButton25.setEnabled(false);
        jButton20.setEnabled(true);
        jButton22.setEnabled(false);
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        popularTabela();
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed

        try {
            String nome = nomeJTF.getText();
            String email = emailJTF.getText();
            String telefone = telefoneJTF.getText();
            String documento = documentoJTF.getText();
            String ocupacao = ocupacaoJTF.getText();
            String genero = (String) generoJTF.getSelectedItem();
            String dataNascimento = nascimentoJTF.getText();
            String tipoDocumento = (String) tipoJTF.getSelectedItem();

            if (nome.equals("") || email.equals("") || telefone.equals("") || documento.equals("") || ocupacao.equals("") || genero.equals("") || dataNascimento.equals("") || tipoDocumento.equals("")) {
                throw new Exception("Preencha todos os campos !!");
            }

            statement.executeUpdate("INSERT INTO cliente VALUES(null,'"
                    + nome + "','"
                    + email + "',"
                    + telefone + ",'"
                    + documento + "','"
                    + ocupacao + "','"
                    + genero + "','"
                    + dataNascimento + "','"
                    + tipoDocumento + "')");

            popularTabela();
            JOptionPane.showMessageDialog(null, "Cliente inserido com sucesso!");
            limparTudo();
            jTabbedPane1.setSelectedIndex(5);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "telefone, e data devem ser numeros!",
                    "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        jTabbedPane1.setSelectedIndex(5);
        jButton22.setEnabled(false);
        nomeJTF.setEnabled(false);
        emailJTF.setEnabled(false);
        telefoneJTF.setEnabled(false);
        documentoJTF.setEnabled(false);
        ocupacaoJTF.setEnabled(false);
        generoJTF.setEnabled(false);
        nascimentoJTF.setEnabled(false);
        tipoJTF.setEnabled(false);
        limparTudo();// TODO add your handling code here:
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed

        String nome = nomeJTF.getText();
        String email = emailJTF.getText();
        String telefone = telefoneJTF.getText();
        String documento = documentoJTF.getText();
        String ocupacao = ocupacaoJTF.getText();
        String genero = (String) generoJTF.getSelectedItem();
        String dataNascimento = nascimentoJTF.getText();
        String tipoDocumento = (String) tipoJTF.getSelectedItem();

        try {
            int linha = jTableC.getSelectedRow();
            if (nome.equals("") || email.equals("") || telefone.equals("") || documento.equals("") | ocupacao.equals("")) {
                throw new Exception("Preencha todos os campos!!");
            }

            statement.executeUpdate("UPDATE cliente SET "
                    + "nome= '" + nome
                    + "', email= '" + email
                    + "', telefone= '" + telefone
                    + "', documento= '" + documento
                    + "', ocupacao= '" + ocupacao
                    + "', genero= '" + genero
                    + "', dataNascimento= '" + dataNascimento
                    + "', tipo= '" + tipoDocumento
                    + "'" + " WHERE idcliente= " + jTableC.getValueAt(linha, 0));

            JOptionPane.showMessageDialog(null, "Editado com sucesso!");
            limparTudo();
            popularTabela();

            jTabbedPane1.setSelectedIndex(5);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "telefone e documento devem ser números!",
                    "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton22ActionPerformed

    private void ocupacaoJTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ocupacaoJTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ocupacaoJTFActionPerformed

    private void tipoJTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoJTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoJTFActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

        jButton13.setEnabled(true);

        statusJTF1.setEnabled(true);
        proprietarioJTF.setEnabled(true);
        marcaJTF.setEnabled(true);
        modeloJTF.setEnabled(true);
        anoJTF.setEnabled(true);
        corJTF.setEnabled(true);
        placaJTF.setEnabled(true);
        kmJTF.setEnabled(true);
        motorJTF.setEnabled(true);
        potenciaJTF.setEnabled(true);
        cambioJTF.setEnabled(true);
        carroceriaJTF.setEnabled(true);
        renavamJTF.setEnabled(true);
        finalPlacaJTF.setEnabled(true);
        valorJTF.setEnabled(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void proprietarioJTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proprietarioJTFActionPerformed
        jList1.setVisible(false);
        Enter = 1;


    }//GEN-LAST:event_proprietarioJTFActionPerformed

    private void proprietarioJTFKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_proprietarioJTFKeyReleased
        if (Enter == 0) {
            ListaDePesquisa();
        } else
            Enter = 0;
    }//GEN-LAST:event_proprietarioJTFKeyReleased

    private void proprietarioJTFKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_proprietarioJTFKeyPressed

    }//GEN-LAST:event_proprietarioJTFKeyPressed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked

        proprietarioJTF.setText(String.valueOf(jList1.getSelectedValue()));
        jList1.setVisible(false);
    }//GEN-LAST:event_jList1MouseClicked

    private void jTableEMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableEMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableEMouseEntered

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed

        Enter = 1;

    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        if (Enter == 0) {
            ListaDePesquisa();
        } else {
            Enter = 0;
        }


    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        if (Enter == 0) {
            ListaDePesquisa();
        } else {
            Enter = 0;
        }

    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        Enter = 1;
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Tem certeza que quer remover?") == JOptionPane.YES_OPTION) {
            try {
                int linha = jTableC.getSelectedRow();

                statement.executeUpdate("DELETE FROM cliente WHERE idcliente=" + jTableC.getValueAt(linha, 0));
                limparTudo();
                popularTabela();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(),
                        "ERRO",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jTextField2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField2MouseClicked
        jTextField2.setText("");
    }//GEN-LAST:event_jTextField2MouseClicked

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        jTextField1.setText("");
    }//GEN-LAST:event_jTextField1MouseClicked

    private void jTableNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableNMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableNMouseClicked

    private void jTableNMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableNMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableNMouseEntered

    private void jTableVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableVMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableVMouseClicked

    private void jTableVMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableVMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableVMouseEntered

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        jTabbedPane1.setSelectedIndex(8);
        jButton26.setEnabled(true);


    }//GEN-LAST:event_jButton24ActionPerformed

    private void generoJTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generoJTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_generoJTFActionPerformed

    private void nascimentoJTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nascimentoJTFActionPerformed


    }//GEN-LAST:event_nascimentoJTFActionPerformed

    private void documentoJTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documentoJTFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_documentoJTFActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        jButton22.setEnabled(true);
        nomeJTF.setEnabled(true);
        emailJTF.setEnabled(true);
        telefoneJTF.setEnabled(true);
        documentoJTF.setEnabled(true);
        ocupacaoJTF.setEnabled(true);
        generoJTF.setEnabled(true);
        nascimentoJTF.setEnabled(true);
        tipoJTF.setEnabled(true);
    }//GEN-LAST:event_jButton25ActionPerformed

    private void documentoJTFVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_documentoJTFVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_documentoJTFVActionPerformed

    private void ocupacaoJTFVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ocupacaoJTFVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ocupacaoJTFVActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed

        try {
                        
            String idvendedor = idvendedorJTFV.getText();
            String idveiculo = idveiculoJTFV.getText();
            String data = dataJTFV.getText().replaceAll("-", "/");
            String idcliente = idnomeJTFV.getText();
            String valor = valorJTFV.getText();
            String nomeCliente = nomeJTFV.getText();
            String nomeVeiculo = veiculoJTFV.getText();
            String Status = statusJTFV.getText();
         
            
            
            
            if (idvendedor.equals("") || data.equals("") || idveiculo.equals("") || idcliente.equals("") || valor.equals("")) {
                throw new Exception("Preencha todos os campos !!");
                
            } else if (Status.equals("off")){
                throw new Exception("Insira um veiculo valido !!");
                
            }

            statement.executeUpdate("INSERT INTO venda VALUES(null, "
                    + idvendedor + ",'"
                    + data + "',"
                    + idveiculo + ","
                    + idcliente + ",'"
                    + valor + "','"
                    + nomeCliente + "','"
                    + nomeVeiculo + "')");

            JOptionPane.showMessageDialog(null, "Venda cadastrada com sucesso!");
            statement.executeUpdate("UPDATE veiculo SET status = 'off' WHERE idveiculo= " + idveiculo);
            popularTabela();
            limparTudo();
            jTabbedPane1.setSelectedIndex(4);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "valor, e data devem ser numeros!",
                    "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "ERRO",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        jTabbedPane1.setSelectedIndex(4);
        limparTudo();
    }//GEN-LAST:event_jButton27ActionPerformed

    private void nascimentoJTFVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nascimentoJTFVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nascimentoJTFVActionPerformed

    private void dataJTFVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dataJTFVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dataJTFVActionPerformed

    private void jList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseClicked

        nomeJTFV.setText(String.valueOf(jList2.getSelectedValue()));
        jList2.setVisible(false);

        try {

            result6 = statement.executeQuery("SELECT * FROM cliente where nome like '" + nomeJTFV.getText() + "'");
            while (result6.next()) {
                idnomeJTFV.setText(String.valueOf(result6.getInt(1)));
                emailJTFV.setText(result6.getString(3));
                telefoneJTFV.setText(String.valueOf(result6.getInt(4)));
                documentoJTFV.setText(result6.getString(5));
                nascimentoJTFV.setText(String.valueOf(result6.getDate(8)).replaceAll("-", "/"));
                ocupacaoJTFV.setText(result6.getString(6));

            }
        } catch (SQLException ex) {
            Logger.getLogger(telaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jList2MouseClicked

    private void nomeJTFVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomeJTFVActionPerformed
        jList2.setVisible(false);
        Enter = 1;
    }//GEN-LAST:event_nomeJTFVActionPerformed

    private void nomeJTFVKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomeJTFVKeyReleased
        if (Enter == 0) {
            ListaDePesquisa();
        } else
            Enter = 0;
    }//GEN-LAST:event_nomeJTFVKeyReleased

    private void veiculoJTFVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_veiculoJTFVActionPerformed

    }//GEN-LAST:event_veiculoJTFVActionPerformed

    private void veiculoJTFVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_veiculoJTFVMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_veiculoJTFVMouseClicked

    private void veiculoJTFVKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_veiculoJTFVKeyReleased

    }//GEN-LAST:event_veiculoJTFVKeyReleased

    private void idveiculoJTFVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idveiculoJTFVActionPerformed

    }//GEN-LAST:event_idveiculoJTFVActionPerformed

    private void idveiculoJTFVKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idveiculoJTFVKeyReleased
        try {
            result6 = statement.executeQuery("SELECT * FROM veiculo where idveiculo like '" + idveiculoJTFV.getText() + "'");
            while (result6.next()) {
                veiculoJTFV.setText(result6.getString(2) + " " + result6.getString(3) + " " + String.valueOf(result6.getInt(4)) + " " + result6.getString(5));
                placaJTFV.setText(result6.getString(6));
                statusJTFV.setText(result6.getString(17));
                valorJTFV.setText(String.valueOf(result6.getInt(14)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(telaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_idveiculoJTFVKeyReleased

    private void idveiculoJTFVKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idveiculoJTFVKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_idveiculoJTFVKeyTyped

    private void idveiculoJTFVKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idveiculoJTFVKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_idveiculoJTFVKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(telaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(telaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(telaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(telaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new telaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField anoJTF;
    private javax.swing.JTextField cambioJTF;
    private javax.swing.JComboBox<String> carroceriaJTF;
    private javax.swing.JTextField corJTF;
    private javax.swing.JFormattedTextField dataJTFV;
    private javax.swing.JFormattedTextField documentoJTF;
    private javax.swing.JFormattedTextField documentoJTFV;
    private javax.swing.JTextField emailJTF;
    private javax.swing.JTextField emailJTFV;
    private javax.swing.JTextField finalPlacaJTF;
    private javax.swing.JComboBox<String> generoJTF;
    private javax.swing.JTextField idclienteJTF;
    private javax.swing.JTextField idnomeJTFV;
    private javax.swing.JTextField idveiculoJTFV;
    private javax.swing.JTextField idvendedorJTFV;
    public static javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton19;
    public static javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    public static javax.swing.JButton jButton3;
    public static javax.swing.JButton jButton4;
    public static javax.swing.JButton jButton5;
    public static javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    public static javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JScrollPane jScrollPane3;
    public static javax.swing.JScrollPane jScrollPane4;
    public static javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
    public static javax.swing.JTable jTableC;
    public static javax.swing.JTable jTableE;
    public static javax.swing.JTable jTableN;
    public static javax.swing.JTable jTableV;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    public static javax.swing.JLabel jtotalEstoque;
    public static javax.swing.JLabel jtotalVenda;
    private javax.swing.JTextField kmJTF;
    private javax.swing.JTextField marcaJTF;
    private javax.swing.JTextField modeloJTF;
    private javax.swing.JTextField motorJTF;
    private javax.swing.JFormattedTextField nascimentoJTF;
    private javax.swing.JFormattedTextField nascimentoJTFV;
    private javax.swing.JTextField nomeJTF;
    private javax.swing.JTextField nomeJTFV;
    private javax.swing.JTextField ocupacaoJTF;
    private javax.swing.JTextField ocupacaoJTFV;
    private javax.swing.JTextField placaJTF;
    private javax.swing.JTextField placaJTFV;
    private javax.swing.JTextField potenciaJTF;
    private javax.swing.JTextField proprietarioJTF;
    private javax.swing.JTextField renavamJTF;
    private javax.swing.JComboBox<String> statusJTF1;
    private javax.swing.JTextField statusJTFV;
    private javax.swing.JTextField telefoneJTF;
    private javax.swing.JTextField telefoneJTFV;
    private javax.swing.JComboBox<String> tipoJTF;
    private javax.swing.JTextField valorJTF;
    private javax.swing.JTextField valorJTFV;
    private javax.swing.JTextField veiculoJTFV;
    private javax.swing.JTextField vendedorJTFV;
    // End of variables declaration//GEN-END:variables
}
