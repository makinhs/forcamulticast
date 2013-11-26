package model;

import ctrl.MultiCastPeer;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Serializer;

public class Jogador implements Serializable {

    private int id;
    private String nick;
    private transient MultiCastPeer multicastConnection;
    private transient boolean server = false;
    private transient boolean client = false;
    private transient ArrayList<Integer> idProcessos;
    private transient ArrayList<Jogador> listaJogadores;
    private transient int cont = 0;

    public Jogador(String nick) {
        this.nick = nick;
        this.server = false;
        idProcessos = new ArrayList<Integer>();
        listaJogadores = new ArrayList<Jogador>();
        Random r = new Random();
        id = r.nextInt(10000);
        addIdJogadores(id);
        multicastConnection = new MultiCastPeer(this);
        addJogador(this);
    }

    public void addIdJogadores(int id) {
        boolean insereId = true;

        for (int idJogadores : idProcessos) {
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

    public void addJogador(Jogador jogador) {
        boolean insere = true;

        for (Jogador j : getListaJogadores()) {
            if (jogador.getId() == (j.getId())) {
                insere = false;
                break;
            }
        }

        if (insere) {
            getListaJogadores().add(jogador);
        }
    }

    public boolean isDefinindoJogadores() {
        if (cont < 4) {
            return true;
        } else {
            eleicao();
            return false;
        }
    }

    public void eleicao() {
        int idJogadorEleito = getListaJogadores().get(0).getId();

        for(int i=1; i<this.getListaJogadores().size(); i++)
        {
        	if(idJogadorEleito < getListaJogadores().get(i).getId())
        	{
        		idJogadorEleito = getListaJogadores().get(i).getId();
        	}
        }

        if (this.getId() == idJogadorEleito) {
            server = true;
            client = false;
        } else {
            server = false;
            client = true;
        }
    }

    public byte[] sendInfo() {
        try {
            String msg = this.getNick() + "|" + this.getId() ;
            return Serializer.serialize(this);
        } catch (IOException ex) {
            Logger.getLogger(Jogador.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public ArrayList<Jogador> getListaJogadores() {
        return listaJogadores;
    }

	public boolean isServer() {
		return server;
	}

	public void setServer(boolean server) {
		this.server = server;
	}

	public boolean isClient() {
		return client;
	}

	public void setClient(boolean client) {
		this.client = client;
	}
    
    

    
}
