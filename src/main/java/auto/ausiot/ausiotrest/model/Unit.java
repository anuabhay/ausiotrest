package auto.ausiot.ausiotrest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

//@AllArgsConstructor
@Getter
@Setter
@Document(collection = "unit")
public class Unit {
    @Id
    private @NonNull
    String id;
    private @NonNull String userID;

    private @Transient String mqqttUserID;
    private @Transient String mqqttPassword;
    private @Transient String mqqttUrl;
}
