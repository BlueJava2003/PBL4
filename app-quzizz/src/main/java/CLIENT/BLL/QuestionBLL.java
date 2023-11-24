/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CLIENT.BLL;


import CLIENT.Client;
import CLIENT.DTO.QuestionDTO;
import CLIENT.DTO.ResponseDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 *
 * @author nguye
 */
public class QuestionBLL {
    
    public static ArrayList<QuestionDTO> listQuestion;
    public static Gson gson = new Gson();

    public QuestionBLL(){}

    public static ArrayList<QuestionDTO> getListQuestion() {
        return listQuestion;
    }

    public static void setListQuestion(ArrayList<QuestionDTO> listQuestion) {
        QuestionBLL.listQuestion = listQuestion;
    }
    
     public void  loadDSQuestion(int examID) throws Exception {
          listQuestion = new ArrayList<>();
         ResponseDTO listData= Client.CallServer("LOAD_QUESTIONS",examID+"");
         Type listType = new TypeToken<ArrayList<QuestionDTO>>(){}.getType();
         listQuestion= gson.fromJson(listData.getResult(), listType);
          //Collections.shuffle(listQuestion);
    }
     
     public String addListQuestion(ArrayList<QuestionDTO> listQues) throws Exception {
          ResponseDTO res= Client.CallServer("ADD_QUESTIONS",gson.toJson(listQues));
         return (res.getStatus()==200) ? "Thêm thành công!":"Thêm thất bại!";
     }
    
    public void addQuestionDTO(QuestionDTO qs) throws Exception{
        try{
            listQuestion.add(qs);

        } 
        catch(Exception e){
            System.out.println("Khong the them QuestionDTO Item vao database ");
        }
    }
    
    public void deleteQuestionDTO(int ID) throws Exception{
        for(QuestionDTO qs : listQuestion )
        {
            if(qs.getId()==ID)
            {   
                try {
                   listQuestion.remove(qs);// xoá trong arraylist
                } catch (Exception e) {
                    System.out.println("Khong the Xoa QuestionDTO vao database !!!");
                } 
                return;
            }
        }
        
    }
    public void updateQuestionDTO(QuestionDTO qs) throws Exception{
         for(int i = 0 ; i < listQuestion.size() ; i++)
        {
            if(Objects.equals(listQuestion.get(i).getId(), qs.getId()))
            {
                try {
                    Client.CallServer("UPDATE_QUESTION", gson.toJson(qs));
                    listQuestion.set(i, qs);
                } catch (Exception e) {
                    System.out.println("Khong the Cap nhat QuestionDTO vao database !!!");
                }
                return;
            }
        }
    }
}
