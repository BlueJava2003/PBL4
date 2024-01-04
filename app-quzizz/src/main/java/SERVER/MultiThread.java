/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER;

/**
 *
 * @author
 */

import CLIENT.DTO.MyExamResult;
import CLIENT.DTO.StatisticalDetailDTO;
import SERVER.BLL.ExamBLL;
import SERVER.BLL.QuestionBLL;
import SERVER.BLL.ResultBLL;
import SERVER.BLL.UserBLL;
import SERVER.DTO.*;
import SERVER.HELPER.Hybrid_Encryption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MultiThread implements Runnable{
    Gson gson = new Gson();
    public static Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    public int clientID;
    public PrivateKey privateKey;
    public PublicKey publicKey;
    public String clientKey;
    public String valueData;

    public String[] parts;
    public String object;
    private static HashMap<String, String> readClient = new HashMap<String, String>();
    public static ExamBLL examBLL = new ExamBLL();
    public static ResultBLL resultBLL = new ResultBLL();
    public static UserBLL userBLL = new UserBLL();
    public static QuestionBLL questionBLL = new QuestionBLL();
    private static final String RSA_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJs+d1sCnbmSgKE1pc71d9kcsERUpZ50blzYieKjrnsB48FpjNghJza5xfAcPOf7EmwVu2EbPK34JiydV2u3ClUHc+/tOlaePZbaXTIbm+s6OxQRFUFoL7oyTX5E1dGKarEIkO0lO1d4H39tkZcF7hZw9dSHAgHnrck/IAZPJ5WfAgMBAAECgYEAkySI8m0vW+W9H49+wgOtfc6QT6O/esm2lS/0uSkVRqfK3NaTVYNO7LL2JphNLj+t/V43xVmQkQAkBqN3abQLCIR961M4eaBwpLAOQtJKALH+fnsiUCCWwbioO3PTfyOpH3injfLvE4NhyoQeazx+AKSkZyro2CG5U/LBsJJWXzECQQDQ6bYILy2WUuqWKwIbGSwDZPdc4T724PFECzdZki1O1gw6PPhdoasUOt0OZrT0rqJ71YF0MdAeykHMYn2PEWtHAkEAvjwOEIgqtFpe6nGNZDiZ+5i/sV5bxW5o/YQWwf106nxR0CQlqfwevrIJvMDUUKs7QTAeMT+pcWnK7eW3DoB+6QJBAICGeA3a8IHd6yKNvRLszo4cDK6giLsbsnK5L8k0TBmHSCiAIBCCiJy+hgb5GvS5h48F0Emq5645ondaVIKzJbsCQQCx4LXF/4zu1xGpZkQvUj2pZEraLsDg+zxw0PH2smiAWX6mgSY2q+iTpyYzuJrOU040xil1I3Hs+l8l04Y3qS8BAkBIHOR887VNtejYOVcwrUHpcKcccVPAKxsoxBBziOxD0alGHtvop7CU1VVfcnQtZ7Dd1sSj4MNgguW92s0/rXNg";
    private static final String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbPndbAp25koChNaXO9XfZHLBEVKWedG5c2Inio657AePBaYzYISc2ucXwHDzn+xJsFbthGzyt+CYsnVdrtwpVB3Pv7TpWnj2W2l0yG5vrOjsUERVBaC+6Mk1+RNXRimqxCJDtJTtXeB9/bZGXBe4WcPXUhwIB563JPyAGTyeVnwIDAQAB";


    public MultiThread(Socket socket, int clientID) throws IOException {
        MultiThread.socket = socket;
        this.clientID = clientID;

        this.privateKey = Hybrid_Encryption.getPrivateKeyRSA(RSA_PRIVATE_KEY);
        this.publicKey = Hybrid_Encryption.getPublicKeyRSA(RSA_PUBLIC_KEY);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        System.out.println("Server thread number " + clientID + " Started");
    }

    public Socket getSocket() {
        return socket;
    }

    public int getClientID() {
        return this.clientID;
    }

    public int GetThreadNumber() {
        return this.clientID;
    }

    @Override
    public void run() {
        while(true) {
            try {
                String line = in.readLine();
                readClient =  gson.fromJson(line, new TypeToken<HashMap<String, String>>() {}.getType());

                String encryptKey = readClient.get("key");
                valueData = readClient.get("value");

                // truong hop client chua có khoa gửi khóa client
                if (Objects.equals(encryptKey, "")) {
                    sendPublicKeyRSA();
                    continue;
                }
                // giải mã data từ client
                decryptMessage(encryptKey);

                assert valueData != null;
                String[] data = valueData.split(";");
                String feature = data[0];
                if (data.length == 2){
                    object = data[1];
                    if(object.contains(",")){
                        parts = object.split(",");
                    }
                    System.out.println(data[0]+"--"+data[1]);
                }
//                System.out.println("PARTTTTTTTTTTTTTTTTTTTTT");
//                if(parts.length > 1){
//                    System.out.println(parts[0]);
//                    System.out.println(parts[1]);
//                }
                switch (feature){
                    case "LOAD_EXAM":
                        ResponseDTO list = examBLL.getListExam();
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(list), clientKey);
                        break;
                    case "LOAD_USER_EXAM":
                        ResponseDTO listExam = examBLL.getExamByUserID(Integer.parseInt(object));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(listExam), clientKey);
                        break;
                    case "ADD_EXAM":
                        Type listExamType = new TypeToken<ExamDTO>(){}.getType();
                        ResponseDTO examDTO = examBLL.addExamDTO(gson.fromJson(object, listExamType));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(examDTO), clientKey);
                        break;
                    case "UPDATE_EXAM":
                        ResponseDTO update = examBLL.updateExam(gson.fromJson(object, FullDataExamDTO.class));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(update), clientKey);
                        break;
                    case "DELETE_EXAM":
                        ResponseDTO deleteSuccess = examBLL.deleteExam(Integer.parseInt(object));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(deleteSuccess), clientKey);
                        break;
                    case "CHECK_EXAM":
                        ResponseDTO isExam = resultBLL.getByExamId(Integer.parseInt(object));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(isExam), clientKey);
                        break;
                    case "LOAD_QUESTIONS":
                        ResponseDTO listQ= questionBLL.findQuestionsByExamID(Integer.parseInt(object));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(listQ), clientKey);
                        break;
                    case "ADD_QUESTIONS":
                        Type listQuestionType = new TypeToken<List<QuestionDTO>>(){}.getType();
                        ResponseDTO addQuestion = questionBLL.createListQuestion(gson.fromJson(object, listQuestionType));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(addQuestion), clientKey);
                        break;
                    case "UPDATE_QUESTION":
                        ResponseDTO updateQuestion = questionBLL.updateQuestion(gson.fromJson(object, QuestionDTO.class));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(updateQuestion), clientKey);
                        break;
                    case "ADD_RESULT":
                        ResponseDTO resultDTO = resultBLL.addResult(gson.fromJson(object, ResultDTO.class));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(resultDTO), clientKey);
                        break;
                    case "LOAD_RESULTS":
                        ResponseDTO listResultDTO = resultBLL.getByExamId(Integer.parseInt(object));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(listResultDTO), clientKey);
                        break;
                    case "LOAD_RESULTS_BY_USER":
                        ResponseDTO listResultDTOO = resultBLL.getByExamIdAndUserId(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(listResultDTOO), clientKey);
                        break;
                    case "GET_USER":
                        ResponseDTO user = userBLL.getByID(Integer.parseInt(object));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(user), clientKey);
                        break;
                    case "UPDATE_USER":
                        ResponseDTO updateUser = userBLL.update(gson.fromJson(object, UserDTO.class));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(updateUser), clientKey);
                        break;
                    case "STATISTICAL":
                        ResponseDTO statisticalDTO = resultBLL.statistical(Integer.parseInt(object));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(statisticalDTO), clientKey);
                        break;
                    case "STATISTICAL_DETAIL":
                        ResponseDTO StatisticalDetailDTO = resultBLL.statisticalDetail(Integer.parseInt(object));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(StatisticalDetailDTO), clientKey);
                        break;
                    case "LOGIN":
                        ResponseDTO userDTO = userBLL.login(gson.fromJson(object, LoginDTO.class));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(userDTO), clientKey);
                        break;
                    case "REGISTER":
                        ResponseDTO registerSuccess = userBLL.register(gson.fromJson(object, UserDTO.class));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(registerSuccess), clientKey);
                        break;
                    case "CONFIRMOTP":
                        ResponseDTO confirmSuccess = userBLL.confirmOTP(gson.fromJson(object, OTPConfirmDTO.class));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(confirmSuccess), clientKey);
                        break;
                    case "ADD_EXAM_RESULT":
                        Type listExamResultType = new TypeToken<MyExamResult>(){}.getType();
                        ResponseDTO examResultDTO = examBLL.addExamResultDTO(gson.fromJson(object, listExamResultType));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(examResultDTO), clientKey);
                        break;
                    case "LOAD_EXAM_RESULT":
                        ResponseDTO listExamResult = examBLL.getExamResultByExamIdAndUserID(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(listExamResult), clientKey);
                        break;    
                    case "DELETE_EXAM_RESULT":
                        ResponseDTO deleteExamResult = examBLL.deleteExamResult(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(deleteExamResult), clientKey);
                        break;    
