package view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import util.GUIConstants;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public MainFrame() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(GUIConstants.FRAME_WIDTH, GUIConstants.FRAME_HEIGHT);
        this.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel telaInicial = new GUITelaInicial(this);
    }
}
