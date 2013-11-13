package view;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BasePanel extends JPanel {

    /**
     * Método auxiliar responsável pelos eventos de troca de panel
     * 
     * @param oldPanel
     *            referência do panel a ser removido
     * @param newPanel
     *            referencia do panel que será inserido
     * @param frame
     *            frame em que alterações ocorrerão
     */
    protected void changePanel(Component oldPanel, Component newPanel, JFrame frame) {
        frame.remove(oldPanel);
        frame.add(newPanel);
        frame.validate();
        frame.repaint();
    }

}
