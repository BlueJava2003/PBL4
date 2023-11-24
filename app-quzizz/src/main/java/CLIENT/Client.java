/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CLIENT;

import CLIENT.BLL.UserBLL;
import CLIENT.DTO.ResponseDTO;
import CLIENT.HELPER.Hybrid_Encryption;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.HashMap;

/**
 *
 * @author LE NHAN
 */
public class Client {
    private static Socket socket = null;
    private static BufferedReader in = null;
    private static BufferedWriter out = null;
    private static String ip="localhost";
    private static int port=9999;
    private static Gson gson=new Gson();
//    private final static String publicKey_RSA = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbPndbAp25koChNaXO9XfZHLBEVKWedG5c2Inio657AePBaYzYISc2ucXwHDzn+xJsFbthGzyt+CYsnVdrtwpVB3Pv7TpWnj2W2l0yG5vrOjsUERVBaC+6Mk1+RNXRimqxCJDtJTtXeB9/bZGXBe4WcPXUhwIB563JPyAGTyeVnwIDAQAB";

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        Client.ip = ip;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        Client.port = port;
    }


    static PublicKey publicKey;
    static String randomKey;

    public Client() throws NoSuchAlgorithmException {
    }

    public static CLIENT.DTO.ResponseDTO CallServer(String key, String data) throws Exception {
        String json = key+";"+data;
        
        if(key.equals("bye"))
            return new ResponseDTO();

        //Mã hóa message
        String request = Hybrid_Encryption.encryptAES(json, randomKey);
        //Ma hoa randomkey
        String encryptRandomKey = Hybrid_Encryption.encryptRSA(randomKey, publicKey);

        HashMap<String, String> readClient = new HashMap<>();

        readClient.put("key", encryptRandomKey);
        readClient.put("value", request);
        // Client gửi request đến data
        String hashmapJson = gson.toJson(readClient);
        sendData(hashmapJson);

        // Client nhận phản hồi từ server
        try{
            String response = in.readLine();
            System.out.println("Client received: " + response);
            String res = Hybrid_Encryption.decryptAES(response, randomKey);
            ResponseDTO responseDTO = gson.fromJson(res, ResponseDTO.class);
            System.out.println("Client received: " + responseDTO);
            return responseDTO;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void sendData(String json) throws IOException {
        out.write(json);
        out.newLine();
        out.flush();
    }

    public BufferedReader getReader() {
        return in;
    }
    
    public static void NewClient() throws IOException, NoSuchAlgorithmException {
        socket = new Socket(ip, port);
        randomKey = Hybrid_Encryption.getRandomAESKey();
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        HashMap<String, String> readClient = new HashMap<>();
        readClient.put("key", "");
        readClient.put("value", "");

        // Client gửi request đến data
        String hashmapJson = gson.toJson(readClient);

        sendData(hashmapJson);
        ObjectInputStream obIn = new ObjectInputStream(socket.getInputStream());

        try{
            Object obj = obIn.readObject();
            // lâ public key từ client (lâ đầu connect)
            publicKey = (PublicKey) obj;
        }catch (ClassNotFoundException e){
            System.out.println("exepction: "+ e);
        }
        System.out.println("");
    }
    public static void OutClient() throws IOException{
       // socket = new Socket("localhost", 5999);
        UserBLL userBLL= new UserBLL();
        try{
            userBLL.logout();
        }
        catch (Exception e){

        }
        in.close();
        out.close();
        socket.close();
    }

}