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

public class Client {

    private int port = 6789;
    private String ipAddress;
    private DatagramSocket socket;
    private InetAddress host;
    private Jogador jogador;
    private boolean chaveEnviada = false;

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

    public void enviarChute(String msg) {
        try {
//            byte[] msgByte = Criptografia.encriptarComChavePrivada(Serializer.serialize(msg), jogador.getChavePrivada());
            byte[] msgByte = Criptografia.encriptarComChavePublica(Serializer.serialize(msg), jogador.getChavePublica());
//            byte[] msgByte = msg.getBytes();
            
            msgByte = msg.getBytes();
            
            DatagramPacket request = new DatagramPacket(msgByte, msgByte.length, host, port);
            socket.send(request);          
//            Object o = Serializer.deserialize(Criptografia.decriptarComChavePublica(request.getData(), jogador.getChavePublica()));
//            if(o instanceof String) {
//                System.out.println(((String) o));
//            }
        } catch (IOException ex) {
            System.out.println("Erro Envio: " + ex.getLocalizedMessage());}
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void enviarChavePrivada() {
        try {
            DatagramPacket request = new DatagramPacket(jogador.getPrivateKey(), jogador.getPrivateKey().length, host, port);
            socket.send(request);
            Random r = new Random();
            Thread.sleep(r.nextInt(200));
        } catch (Exception e) {
            System.out.println("Erro Envio chave privada: " + e.getLocalizedMessage());
        }
    }
}
