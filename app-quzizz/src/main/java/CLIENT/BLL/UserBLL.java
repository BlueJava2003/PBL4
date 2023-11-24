package CLIENT.BLL;

import CLIENT.Client;
import CLIENT.DTO.OTPConfirmDTO;
import CLIENT.DTO.ResponseDTO;
import CLIENT.DTO.UserDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserBLL {

    static ArrayList<UserDTO> listUsers;
    public static Gson gson = new Gson();

    public UserBLL() {
    }

    public static ArrayList<UserDTO> getListUsers() {
        return listUsers;
    }

    public static void setListUsers(ArrayList<UserDTO> listUsers) {
        UserBLL.listUsers = listUsers;
    }

    public ArrayList<UserDTO> loadDSUser(int examID) throws Exception {
        Gson gson = new Gson();
        if (listUsers == null) {
            listUsers = new ArrayList<>();
        }
        ResponseDTO listData = Client.CallServer("LOAD_RESULTS", examID + "");
        Type listType = new TypeToken<ArrayList<UserDTO>>() {
        }.getType();
        // ArrayList<QuestionDTO> temp=gson.fromJson(listData, listType);
        listUsers = gson.fromJson(listData.getResult(), listType);
        return listUsers;
    }

    public ResponseDTO checkLogin(UserDTO user) throws Exception {
        String data = gson.toJson(user);
        ResponseDTO res = Client.CallServer("LOGIN", data);
        return res;

    }
    public ResponseDTO logout() throws Exception {
        ResponseDTO res = Client.CallServer("EXIT", "");
        return res;

    }

    public UserDTO loadUserLogin(int id) throws Exception {
        String data = gson.toJson(id);
        ResponseDTO res = Client.CallServer("GET_USER", data);
        UserDTO userLogin = gson.fromJson(res.getResult(), UserDTO.class);
        System.out.println(userLogin);
        return userLogin;

    }

    public String updateUser(UserDTO user) throws Exception {
        String data = gson.toJson(user);
        ResponseDTO res = Client.CallServer("UPDATE_USER", data);
        if (res.getStatus() == 200) {
            return "Cập nhật thành công !";
        } else {
            return res.getMessage();
        }
    }

    public String addUser(UserDTO user) throws Exception {
        String data = gson.toJson(user);
        ResponseDTO res = Client.CallServer("REGISTER", data);
        if (res.getStatus() == 200) {
            return "Đăng ký thành công !";
        } else {
            return res.getMessage();
        }
    }
    
    public String confirmOtp(OTPConfirmDTO otp) throws Exception{
        String data = gson.toJson(otp);
        ResponseDTO res = Client.CallServer("CONFIRMOTP", data);
        if (res.getStatus() == 200) {
            return "Xác thực thành công !";
        } else {
            return res.getMessage();
        }
    }

}
