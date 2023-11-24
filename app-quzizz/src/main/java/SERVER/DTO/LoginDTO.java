package SERVER.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@AllArgsConstructor
public class LoginDTO {
    private String email;
    private String password;
}
