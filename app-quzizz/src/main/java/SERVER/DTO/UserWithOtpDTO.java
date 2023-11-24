/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER.DTO;

import java.sql.Timestamp;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author LE NHAN
 */

@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor
public class UserWithOtpDTO {
    private Integer id;
    private String email;
    private String password;
    private String name;
    private Integer status;
    private Boolean gender;
    private Date birthday;
    private String role;
    private String otp;
    private Timestamp otp_create_time;
}
