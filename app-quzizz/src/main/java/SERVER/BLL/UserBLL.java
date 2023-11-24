package SERVER.BLL;

import SERVER.DAL.UserDAL;
import SERVER.DTO.*;
import SERVER.HELPER.BCryptHelper;
import SERVER.HELPER.SendEmail;
import com.google.gson.Gson;

import javax.mail.Message;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Date;

public class UserBLL {
    private static UserDAL userDAL = new UserDAL();
    private static Gson gson = new Gson();

    public ResponseDTO login(LoginDTO loginDTO) {
        UserWithOtpDTO userDTO = userDAL.getByEmail(loginDTO.getEmail());
        if (userDTO == null) {
            return new ResponseDTO(400, "", "Email không tồn lại");
        }
        else if(userDTO.getStatus()==4)  return new ResponseDTO(400, "", "Tài khoản đã bị khóa !");

        if (!BCryptHelper.check(loginDTO.getPassword(), userDTO.getPassword())){
            return new ResponseDTO(400, "", "Sai mật khẩu");
        }

        return new ResponseDTO(200, gson.toJson(userDTO), "Đăng nhập thành công");
    }

    public ResponseDTO register (UserDTO userDTO) throws FileNotFoundException, Exception {
        System.out.println("SERVER.BLL.UserBLL.register()1");
        UserWithOtpDTO user = userDAL.getByEmail(userDTO.getEmail());
        if (user != null) {
            return new ResponseDTO(400, "", "Email đã tồn tại");
        }
        
        String password = BCryptHelper.encode(userDTO.getPassword());

        Message ms = SendEmail.sendMail(userDTO.getEmail());
        String otp = SendEmail.getTextFromMessage(ms);

        Date date = new Date();
        Timestamp t = new Timestamp(date.getTime());

        userDTO.setPassword(password);
        userDAL.save(userDTO, otp, t);

        return new ResponseDTO(200, "", "Đăng ký thành công!!!");
    }

    public ResponseDTO getByID(int id) {
        UserDTO userDTO = userDAL.getById(id);
        if (userDTO == null) {
            return new ResponseDTO(400, "", "Invalid Param!!!");
        }

        return new ResponseDTO(200, gson.toJson(userDTO), "");
    }

    public ResponseDTO update(UserDTO userDTO) throws FileNotFoundException {
        try{
            userDAL.update(userDTO);
            return new ResponseDTO(200, "", "Update successfully!!!");
        }
        catch (Exception e){
            return new ResponseDTO(400, "", "Update unsuccessfully!!!");
        }
    }

    public ResponseDTO confirmOTP(OTPConfirmDTO otp) throws FileNotFoundException{
        UserWithOtpDTO user = userDAL.getByEmail(otp.getEmail());
        
        Timestamp time = new Timestamp(new Date().getTime()-600000);
        if(!user.getOtp().equals(otp.getOtp())){
            return new ResponseDTO(400, "", "OTP không đúng!!!");
        }

        if(time.after(user.getOtp_create_time())){
            return new ResponseDTO(400, "", "OTP hết hạn!!!");
        }

        userDAL.active(user.getId());
        return new ResponseDTO(200, "", "Xác thực thành công!!!");
    }

    public ResponseDTO block(BlockUserDTO blockUserDTO) throws FileNotFoundException {
        try{
            UserDTO userDTO = userDAL.getById(blockUserDTO.getUserID());
            if (userDTO.getStatus() == 4){
                return new ResponseDTO(400, "", "Blocked unsuccessfully!!!");
            }

            if (userDTO.getStatus() == blockUserDTO.getStatus()){
                return new ResponseDTO(200, "", "Blocked successfully!!!");
            }

            userDAL.block(blockUserDTO);
            return new ResponseDTO(200, "", "Blocked successfully!!!");
        }
        catch (Exception e){
            return new ResponseDTO(400, "", "Blocked unsuccessfully!!!");
        }
    }
}
