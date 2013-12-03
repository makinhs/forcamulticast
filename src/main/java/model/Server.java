package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.Criptografia;
import util.Parameter;
import util.Serializer;
import ctrl.MultiCastPeer;

public class Server {

    private ArrayList<Jogador> jogadores = new ArrayList<Jogador>();
    private int cont = 0, contPalavra = -1;
    private Jogador jogadorDaVez;
    private Palavra palavrasController;
    private String palavraDaVez = "";
    private MultiCastPeer mCast;
    private ArrayList<String> palavraTentada = new ArrayList<String>();
//    private ArrayList<PrivateKey> chavesPrivadas = new ArrayList<PrivateKey>();
//    private ArrayList<PublicKey> chavesPublicas = new ArrayList<PublicKey>();
    
    private HashMap<String, PublicKey> cPublicas = new HashMap<String, PublicKey>();
    private HashMap<String, PrivateKey> cPrivadas = new HashMap<String, PrivateKey>();
    
    private boolean loopGetPrivateKey = true;    
    private int contLoopStillAlive = 0;

    public Server(List<Jogador> jogadores, MultiCastPeer mCast) {
        // TODO Auto-generated constructor stub
        this.jogadores.addAll(jogadores);
        this.mCast = mCast;
        palavrasController = new Palavra();
    }

    public boolean isLoopGetPrivateKey() {
        return loopGetPrivateKey;
    }

    public void addContLoop() {
        contLoopStillAlive += 88;
    }
 

    public void addJogador(Jogador jogador) {
        jogadores.add(jogador);
    }

    public void addJogadores(List<Jogador> jogadores) {
        jogadores.addAll(jogadores);
    }

    public Jogador getJogadorDaVez() {
        Jogador jogadorDaVez = jogadores.get(cont);
        cont++;
        if (cont == jogadores.size()) {
            cont = 0;
        }

        return jogadorDaVez;
    }

//    public ArrayList<PrivateKey> getChavesPrivadas() {
//    	return chavesPrivadas;
//    }

    private void mandarMensagemHello() {
        // TODO Auto-generated method stub
        addContLoop();
        String hello = "hello";
        if (contLoopStillAlive >= Parameter.DELTA_T1_SERVER_MANDAR_HELLO) {
            mCast.enviarMensagem(hello.getBytes());
            contLoopStillAlive = 0;
        }

    }

  

    public void startJogo() {
        DatagramSocket aSocket = null;
        ChaveLetraJogadorController chaveJogador = null;
        boolean isJogoLoop = true;
        try {

            

        	palavraDaVez = getProximaPalavra();
            while (isJogoLoop) {
            	setDadosForca();
                mandarMensagemHello();
                
                aSocket = new DatagramSocket(6789);
                // create socket at agreed port
                boolean msgRecebida = false;
                int contJogadorVez = 0;
                while(!msgRecebida)
                {
                	avisaVezDoJogador();
                    System.out.println("Vez do jogador: " + jogadorDaVez.getNick());
                	byte[] buffer = new byte[128];
                	DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                	aSocket.setSoTimeout(15000);
                	aSocket.receive(request);
                	
                	
                	int contadorChave = 0;
                	
                	byte[] response = Criptografia.decriptarComChavePrivada(request.getData(), cPrivadas.get(jogadorDaVez.getNick()));
                	String resposta = new String(response).trim();
                	

                	if(resposta == null)
                	{                		
                		
                		System.out.println("Não é o jogador da vez!");
                		contJogadorVez++;
                		
                		if(contJogadorVez > 2)
                		{
                			msgRecebida = true;
                			for(Jogador j: getJogadores())
                			{
                				if(j.getId() == jogadorDaVez.getId())
                				{
                					getJogadores().remove(j);
                					break;
                				}
                			}
                		}
                		
                	}
                	else
                	{
                		if(resposta.equals(""))
                		{
                			System.out.println("Jogador passou a vez");
                		}
                		else
                		{
                			String [] respos = resposta.split(" ");
                			resposta = respos[0].trim();
                			if(resposta.length()>1)
                			{
                				resposta.equalsIgnoreCase(palavraDaVez);
                				if(resposta.equals(palavraDaVez))
                				{
                					System.out.println("Palavra acertada!: " + palavraDaVez);
                					String fim = "Acertou palavra! UAU";
                					mCast.enviarMensagem(fim.getBytes());
                					palavraDaVez = getProximaPalavra();
                					palavraTentada.clear();
                				}
                				else
                				{
                					System.out.println("Errou... continuemos");
                				}
                			}
                			else
                			{
                				//testa letra
                				if(testaLetra(resposta))
                				{                					
                					String fim = "Acertou palavra! UAU";
                					mCast.enviarMensagem(fim.getBytes());
                					palavraDaVez = getProximaPalavra();
                					palavraTentada.clear();
                				}
                			}
                			
                			
                		}
                		String teste = new String(resposta);
                		System.out.println(teste);
                		msgRecebida = true;                		
                	}
                }

            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null) {
                aSocket.close();
            }
        }
    }

