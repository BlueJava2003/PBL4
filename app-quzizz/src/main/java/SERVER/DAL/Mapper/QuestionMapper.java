/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER.DAL.Mapper;

import SERVER.DTO.QuestionDTO;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ThuanVo
 */
public class QuestionMapper implements RowMapper<QuestionDTO>{

    @Override
    public QuestionDTO mapRow(ResultSet rs) {
        try {
            QuestionDTO q = new QuestionDTO();
            q.setId(rs.getInt("id"));
//            q.setExam_id(rs.getInt("exam_id"));
            q.setQuestion(rs.getNString("question"));
            q.setA(rs.getNString("A"));
            q.setB(rs.getNString("B"));
            q.setC(rs.getNString("C"));
            q.setD(rs.getNString("D"));
            q.setAnswer(rs.getString("answer"));
            return q;
        } catch (SQLException e) {
            return null;
        }
    }
}
