package model;

import ctrl.MultiCastPeer;
import java.util.ArrayList;
import java.util.Random;

public class Jogador {

    private Integer id;
    private String nick;
    private MultiCastPeer multicastConnection;
    private boolean server;
    private ArrayList<Integer> idProcessos;
    private int cont = 0;

    public Jogador(String nick) {
        this.nick = nick;
        this.server = false;
        idProcessos = new ArrayList<Integer>();
        Random r = new Random();
        id = r.nextInt(10000);
        addIdJogadores(id);
        info();
        multicastConnection = new MultiCastPeer(this);
    }

    public void addIdJogadores(int id) {
        boolean insereId = true;

        for (Integer idJogadores : idProcessos) {
            if (idJogadores == id) {
                insereId = false;
                break;
            }
        }

        if (insereId) {
            idProcessos.add(id);
            cont++;
        }
    }
    
    public boolean isDefinindoJogadores()
    {
        if(cont <4)
        {
            return true;
        }
        else
        {
            eleicao();
            return false;
        }
    }

    public void eleicao() {
        int idJogadorEleito = idProcessos.get(0);

        for (int i = 1; i < idProcessos.size(); i++) {
            if (i > idJogadorEleito) {
                idJogadorEleito = i;
            }
        }
        
        if(this.id == idJogadorEleito) 
            server = true;
        else 
            server = false;
    }

    public void info() {
        System.out.println("ID: " + this.id);
        System.out.println("Nick: " + this.nick);
    }
}
