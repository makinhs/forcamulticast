package ctrl;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import model.Client;
import model.Jogador;
import util.Serializer;

public class MultiCastPeer extends Thread implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String HOST = "229.0.0.50";
    private final int PORT = 5050;
    private final int TIMEOUT = 20000;
    private String usuario;
    private MulticastSocket socket;
    private InetAddress group;
    private Jogador jogador;
    
    int batata = 10;

    public MultiCastPeer(Jogador jogador) {
        try {
            this.jogador = jogador;
            group = InetAddress.getByName(HOST);
            socket = new MulticastSocket(PORT);
            socket.setSoTimeout(TIMEOUT);
            socket.joinGroup(group);
            this.start();
        } catch (UnknownHostException e) {
            System.out.println("Erro no host: " + e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println("Erro I/O: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void run() {  
        while (!socket.isClosed()) {
        	
        	
            if (this.jogador.getListaJogadores().size() < 4) {
                this.adicionarJogadores();                
            }
            else
            {
            	if(!jogador.isClient() && !jogador.isServer())
            	{
            		jogador.eleicao();
            	}
            	
            	if(jogador.isServer())
            	{
            		inicializarServidorUDP();
            	}
            	
            	if(jogador.isClient())
            	{
            		inicializarClienteUDP();
            	}
            }
        }
    }

    private void inicializarClienteUDP() {
		// TODO Auto-generated method stub    	
    	
    	//loop do jogo
    	Client c = jogador.getClient();
    	

    	for(Jogador j : jogador.getListaJogadores())
    	{
    		if(j.getServer() == null)
    		{
    			
    		}
    		else
    		{
    			if(j.getServer().isLoopGetPrivateKey())
    			{
    				c.enviarChavePrivada();
    			}
    		}
    	}
   			    	

    	
    	c.enviarChute(jogador.getNick() + "diz: teuCU");
    	try {
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void inicializarServidorUDP() {
		// TODO Auto-generated method stub
		
		try {
			sleep(3000);
			//começa o server udp
			if(jogador.getServer().isLoopGetPrivateKey())
			{
				jogador.getServer().getChavesPrivadas();
			}
			if(jogador.getServer().isLoopMainGame())
			{
				jogador.getServer().startJogo();
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

	private void mandaDadosServerMulticast() {
		// TODO Auto-generated method stub
		byte[] buffer = new byte[1024];
		try
		{
			this.enviarMensagem(jogador.sendInfo());
			sleep(1000);
		}catch(Exception e)
		{
			
		}finally
		{
			cleanBuffer(buffer);
		}
		
		
	}

	/**
     * Envia uma mensagem para o grupo multicast
     *
     * @param String msg
     */
    public void enviarMensagem(byte[] msg) {
        DatagramPacket msgOut = new DatagramPacket(msg, msg.length, group, PORT);
        try {
            socket.send(msgOut);
        } catch (IOException e) {
            System.out.println("Erro I/O: " + e.getLocalizedMessage());
        }
    }

    /**
     * Método responsável por limpar o buffer de dados
     *
     * @param byte [] buffer
     */
    private void cleanBuffer(byte[] buffer) {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = 0;
        }
    }

    private void adicionarJogadores() {
        byte[] buffer = new byte[1024];
        DatagramPacket msgIn = new DatagramPacket(buffer, buffer.length, group, PORT);
        try {
            this.enviarMensagem(jogador.sendInfo());
            socket.receive(msgIn);
            Object o = Serializer.deserialize(msgIn.getData());
            if (o instanceof Jogador) {
                jogador.addJogador((Jogador) o);                
            }
            sleep(1250);
            // enviarMensagem("Recebido por " + usuario);
        } catch (IOException e) {
            System.out.println("Erro I/O: " + e.getLocalizedMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro Serializacao: " + ex.getLocalizedMessage());
        } catch (InterruptedException ex) {
            System.out.println("Erro sleep: " + ex.getLocalizedMessage());
        } finally {
            cleanBuffer(buffer);
        }
    }
}