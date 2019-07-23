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
@Document(collection = "scheduleruntime")
public class ScheduleItemRuntime {
    @Id
    private @NonNull String id;
    private @NonNull Date starttime;
    private @NonNull Date endtime;
    private @NonNull String status;
}