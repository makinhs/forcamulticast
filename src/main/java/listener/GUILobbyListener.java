package listener;

import java.awt.Component;
import java.awt.event.ActionEvent;

import view.GUILobby;

public class GUILobbyListener extends BaseListener {

    private GUILobby panel;

    public GUILobbyListener(GUILobby panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Component c = (Component) e.getSource();
        System.out.println("Teste");

    }
}
