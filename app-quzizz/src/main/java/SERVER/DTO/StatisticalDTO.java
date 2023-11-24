package SERVER.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticalDTO {
    private int totalUser;
    private float maxScore;
    private float minScore;
    private float avgScore;
}
