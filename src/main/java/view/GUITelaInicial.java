package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import listener.BaseListener;
import listener.GUITelaInicialListener;
import util.GUIConstants;

public class GUITelaInicial extends BasePanel {

    private static final long serialVersionUID = 1L;
    private JTextField textFieldNome;
    private JButton buttonJogar;
    private BaseListener listener;

    public GUITelaInicial(JFrame mainFrame) {
        super(mainFrame);
        listener = new GUITelaInicialListener(this);
        this.setSize(GUIConstants.FRAME_WIDTH, GUIConstants.FRAME_HEIGHT);
        this.setVisible(true);
        this.initComponents();
        mainFrame.add(this);
    }

    private void initComponents() {
        textFieldNome = new JTextField();
        textFieldNome.setBounds(GUIConstants.HORIZONAL_CENTER_POS, 175, GUIConstants.BASE_COMPONENT_WIDTH, GUIConstants.BASE_COMPONENT_HEIGHT);
        this.add(textFieldNome);

        buttonJogar = new JButton("Jogar");
        buttonJogar.setBounds(GUIConstants.HORIZONAL_CENTER_POS, 205, GUIConstants.BASE_COMPONENT_WIDTH, GUIConstants.BASE_COMPONENT_HEIGHT);
        buttonJogar.addActionListener(listener);
        this.add(buttonJogar);
    }

    public JTextField getTextFieldNome() {
        return textFieldNome;
    }

    public JButton getButtonJogar() {
        return buttonJogar;
    }

}
