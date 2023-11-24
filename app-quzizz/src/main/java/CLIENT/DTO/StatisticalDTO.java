package CLIENT.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StatisticalDTO {
    private int totalUser;
    private float maxScore;
    private float minScore;
    private float avgScore;
}
