package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import util.GUIConstants;

public class GUITelaInicial extends BasePanel {

    private JFrame mainFrame;
    private JTextField textFieldNome;
    private JButton buttonJogar;

    public GUITelaInicial(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setSize(GUIConstants.FRAME_WIDTH, GUIConstants.FRAME_HEIGHT);
        this.setVisible(true);
        this.initComponents();
        mainFrame.add(this);
        mainFrame.invalidate();
        mainFrame.repaint();
    }

    private void initComponents() {
        textFieldNome = new JTextField();
        textFieldNome.setBounds(200, 175, GUIConstants.BASE_COMPONENT_WIDTH, GUIConstants.BASE_COMPONENT_HEIGHT);
        this.add(textFieldNome);

        buttonJogar = new JButton("Jogar");
        buttonJogar.setBounds(200, 205, GUIConstants.BASE_COMPONENT_WIDTH, GUIConstants.BASE_COMPONENT_HEIGHT);
        this.add(buttonJogar);
    }
}
