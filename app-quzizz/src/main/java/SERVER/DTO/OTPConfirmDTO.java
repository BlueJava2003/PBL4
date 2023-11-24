/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER.DTO;

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
public class OTPConfirmDTO {
    private String email;
    private String otp;
}
