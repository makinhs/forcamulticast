package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.Criptografia;
import util.Serializer;


/**
 * Classe responsável para rodar o Cliente do jogo 
 * 
 *
 */
public class Client {

    private int port = 6789;
    private String ipAddress;
    private DatagramSocket socket;
    private InetAddress host;
    private Jogador jogador;
    private boolean chaveEnviada = false;

    /**
     * Construtora do Cliente.
     * @param ipAddress Recebe o endereço em String para o cliente se conectar a um server UDP
     * @param jogador Recebe o jogador.
     */
    public Client(String ipAddress, Jogador jogador) {
        try {
            this.ipAddress = ipAddress;
            socket = new DatagramSocket();
            host = InetAddress.getByName(ipAddress);
            this.jogador = jogador;
        } catch (SocketException ex) {
            System.out.println("Erro Socket: " + ex.getLocalizedMessage());
        } catch (UnknownHostException ex) {
            System.out.println("Erro Host: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Método que envia via UDP uma tentativa de acerto para a Forca
     * @param msg
     */
    public void enviarChute(String msg) {
        try {
            byte[] msgByte = Criptografia.encriptarComChavePublica(msg.getBytes(), jogador.getChavePublica());
//            byte[] msgByte = msg.getBytes();
            
            DatagramPacket request = new DatagramPacket(msgByte, msgByte.length, host, port);
            socket.send(request);

        } catch (IOException ex) {
            System.out.println("Erro Envio: " + ex.getLocalizedMessage());
        }
    }
    
}
