/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER.DAL.Mapper;

import CLIENT.DTO.MyExamResult;
import SERVER.DTO.ExamDTO;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author PC
 */
public class ExamResultMapper implements RowMapper<MyExamResult>{

    @Override
    public MyExamResult mapRow(ResultSet rs) {
        try {
            MyExamResult ex = new MyExamResult();
            ex.setId(rs.getInt("id"));
            ex.setAmount_correct(rs.getInt("amount_correct"));
            ex.setAmount_incorrect(rs.getInt("amount_incorrect"));
            ex.setTime_doing(rs.getInt("time_doing"));
            ex.setPoint(rs.getInt("point"));
            ex.setPosition(rs.getInt("position"));
            ex.setExam_id(rs.getInt("exam_id"));
            ex.setUser_id(rs.getInt("user_id"));           
            return ex;
        } catch (SQLException e) {
            return null;
        }
    }
}
