package view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import listener.BaseListener;
import listener.GUILobbyListener;
import util.GUIConstants;

public class GUILobby extends BasePanel {

    private static final long serialVersionUID = 1L;
    private BaseListener listener;
    private JLabel labelPalavra;
    private JLabel labelJogadorAtual;
    private JLabel labelTituloLetrasChutadas;
    private JLabel labelLetrasChutadas;
    private JTextField textFieldPalavra;
    private JTextField textFieldMsgChat;
    private JButton buttonChutar;
    private JTextField buttonEnviar;
    private JTextArea textAreaChat;
    

    public GUILobby(JFrame mainFrame) {
        super(mainFrame);
        listener = new GUILobbyListener(this);
        this.setSize(GUIConstants.FRAME_WIDTH, GUIConstants.FRAME_HEIGHT);
        this.setVisible(true);
        this.initComponents();
        mainFrame.add(this);
    }

    private void initComponents() {
        labelPalavra = new JLabel("T E S T E");
        labelPalavra.setBounds(10, 20, GUIConstants.FRAME_WIDTH - 20, 40);
        labelPalavra.setHorizontalAlignment(SwingConstants.CENTER);
        labelPalavra.setFont(new Font(null, Font.BOLD, 18));
        this.add(labelPalavra);

        labelJogadorAtual = new JLabel("Vez de fulano");
        labelJogadorAtual.setBounds(10, 70, GUIConstants.FRAME_WIDTH / 2, GUIConstants.BASE_COMPONENT_HEIGHT);
        labelJogadorAtual.setFont(new Font(null, Font.PLAIN, 10));
        this.add(labelJogadorAtual);

        labelTituloLetrasChutadas = new JLabel("Letras já chutadas: ");
        labelTituloLetrasChutadas.setBounds(10, 100, GUIConstants.FRAME_WIDTH / 2, GUIConstants.BASE_COMPONENT_HEIGHT);
        this.add(labelTituloLetrasChutadas);

        labelLetrasChutadas = new JLabel("Letras chutadas e formatadas aqui");
        labelLetrasChutadas.setFont(new Font(null, Font.PLAIN, 12));
        labelLetrasChutadas.setBounds(10, 120, GUIConstants.FRAME_WIDTH / 2, GUIConstants.BASE_COMPONENT_HEIGHT);
        this.add(labelLetrasChutadas);
        
        textFieldPalavra = new JTextField();
        textFieldPalavra.setBounds(10, GUIConstants.FRAME_HEIGHT - 80, 150, GUIConstants.BASE_COMPONENT_HEIGHT);
        this.add(textFieldPalavra);
        
        buttonChutar = new JButton("Chutar Letra/Palavra");
        buttonChutar.setFont(new Font(null, Font.PLAIN, 10));
        buttonChutar.setBounds(10, GUIConstants.FRAME_HEIGHT - 60, 150, GUIConstants.BASE_COMPONENT_HEIGHT);
        this.add(buttonChutar);
        
        textAreaChat = new JTextArea();
        textAreaChat.setBounds(200, 290, 290, 50);
        textAreaChat.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.add(textAreaChat);
        
        textFieldMsgChat = new JTextField();
    }

}
