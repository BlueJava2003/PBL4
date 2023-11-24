package CLIENT;

import CLIENT.DTO.*;
import SERVER.HELPER.Hybrid_Encryption;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.PublicKey;
import java.util.HashMap;

public class Client_console {
    private static Socket socket = null;
    private static BufferedReader in = null;
    private static BufferedWriter out = null;
    private static BufferedReader stdIn = null;
    private static final Gson gson = new Gson();
    private static String publicKey_RSA = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbPndbAp25koChNaXO9XfZHLBEVKWedG5c2Inio657AePBaYzYISc2ucXwHDzn+xJsFbthGzyt+CYsnVdrtwpVB3Pv7TpWnj2W2l0yG5vrOjsUERVBaC+6Mk1+RNXRimqxCJDtJTtXeB9/bZGXBe4WcPXUhwIB563JPyAGTyeVnwIDAQAB";
    private static String randomKey;

    public static void main(String[] args) throws IOException {
        try {
            socket = new Socket("localhost", 8888);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            randomKey = Hybrid_Encryption.getRandomAESKey();

            PublicKey publicKey = Hybrid_Encryption.getPublicKeyRSA(publicKey_RSA);
            while(true) {
                // Client nhận dữ liệu từ keyboard và gửi vào stream -> server
                System.out.print("Client input: ");
                String line = stdIn.readLine();

                //Mã hóa message
                String data = Hybrid_Encryption.encryptAES(line, randomKey);
                //Ma hoa randomkey
                String encryptRandomKey = Hybrid_Encryption.encryptRSA(randomKey, publicKey);

                HashMap<String, String> readClient = new HashMap<>();

                readClient.put("key", encryptRandomKey);
                readClient.put("value", data);

                String hashmapJson = gson.toJson(readClient);

                out.write(hashmapJson);
                out.newLine();
                out.flush();

                // Client nhận phản hồi từ server
                String res = in.readLine();
                String response = Hybrid_Encryption.decryptAES(res, randomKey);
                logResponse(line, response);
            }

        } catch (IOException e) {
            System.out.println("Client closed connection");
            in.close();
            out.close();
            stdIn.close();
            socket.close();
            System.err.println(e);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void logResponse(String req, String res){
        String[] data = req.split(";");
        String feature = data[0];
        switch (feature){
            case "STATISTICAL_SERVER":
                ResponseDTO responseStatistical= gson.fromJson(res, ResponseDTO.class);
                StatisticalDTO statisticalDTO = gson.fromJson(responseStatistical.getResult(), StatisticalDTO.class);
                System.out.println(statisticalDTO.toString());
                break;
            case "GET_USER":
                ResponseDTO response= gson.fromJson(res, ResponseDTO.class);
                UserDTO userDTO= gson.fromJson(response.getResult(), UserDTO.class);
                System.out.println(userDTO.toString());
                break;
            case "ONLINE_USER":
            case "HELP":
                System.out.println(res);
                break;
            case "BLOCK_USER_LOGIN":
            case "BLOCK_USER_CREATE_EXAM":
            case "BLOCK_USER_TAKE_EXAM":
                ResponseDTO responseDTO = gson.fromJson(res, ResponseDTO.class);
                System.out.println(responseDTO.getMessage());
                break;

        }
    }
}
