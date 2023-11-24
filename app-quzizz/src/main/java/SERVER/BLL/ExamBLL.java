/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER.BLL;

import CLIENT.DTO.MyExamResult;
import SERVER.DAL.ExamDAL;
import SERVER.DAL.QuestionDAL;
import SERVER.DAL.ResultDAL;
import SERVER.DTO.ExamDTO;
import SERVER.DTO.FullDataExamDTO;
import SERVER.DTO.ResponseDTO;
import SERVER.DTO.ResultDTO;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.util.List;

/**
 *
 * @author nguye
 */
public class ExamBLL {
    private static ExamDAL examDal = new ExamDAL();
    private static QuestionDAL questionDAL = new QuestionDAL();
    private static ResultDAL resultDAL = new ResultDAL();
    private static Gson gson = new Gson();
    private int userID;
    public ExamBLL(){}

    public ExamBLL(int userID){
        this.userID=userID;
    }

    public ResponseDTO getListExam() {
        List<ExamDTO> listExam = examDal.findAll();
        return new ResponseDTO(200, gson.toJson(listExam), "");

    }
    
     public ResponseDTO  getExamByUserID(int userID){
         List<ExamDTO> listExam = examDal.findByUser(userID);
         return new ResponseDTO(200, gson.toJson(listExam), "");
     }
    
    public ResponseDTO addExamDTO(ExamDTO ex){
        try{
           int idExam= examDal.save(ex);
           ex.setId(idExam);
        } 
        catch(Exception e){
            return new ResponseDTO(400, "", "Add new exam failed!!!");
        }
        return new ResponseDTO(200, gson.toJson(ex), "Add Exam successfully!!!");
    }
    
    public ResponseDTO deleteExam(int id) throws Exception{
        try {
            ExamDTO exam = examDal.getById(id);
            if (exam.getStatus() == 1){
                return new ResponseDTO(400, "", "Can not delete this exam");
            }
            examDal.updateStatus(0, id);
        } catch (Exception e) {
            return  new ResponseDTO(400, "", "Can not delete this exam");
        }
        return  new ResponseDTO(200, "", "Delete Successfully!!!");
    }
    
    public ResponseDTO updateExam(FullDataExamDTO ex) throws FileNotFoundException {
        List<ResultDTO> listResult = resultDAL.getByExamId(ex.getId());

        if (listResult.size() != 0){
            return new ResponseDTO(400, "", "Đề đã được thi.");
        }

        examDal.update(ex);
        return new ResponseDTO(200, "", "Update Exam successfully!!!");
    }
    
    public ResponseDTO addExamResultDTO(MyExamResult ex){
        try{
           int idExam= examDal.saveExamResult(ex);
           ex.setId(idExam);
        } 
        catch(Exception e){
            return new ResponseDTO(400, "", "Add new exam failed!!!");
        }
        return new ResponseDTO(200, gson.toJson(ex), "Add Exam successfully!!!");
    }
    
    public ResponseDTO getExamResultByExamIdAndUserID(int examID, int userID){
         List<MyExamResult> listExamResult = examDal.getExamResultByExamIdAndUserID(examID, userID);
         return new ResponseDTO(200, gson.toJson(listExamResult), "");
    }
    
    public ResponseDTO deleteExamResult(int examID, int userID) throws FileNotFoundException {
        examDal.deleteExamResult(examID,userID);
        return new ResponseDTO(200, "", "Delete successfully!");
    }
}
