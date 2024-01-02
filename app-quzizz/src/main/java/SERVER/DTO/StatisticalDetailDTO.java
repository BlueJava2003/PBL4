/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author PC
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StatisticalDetailDTO {
    private int exam_id;
    private int user_id;
    private String exam_name;
    private String user_name;
    private float score;
}
