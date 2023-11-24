/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CLIENT.DTO;

import lombok.*;

import java.util.Date;

/**
 *
 * @author ThuanVo
 */

@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private Integer id;
    private String email;
    private String password;
    private String name;
    private Integer status;
    private Boolean gender;
    private Date birthday;
    private String role;
}
