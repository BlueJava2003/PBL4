/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER.DAL;

import SERVER.DAL.Mapper.UserMapper;
import SERVER.DAL.Mapper.UserWithOtpMapper;
import SERVER.DTO.BlockUserDTO;
import SERVER.DTO.UserDTO;
import SERVER.DTO.UserWithOtpDTO;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Thuan
 */
public class UserDAL extends AbstractDAL<UserDTO> {

    public UserDTO getById(int id_user) {
        UserDAL user = new UserDAL();
        String  sql = "SELECT * FROM `users` WHERE id = ?";
        List<UserDTO> temp = user.query(sql, new UserMapper(), id_user);
        UserDTO rs = temp.get(0);
        return rs;
    }

    public UserWithOtpDTO getByEmail(String email) {
        UserDAL user = new UserDAL();
        String  sql = "SELECT * FROM `users` WHERE email = ?";
        List<UserWithOtpDTO> temp = user.query(sql, new UserWithOtpMapper(), email);
        return (temp.size()>0)?temp.get(0):null;
    }

    public Integer save(UserDTO user, String otp, Timestamp createOtp) throws FileNotFoundException {
        StringBuilder sql = new StringBuilder("INSERT INTO users(email, password, name, status, gender, birthday, role,otp, otp_create_time)");
        sql.append(" VALUES(?, ?, ?, ?, ?, ?, ?,?,?)");
        return insert(sql.toString(),user.getEmail(),
                user.getPassword(), user.getName(), user.getStatus(),
                user.getGender()?1:0, user.getBirthday(), user.getRole(),otp,createOtp);
    }

    public List<UserDTO> findAll() {
        String sql = "select * from users";
        return query(sql, new UserMapper());
    }

    public void delete(int user_id) throws FileNotFoundException {
        String sql = "DELETE FROM users WHERE id = ? ";
        update(sql, user_id);
    }

    public void update(UserDTO user) throws FileNotFoundException {
        StringBuilder sql = new StringBuilder("UPDATE users SET email = ?, name = ?, status = ?, gender = ?, birthday = ?, role = ? ");
        sql.append("WHERE id = ?");
        update(sql.toString(), 
                user.getEmail(),
                user.getName(),
                user.getStatus(),
                user.getGender()?1:0,
                user.getBirthday(),
                user.getRole(),
                user.getId());
    }

    public void block(BlockUserDTO user) throws FileNotFoundException {
        StringBuilder sql = new StringBuilder("UPDATE users SET status = ? ");
        sql.append("WHERE id = ?");
        update(sql.toString(),
                user.getStatus(),
                user.getUserID());
    }

    public void active(int userId) throws FileNotFoundException {
        StringBuilder sql = new StringBuilder("UPDATE users SET status =1, otp = null, otp_create_time =null ");
        sql.append("WHERE id = ?");
        update(sql.toString(),userId);
    }
}