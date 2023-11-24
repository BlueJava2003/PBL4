/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author LE NHAN
 */
public class Server {

    private static ServerSocket server = null;
    private static Socket socket = null;
    private static Integer numberOfThread = 10;
    public static MultiThreadBus multiThreadBus;
        public static void main(String[] args) throws Exception {
        try {
            multiThreadBus = new MultiThreadBus();
            System.out.println("Server is waiting to accept user...");

            server = new ServerSocket(9999);
            System.out.println("Server started...");

            try {
                ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
                int clientNumber = 1;
                while (true) {
                    socket = server.accept();
                    System.out.println("Client " + socket.getInetAddress() + " connected...");

                    MultiThread serverThread = new MultiThread(socket, clientNumber++);
                    multiThreadBus.add(serverThread);

                    executor.execute(serverThread);
                    System.out.println("Số thread đang chạy là: "+ multiThreadBus.getLength());
                }
            }catch (IOException e) {
                System.err.println(e);
                System.out.println("Server closed connection");
                    //Đóng kết nối
                    socket.close();
                    server.close();
            }
        } catch (IOException e) { System.err.println(e); }
    }
}