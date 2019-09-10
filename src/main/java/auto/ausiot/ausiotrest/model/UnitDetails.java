package auto.ausiot.ausiotrest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Getter
@Setter
@Document(collection = "unitdetails")
public class UnitDetails {
    @Id
//    private @NonNull
//    String id;
    String unitID;
    private @NonNull String mqqttUserID;
    private @NonNull String mqqttPassword;
    private @NonNull String mqqttUrl;
}
