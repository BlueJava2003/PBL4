/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CLIENT.DTO;
import lombok.*;
/**
 *
 * @author PC
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TempResultDTO {
    private String id;
    private Integer time_doing;
    private String answer;
    private int exam_id;
    private int question_id;
    private int user_id;
    
}
