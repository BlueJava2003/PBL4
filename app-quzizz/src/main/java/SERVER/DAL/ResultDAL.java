/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER.DAL;

import SERVER.DAL.Mapper.ResultMapper;
import SERVER.DTO.ResultDTO;
import SERVER.DTO.StatisticalDTO;

import java.io.FileNotFoundException;
import java.util.List;

/**
 *
 * @author Thuan
 */
public class ResultDAL extends AbstractDAL<ResultDTO> {
    private static ResultDAL result = new ResultDAL();
    public ResultDTO getById(int id_result) {
        String  sql = "SELECT * FROM `results` WHERE id = ?";
        List<ResultDTO> temp = result.query(sql, new ResultMapper(), id_result);
        ResultDTO rs = temp.get(0);
        return rs;
    }
    public List<ResultDTO> getByExamId(int id_exam) {
        String  sql = "SELECT * FROM `results` WHERE exam_id = ?";
        List<ResultDTO> temp = result.query(sql, new ResultMapper(), id_exam);
       return temp;
    }

    public ResultDTO getRank(int id_exam, int user_id) {
        String  sql = "SELECT RANK() OVER(ORDER BY score asc) AS Rank FROM `results` WHERE user_id= ? AND exam_id= ?";
        List<ResultDTO> temp =result.query(sql, new ResultMapper(),user_id, id_exam);
        return temp.get(0);
    }

    public StatisticalDTO statistical(int id_exam) {
        String totalUserSql = "SELECT COUNT(*) FROM RESULTS WHERE exam_id = ?";
        int totalUser = result.count(totalUserSql, id_exam);

        String maxScoreSql = "SELECT MAX(score) FROM RESULTS WHERE exam_id = ?";
        float maxScore = result.score(maxScoreSql, id_exam);

        String minScoreSql = "SELECT MIN(score) FROM RESULTS WHERE exam_id = ?";
        float minScore = result.score(minScoreSql, id_exam);

        String avgScoreSql = "SELECT AVG(score) FROM RESULTS WHERE exam_id = ?";
        float avgScore = result.score(avgScoreSql, id_exam);

        return new StatisticalDTO(totalUser,maxScore,minScore,avgScore);
    }

    public StatisticalDTO statisticalServer(int id_exam) {
        String totalUserSql = "SELECT COUNT(*) FROM RESULTS";
        int totalUser = result.count(totalUserSql, id_exam);

        String maxScoreSql = "SELECT MAX(score) FROM RESULTS";
        float maxScore = result.score(maxScoreSql, id_exam);

        String minScoreSql = "SELECT MIN(score) FROM RESULTS";
        float minScore = result.score(minScoreSql, id_exam);

        String avgScoreSql = "SELECT AVG(score) FROM RESULTS";
        float avgScore = result.score(avgScoreSql, id_exam);

        return new StatisticalDTO(totalUser,maxScore,minScore,avgScore);
    }

    public Integer save(ResultDTO result) throws FileNotFoundException {
        StringBuilder sql = new StringBuilder("INSERT INTO results(exam_id, user_id, score)");
        sql.append(" VALUES(?, ?, ?)");
        return insert(sql.toString(),result.getExamID(),
                result.getUserID(), result.getScore());
    }

    public List<ResultDTO> findAll() {
        String sql = "select * from results";
        return query(sql, new ResultMapper());
    }

    public void delete(int result_id) throws FileNotFoundException {
        String sql = "DELETE FROM results WHERE id = ? ";
        update(sql, result_id);
    }

    public void update(ResultDTO result) throws FileNotFoundException {
        StringBuilder sql = new StringBuilder("UPDATE results SET exam_id = ? , user_id = ?, score = ?");
        sql.append("WHERE id = ?");
        update(sql.toString(), 
                result.getExamID(),
                result.getUserID(),
                result.getScore(),
                result.getId());
    }

}