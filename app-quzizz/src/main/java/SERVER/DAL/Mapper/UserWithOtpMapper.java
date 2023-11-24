/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER.DAL.Mapper;

import SERVER.DTO.UserWithOtpDTO;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author LE NHAN
 */
public class UserWithOtpMapper implements RowMapper<UserWithOtpDTO>{
    @Override
    public UserWithOtpDTO mapRow(ResultSet rs) {
        try {
            UserWithOtpDTO u = new UserWithOtpDTO();
            u.setId(rs.getInt("id"));
            u.setEmail(rs.getString("email"));
            u.setPassword(rs.getString("password"));
            u.setName(rs.getString("name"));
            u.setRole(rs.getString("role"));
            u.setStatus(rs.getInt("status"));
            u.setGender(rs.getBoolean("gender"));
            u.setBirthday(rs.getTimestamp("birthday"));
            u.setOtp(rs.getString("otp"));
            u.setOtp_create_time(rs.getTimestamp("otp_create_time"));
            return u;
        } catch (SQLException e) {
            return null;
        }
    }
}
