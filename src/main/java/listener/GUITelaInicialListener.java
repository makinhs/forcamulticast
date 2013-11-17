package listener;

import java.awt.Component;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import view.GUILobby;
import view.GUITelaInicial;

public class GUITelaInicialListener extends BaseListener {

    private GUITelaInicial panel;

    public GUITelaInicialListener(GUITelaInicial panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component c = (Component) e.getSource();
        if (c.equals(panel.getButtonJogar())) {
            String username = panel.getTextFieldNome().getText();
            if(username == null || username.isEmpty()) {
                panel.getLabelErro().setText("O preenchimento do nick é obrigatório");
            } else {
                panel.changePanel(new GUILobby(panel.getMainFrame()));
            }
        }
    }
}
