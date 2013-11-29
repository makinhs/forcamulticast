package listener;

import java.awt.Component;
import java.awt.event.ActionEvent;

import model.Jogador;
import view.GUILobby;

public class GUILobbyListener extends BaseListener {

    private GUILobby panel;

    public GUILobbyListener(GUILobby panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component c = (Component) e.getSource();
        if (c.equals(panel.getButtonChutar())) {
            Jogador j = panel.getJogador();
            String s = panel.getTextFieldPalavra().getText();
            if (j.isClient()) {
                j.getClient().enviarChute(s);
            }
        }
    }
}
