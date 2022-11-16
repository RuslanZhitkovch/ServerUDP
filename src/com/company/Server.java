package com.company;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {
    private double sum1 = 0.0;
    private double sum2 = 0.0;
    private final DatagramSocket socket;

    private DatagramPacket packet;

    public Server() throws SocketException {
        socket = new DatagramSocket(12345);
        packet = new DatagramPacket(new byte[100], 100);
        listen();
    }

    private void listen() {
        try {
            while (true) {
                int a;
                int b;
                int c;
                socket.receive(packet);
                String str = new String(packet.getData());
                str = str.substring(0, packet.getLength());
                System.out.println("number " + str + " received as a");
                a = Integer.parseInt(str);

                socket.receive(packet);
                str = new String(packet.getData());
                str = str.substring(0, packet.getLength());
                System.out.println("number " + str + " received as b");
                b = Integer.parseInt(str);

                socket.receive(packet);
                str = new String(packet.getData());
                str = str.substring(0, packet.getLength());
                System.out.println("number " + str + " received as c");
                c = Integer.parseInt(str);

                Thread t1 = new Thread(() -> {
                    for (int i = a; i <= b; i++) {
                        sum1 += 2 * i / (i + 1);
                    }
                });
                Thread t2 = new Thread(() -> {
                    for (int i = b; i <= c; i++) {
                        sum2 += Math.pow(3, i) * i;
                    }
                });
                t1.start();
                t2.start();
                t1.join();
                t2.join();
                sendBack();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    private void sendBack() throws IOException {
        String str = String.valueOf(sum1 + sum2);
        byte[] send = str.getBytes();
        packet = new DatagramPacket(send, send.length, InetAddress.getByName("localhost"), 12346);
        socket.send(packet);
        sum1 = 0;
        sum2 = 0;
    }

}
