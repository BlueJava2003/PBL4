/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER.DAL.Mapper;

/**
 *
 * @author ThuanVo
 */
import java.sql.ResultSet;

public interface RowMapper<T> {
    T mapRow(ResultSet rs);
}
