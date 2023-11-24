/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER.DAL;

import SERVER.DAL.Mapper.QuestionMapper;
import SERVER.DTO.QuestionDTO;
import java.io.FileNotFoundException;
import java.util.List;

/**
 *
 * @author ThuanVo
 */
public class QuestionDAL extends AbstractDAL<QuestionDTO> {

    public QuestionDTO getById(int id_question) {
        QuestionDAL question = new QuestionDAL();
        String  sql = "SELECT * FROM `questions` WHERE id = ?";
        List<QuestionDTO> temp = question.query(sql, new QuestionMapper(), id_question);
        QuestionDTO result = temp.get(0);
        return result;
    }

    public Integer save(QuestionDTO question) throws FileNotFoundException {
        StringBuilder sql = new StringBuilder("INSERT INTO questions(exam_id,question,A,B,C,D,answer)");
        sql.append(" VALUES(?, ?, ?, ?, ?, ?, ?)");
        return insert(sql.toString(), question.getExamID(),
                question.getQuestion(), question.getA(), 
                question.getB(), question.getC(),
                question.getD(), question.getAnswer());
    }

    public List<QuestionDTO> findAll() {
        String sql = "select * from questions";
        return query(sql, new QuestionMapper());
    }
    public List<QuestionDTO> findQuestionsByExamID(int exam_id) {
        String sql = "select * from questions where exam_id="+exam_id;
        return query(sql, new QuestionMapper());
    }

    public void delete(int question_id) throws FileNotFoundException {
        String sql = "DELETE FROM questions WHERE id = ? ";
        update(sql, question_id);
    }

    public void update(QuestionDTO question) throws FileNotFoundException {
        StringBuilder sql = new StringBuilder("UPDATE questions SET  question = ? , A = ?, B =?, C =?, D = ?, answer =? ");
        sql.append("WHERE id = ?");
        update(sql.toString(),
                question.getQuestion(), 
                question.getA(), 
                question.getB(), 
                question.getC(), 
                question.getD(), 
                question.getAnswer(),
                question.getId());
    }

}