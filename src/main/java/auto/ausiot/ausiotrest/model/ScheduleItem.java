package auto.ausiot.ausiotrest.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@Getter @Setter
//employee will be the name of table in mongodb
@Document(collection = "scheduleitem")
public class ScheduleItem {
    @Id
    private @NonNull String id;

    @JsonDeserialize(using= CustomerDateAndTimeDeserialize.class)
    private @NonNull Date time;

    private @NonNull int duration ;
    private @NonNull boolean enabled = true;
    private Date lastTriggered;
}