//          =============================================== ADMIN UI ========================================================
                        //Thống kê server
                    case "HELP":
                        String help = "============= [List commands] =============\n"
                                + "============= THỐNG KÊ =============\n"
                                + "STATISTICAL_SERVER:        Thống kê\n"
                                + "ONLINE_USER:               Số lượng người dùng online hiện tại\n"
                                + "GET_USER;<user-id>:        Thông tin người dùng\n"
                                + "============= BLOCK NGƯỜI DÙNG =============\n"
                                + "BLOCK_USER_LOGIN;<user-id>:          block người dùng không cho đăng nhập\n"
                                + "BLOCK_USER_CREATE_EXAM;<user-id>:    block người dùng tạo đề thi\n"
                                + "BLOCK_USER_TAKE_EXAM;<user-id>:      block người dùng thi\n"
                                + "=======================================";
                        Server.multiThreadBus.writeSocket(getClientID(), help, clientKey);
                        break;
                    case "STATISTICAL_SERVER":
                        ResponseDTO statisticalServerDTO = resultBLL.statisticalServer(Integer.parseInt(object));
                        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(statisticalServerDTO), clientKey);
                        break;
                    case "BLOCK_USER_LOGIN":
                        blockUserController(4);
                        break;
                    case "BLOCK_USER_CREATE_EXAM":
                        blockUserController(3);
                        break;
                    case "BLOCK_USER_TAKE_EXAM":
                        blockUserController(2);
                        break;
                    case "ONLINE_USER":
                        int clientOnlineSize = Server.multiThreadBus.getLength();
                        String temp = "Số lượng người online : " + clientOnlineSize + " người";
                        Server.multiThreadBus.writeSocket(getClientID(), temp, clientKey);
                    case "EXIT":
                        Server.multiThreadBus.removeSocket(this.clientID);
                        socket.close();
                        in.close();
                        out.close();
                        break;
                }
            }catch (IOException ex) {
                System.out.println("VAOIO");
                ex.printStackTrace();
            } catch (Exception e) {
                System.out.println("VAO");
               e.printStackTrace();
            }
        }
    }

    public void writeSocket(String json) throws IOException{
        out.write(json);
        out.newLine();
        out.flush();
    }

    private void sendPublicKeyRSA () throws IOException {
        ObjectOutputStream obOut = new ObjectOutputStream(socket.getOutputStream());
        obOut.writeObject(publicKey);
        obOut.flush();
    }

    private void decryptMessage(String encryptKey) throws Exception {
        //Giải mã public key mã hóa dựa trên private key
        clientKey = Hybrid_Encryption.decryptRSA(encryptKey, privateKey);
        //Giải mã dữ liệu dựa trên public key đã giải mã
        valueData = Hybrid_Encryption.decryptAES(valueData, clientKey);
    }

    private void blockUserController(int status) throws IOException {
        BlockUserDTO blockUserDTO = new BlockUserDTO(Integer.parseInt(object),status);
        ResponseDTO blockUser = userBLL.block(blockUserDTO);
        Server.multiThreadBus.writeSocket(getClientID(), gson.toJson(blockUser), clientKey);
    }
}