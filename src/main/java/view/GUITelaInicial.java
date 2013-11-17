package view;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import listener.BaseListener;
import listener.GUITelaInicialListener;
import util.GUIConstants;

public class GUITelaInicial extends BasePanel {

    private static final long serialVersionUID = 1L;
    private JLabel labelUsername;
    private JLabel labelErro;
    private JTextField textFieldNome;
    private JButton buttonJogar;
    private BaseListener listener;

    public GUITelaInicial(JFrame mainFrame) {
        super(mainFrame);
        listener = new GUITelaInicialListener(this);
        this.setSize(GUIConstants.FRAME_WIDTH, GUIConstants.FRAME_HEIGHT);
        this.setVisible(true);
        this.initComponents();
        mainFrame.getContentPane().add(this);
    }

    /**
     * Inicialização dos componentes da tela de início
     */
    private void initComponents() {
        textFieldNome = new JTextField();
        textFieldNome.setBounds(GUIConstants.HORIZONAL_CENTER_POS, 175, GUIConstants.BASE_COMPONENT_WIDTH, GUIConstants.BASE_COMPONENT_HEIGHT);
        this.add(textFieldNome);

        buttonJogar = new JButton("Jogar");
        buttonJogar.setBounds(GUIConstants.HORIZONAL_CENTER_POS, 205, GUIConstants.BASE_COMPONENT_WIDTH, GUIConstants.BASE_COMPONENT_HEIGHT);
        buttonJogar.addActionListener(listener);
        this.add(buttonJogar);

        labelUsername = new JLabel("Nick:");
        labelUsername.setBounds(GUIConstants.HORIZONAL_CENTER_POS, 155, GUIConstants.BASE_COMPONENT_WIDTH, GUIConstants.BASE_COMPONENT_HEIGHT);
        this.add(labelUsername);

        labelErro = new JLabel();
        labelErro.setBounds(GUIConstants.HORIZONAL_CENTER_POS, 235, GUIConstants.BASE_COMPONENT_WIDTH, GUIConstants.BASE_COMPONENT_HEIGHT);
        labelErro.setFont(new Font(null, Font.PLAIN, 10));
        this.add(labelErro);
    }

    public JTextField getTextFieldNome() {
        return textFieldNome;
    }

    public JButton getButtonJogar() {
        return buttonJogar;
    }

    public JLabel getLabelUsername() {
        return labelUsername;
    }

    public JLabel getLabelErro() {
        return labelErro;
    }

}
