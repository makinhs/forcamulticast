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
    private transient boolean isServer = false;
    private transient boolean isClient = false;
    private transient ArrayList<Integer> idProcessos;
    private transient ArrayList<Jogador> listaJogadores;
    private transient int cont = 0;           
    private transient Server servidorUDP = null;
    private transient Client clienteUDP = null;
    

    public Jogador(String nick) {
        this.nick = nick;
        this.isServer = false;
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
            isServer = true;
            isClient = false;
        } else {
            isServer = false;
            isClient = true;
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
		return isServer;
	}

	public void setServer(boolean server) {
		this.isServer = server;
	}

	public boolean isClient() {
		return isClient;
	}

	public void setClient(boolean client) {
		this.isClient = client;
	}

	public Server getServer() {
		// TODO Auto-generated method stub
		if(isServer)
		{
			if(servidorUDP == null)
			{
				servidorUDP = new Server(getListaJogadores(), multicastConnection);
			}			
			
			return servidorUDP;			
		}
		return null;
	}
	
	public Client getClient()
	{
		if(isClient)
		{
			if(clienteUDP == null)
			{
				clienteUDP = new Client(getServidorAddress());
			}
			
			return clienteUDP;
		}
		return null;
	}

	private String getServidorAddress() {
		String address = "";
		for(Jogador j : getListaJogadores())
		{
			if(j.isServer())
			{
				address = j.getServer().getAddress();
				break;
			}
		}
		return address;
	}

    

    
}
