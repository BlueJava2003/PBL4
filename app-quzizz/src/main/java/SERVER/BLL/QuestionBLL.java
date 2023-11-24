/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER.BLL;

import SERVER.DAL.QuestionDAL;
import SERVER.DTO.QuestionDTO;
import SERVER.DTO.ResponseDTO;
import com.google.gson.Gson;

import java.util.List;

/**
 *
 * @author nguye
 */
public class QuestionBLL {

    private static QuestionDAL questionDAL = new QuestionDAL();
    private static Gson gson = new Gson();
    public QuestionBLL(){}

    public ResponseDTO getListExam() {
        List<QuestionDTO> listExam = questionDAL.findAll();
        return new ResponseDTO(200, gson.toJson(listExam), "");

    }
    public ResponseDTO createListQuestion(List<QuestionDTO> listQuestion){
        try{
            for (QuestionDTO qs : listQuestion){
                questionDAL.save(qs);
            }
            return new ResponseDTO(200, "", "Add list question Successfully!!!");
        }
        catch(Exception e){
            return new ResponseDTO(400, "", "Invalid Param");
        }
    }
    
    public void deleteQuestionDTO(int ID) throws Exception{
        try {
            questionDAL.delete(ID);
        } catch (Exception e) {
            System.out.println("Khong the Xoa QuestionDTO vao database !!!");
        }
    }

    public ResponseDTO updateQuestion(QuestionDTO Question) throws Exception{
       try{
           questionDAL.update(Question);
           return new ResponseDTO(400, "", "Cập nhật thành công");
       }
       catch (Exception e){
          //
       }
        return new ResponseDTO(400, "", "Cập nhật thất bại");
    }

    public ResponseDTO findQuestionsByExamID(int examID){
        List<QuestionDTO> listExam =questionDAL.findQuestionsByExamID(examID);
        return new ResponseDTO(200, gson.toJson(listExam), "");
    }
}