    private boolean testaLetra(String resposta) {
		// TODO Auto-generated method stub
    	if(!palavraTentada.contains(resposta))
    	{
    		palavraTentada.add(resposta);
    	}
    	
    	String [] teste = new String[palavraDaVez.length()];
    	
    	for(int i=0; i<teste.length; i++)
    	{
    		teste[i] = "";
    	}
    	
    	for(int i=0; i<teste.length;i++)
    	{
    		for(String s : palavraTentada)
    		{
    			if(s.equals(palavraDaVez.substring(i, i+1)))
    			{
    				teste[i] = s;
    				break;
    			}
    		}
    	}
    	
    	for(int i=0; i<teste.length; i++)
    	{
    		if(teste[i].equals(""))
    		{
    			return false;
    		}
    	}
    	
    	return true;
    	
		
	}

	private void avisaVezDoJogador() {
		// TODO Auto-generated method stub
    	String msg = "Jogador da vez: " + jogadorDaVez.getNick();
    	for(int i=0;i<1;i++){
    		mCast.enviarMensagem(msg.getBytes());
    	}
	}

	private void setDadosForca() {
        jogadorDaVez = getJogadorDaVez();
//		palavraDaVez = getProximaPalavra();

    }

    private DatagramPacket controleJogo(ChaveLetraJogadorController chaveJogador) {

        return null;

    }

    private void limparDados() {
        // TODO Auto-generated method stub
        palavraDaVez = getProximaPalavra();
        palavraTentada = new ArrayList<String>();
    }

    private String getProximaPalavra() {
        contPalavra++;
        return palavrasController.getPalavras().get(contPalavra);

    }

    public String getAddress() {
        // TODO Auto-generated method stub
        return Parameter.HOST_ADDRESS;
    }

//	public void setChavesPublicas(ArrayList<PublicKey> chavesPublicas) {
//		// TODO Auto-generated method stub
//		this.chavesPublicas = chavesPublicas;
//	}

//	public void setChavesPrivadas(ArrayList<PrivateKey> chavesPrivadas) {
//		// TODO Auto-generated method stub
//		this.chavesPrivadas = chavesPrivadas;
//	}

//	public ArrayList<PublicKey> getChavesPublicas() {
//		return chavesPublicas;
//	}

	public ArrayList<Jogador> getJogadores() {
		return jogadores;
	}

	public void addChavePublica(String nick, PublicKey publicKey, PrivateKey privateKey) {
		// TODO Auto-generated method stub
		
		if(!cPublicas.containsKey(nick))
		{
			cPublicas.put(nick, publicKey);
		}
		
		if(!cPrivadas.containsKey(nick))
		{
			cPrivadas.put(nick, privateKey);
		}
		
	}
	
	
	
	
	
}
