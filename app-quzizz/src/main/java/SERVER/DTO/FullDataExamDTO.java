package SERVER.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FullDataExamDTO {
    private int id;
//    private List<QuestionDTO> listQuestion;
    private int userID;
    private String subject;
    private String class_room;
    private int quantity;
    private int status;
    private Integer total_time;
}
