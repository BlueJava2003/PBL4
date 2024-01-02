/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER.DAL.Mapper;

import CLIENT.DTO.StatisticalDetailDTO;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author PC
 */
public class StatisticalDetailMapper implements RowMapper<StatisticalDetailDTO>{

    @Override
    public StatisticalDetailDTO mapRow(ResultSet rs) {
        try {
            StatisticalDetailDTO st = new StatisticalDetailDTO();
            st.setExam_id(rs.getInt("exam_id"));
            st.setUser_id(rs.getInt("user_id"));
            st.setExam_name(rs.getString("subject"));
            st.setUser_name(rs.getString("name"));
            st.setScore(rs.getFloat("score"));
            return st;
        } catch (SQLException e) {
            return null;
        }
    }
}
