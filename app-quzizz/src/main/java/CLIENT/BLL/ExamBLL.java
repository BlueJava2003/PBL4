/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CLIENT.BLL;

import CLIENT.Client;
import CLIENT.DTO.ExamDTO;
import CLIENT.DTO.ResponseDTO;
import CLIENT.DTO.StatisticalDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
/**
 *
 * @author nguye
 */
public class ExamBLL {
    static ArrayList<ExamDTO> listExam;
    static ArrayList<ExamDTO> listUserExam;
    static String list;
    public static Gson gson = new Gson();

    public static String getList() {
        return list;
    }

    public static void setList(String list) {
        ExamBLL.list = list;
    }

    public ExamBLL(){}

    public static ArrayList<ExamDTO> getListExam() {
        return listExam;
    }

    public static ArrayList<ExamDTO> getListUserExam() {
        return listUserExam;
    }
    public static void setListExam(ArrayList<ExamDTO> listExam) {
        ExamBLL.listExam = listExam;
    }
    
     public void  loadDSExam(String key ) throws Exception{
        if(listExam==null) listExam = new ArrayList<>();
        ResponseDTO listData= Client.CallServer(key,"1");
        Type listType = new TypeToken<ArrayList<ExamDTO>>(){}.getType();
        listExam = gson.fromJson(listData.getResult(), listType);
    }

     public void  loadDSUserExam(String data ) throws Exception{
        if(listUserExam==null) listUserExam = new ArrayList<>();
        ResponseDTO listData= Client.CallServer("LOAD_USER_EXAM",data);
        Type listType = new TypeToken<ArrayList<ExamDTO>>(){}.getType();
        listUserExam = gson.fromJson(listData.getResult(), listType);
    }
    public ResponseDTO addExamDTO(ExamDTO ex) throws Exception{
        try {
            ResponseDTO exam = Client.CallServer("ADD_EXAM", gson.toJson(ex));
            ExamDTO examDTO = gson.fromJson(exam.getResult(), ExamDTO.class);
            listUserExam.add(examDTO);
            return exam;
        } 
        catch(Exception e){
            System.out.println("Khong the them ExamDTO Item vao database ");
        }
        return null;
    }
    
    public String deleteExamDTO(int ID) throws Exception{
        ResponseDTO res=null;
        for(int i=0;i<listUserExam.size();i++)
        {
            if(listUserExam.get(i).getId()==ID)
            {   
                try {
                    res = Client.CallServer("DELETE_EXAM", String.valueOf(ID));

                  listUserExam.remove(i);// xoá trong arraylist
                } catch (Exception e) {
                       // return "Xóa thất bại";
                } 
                
            }
        }
        return (res.getStatus()==200)?"Xóa thành công":"Xóa thất bại";
        
    }
    public boolean checkTakenExam(String examID){
        try {
             ResponseDTO res = Client.CallServer("CHECK_EXAM", examID);
             return res.getStatus() == 200;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public String updateExamDTO(ExamDTO ex) throws Exception{
        for(int i = 0 ; i < listUserExam.size() ; i++)
        {
            if(listUserExam.get(i).getId()== ex.getId())
            {
                try {
                    ResponseDTO res= Client.CallServer("UPDATE_EXAM",gson.toJson(ex));
                     // check respond
                    listUserExam.set(i, ex);
                    return (res.getStatus()==200) ? "Cập nhật thành công!":"Cập nhật thất bại!";
                  
                } catch (Exception e) {
                    System.out.println("Khong the Cap nhat ExamDTO vao database !!!");
                   
                }


            }
        }
        return "Fail";
    }
    public ExamDTO findExamByID(int id) {
        for(ExamDTO ex : listExam)  {
            if(ex.getId()==id) return ex;
        }
        return null;
    }
    public StatisticalDTO statisticExam(int idExam) throws Exception {
        ResponseDTO res= Client.CallServer("STATISTICAL",String.valueOf(idExam   ));
        return gson.fromJson(res.getResult(),StatisticalDTO.class   );

    }
    
}
