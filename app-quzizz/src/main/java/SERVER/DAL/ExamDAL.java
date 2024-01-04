/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER.DAL;

import CLIENT.DTO.MyExamResult;
import SERVER.DAL.Mapper.ExamMapper;
import SERVER.DAL.Mapper.ExamResultMapper;
import SERVER.DTO.ExamDTO;
import SERVER.DTO.FullDataExamDTO;

import java.io.FileNotFoundException;
import java.util.List;

/**
 *
 * @author
 */
public class ExamDAL extends AbstractDAL<ExamDTO> {

    public ExamDTO getById(int id_exam) {
        ExamDAL exam = new ExamDAL();
        String  sql = "SELECT * FROM `exams` WHERE id = ?";
        List<ExamDTO> temp = exam.query(sql, new ExamMapper(), id_exam);
        ExamDTO result = temp.get(0);
        return result;
    }

    public Integer save(ExamDTO exam) throws FileNotFoundException {
        StringBuilder sql = new StringBuilder("INSERT INTO exams(user_id, subject, class_room, quantity, status, total_time)");
        sql.append(" VALUES(?, ?, ?, ?, ?, ?)");
        return insert(sql.toString(),exam.getUserID(),
                exam.getSubject(), exam.getClass_room(),
                exam.getQuantity(), exam.getStatus(),
                exam.getTotal_time());
    }
    
    public Integer saveExamResult(MyExamResult exam) throws FileNotFoundException {
        StringBuilder sql = new StringBuilder("INSERT INTO temp_result(amount_correct"
                                                                    + ", amount_incorrect"
                                                                    + ", time_doing"
                                                                    + ", point"
                                                                    + ", position"
                                                                    + ", exam_id"
                                                                    + ", user_id)");
        sql.append(" VALUES(?, ?, ?, ?, ?, ?, ?)");
        return insert(sql.toString(),
                exam.getAmount_correct(),
                exam.getAmount_incorrect(),
                exam.getTime_doing(),
                exam.getPoint(), 
                exam.getPosition(),
                exam.getExam_id(),
                exam.getUser_id()
            );
    }

    public List<ExamDTO> findAll() {
        String sql = "SELECT * from exams where quantity > 0 and status = 1";
        return query(sql, new ExamMapper());
    }

    public List<ExamDTO> findByUser(int userId) {
        String sql = "SELECT * from exams where status =1 and user_id=" + userId;
        return query(sql, new ExamMapper());
    }
    
    public void delete(int exam_id) throws FileNotFoundException {
        String sql = "DELETE FROM exams WHERE id = ? ";
        update(sql, exam_id);
    }

    public void updateStatus(int status,int exam_id) throws FileNotFoundException {
        StringBuilder sql = new StringBuilder("UPDATE exams SET status = ? ");
        sql.append("WHERE id = ?");
        update(sql.toString(), status, exam_id);
    }

    public void update(FullDataExamDTO exam) throws FileNotFoundException {
        StringBuilder sql = new StringBuilder("UPDATE exams SET subject = ?, class_room = ?, quantity = ?, status = ?, total_time = ?, user_id = ? ");
        sql.append("WHERE id = ?");
        update(sql.toString(), exam.getSubject(),
                exam.getClass_room(), exam.getQuantity(),
                exam.getStatus(), exam.getTotal_time(),
                exam.getUserID(),
                exam.getId());
    }
    
    public List<MyExamResult> getExamResultByExamIdAndUserID(int examId, int userId) {
        String sql = "SELECT * from temp_result where exam_id  = " + examId + " and user_id = " + userId;
        return query(sql, new ExamResultMapper());
    }
    
    public void deleteExamResult(int examId, int userId) throws FileNotFoundException {
       String sql = "DELETE FROM temp_result WHERE exam_id = ? and user_id = ? ";
       update(sql, examId, userId);
    }
}