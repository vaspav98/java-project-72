package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
public class Url {

    public Url(String name) {
        this.name = name;
    }

    private long id;
    private String name;
    private Timestamp createdAt;
}
