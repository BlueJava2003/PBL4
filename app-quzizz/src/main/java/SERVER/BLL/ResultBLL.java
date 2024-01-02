package SERVER.BLL;

import CLIENT.DTO.StatisticalDetailDTO;
import SERVER.DAL.ExamDAL;
import SERVER.DAL.ResultDAL;
import SERVER.DTO.ResponseDTO;
import SERVER.DTO.ResultDTO;
import SERVER.DTO.StatisticalDTO;
import com.google.gson.Gson;

import java.util.List;

public class ResultBLL {
    private static final ResultDAL resultDAL = new ResultDAL();
    private static final ExamDAL examDAL = new ExamDAL();
    private static final Gson gson = new Gson();
    public ResponseDTO addResult(ResultDTO resultDTO) {
        try{
           int id= resultDAL.save(resultDTO);
            return new ResponseDTO(200,String.valueOf(id), "Thêm thành công!!!");
        }
        catch(Exception e){
            return new ResponseDTO(400,"", "Thêm that bai");
        }

    }

    public ResponseDTO statistical(int examID) {
        StatisticalDTO statisticalDTO = resultDAL.statistical(examID);
        return new ResponseDTO(200,gson.toJson(statisticalDTO), "");
    }
    
    public ResponseDTO statisticalDetail(int examID) {
        List<StatisticalDetailDTO> statisticalDTO = resultDAL.statisticalDetail(examID);
        System.out.println("LIST");
        statisticalDTO.forEach(s -> System.out.println(s));
        return new ResponseDTO(200,gson.toJson(statisticalDTO), "");
    }

    public ResponseDTO statisticalServer(int examID) {
        StatisticalDTO statisticalDTO = resultDAL.statisticalServer(examID);
        return new ResponseDTO(200,gson.toJson(statisticalDTO), "");
    }

    public ResponseDTO getByExamId(int examID){
        List<ResultDTO> listResult = resultDAL.getByExamId(examID);
        if (listResult.size() == 0) {
            return new ResponseDTO(400, gson.toJson(listResult), "Đề chưa được thi");
        }
        return new ResponseDTO(200, gson.toJson(listResult), "Đề thi đã được thi");
    }
    
    public ResponseDTO getByExamIdAndUserId(int examID, int userID){
        List<ResultDTO> listResult = resultDAL.getByExamIdAndUserId(examID, userID);
        if (listResult.size() == 0) {
            return new ResponseDTO(400, gson.toJson(listResult), "Đề chưa được thi");
        }
        return new ResponseDTO(200, gson.toJson(listResult), "Đề thi đã được thi");
    }
}
