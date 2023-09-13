package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BasePage {

    public BasePage() {
    }
    private String flash;
    private String flashType;
}
