package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.PrivateKey;
import java.security.PublicKey;

import util.Serializer;

/**
 * Classe responsável para criar um server para receber as chaves publicas utilizadas pelo Cliente.
 * 
 *
 */
public class ServerRecebedorChave {

    private int porta = 0;
    private Jogador jogador;

    /**
     * Construtora que recebe a porta do jogador e o jogador
     * @param porta um int representando a porta do jogador
     * @param j o próprio jogador em questão
     */
    public ServerRecebedorChave(int porta, Jogador j) {
        this.porta = porta;
        this.jogador = j;
    }

    /**
     * Método que executa o server UDP e fica esperando receber a chave publica do servidor para o cliente.
     */
    public void run() {
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket(porta);
            boolean laco = true;
            // create socket at agreed port
            byte[] buffer = new byte[2048];
            while (laco) {
                System.out.println("Server RODANDO nick: " + jogador.getNick());
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);

                Object o = null;
                try {
                    o = Serializer.deserialize(request.getData());
                    if (o instanceof PublicKey) {
                        PublicKey publicKey = ((PublicKey) o);

                        jogador.setChavePublica(publicKey);
                        System.out.println("Chave recebida nick: " + jogador.getNick());
                        laco = false;

                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null)
                aSocket.close();
        }
    }

}
