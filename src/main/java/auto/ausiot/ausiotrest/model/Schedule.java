package auto.ausiot.ausiotrest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;


@AllArgsConstructor
@Getter @Setter
//employee will be the name of table in mongodb
@Document(collection = "schedule")
public class Schedule {

    @Id
    private @NonNull String id;
    private @NonNull Map<Days, ScheduleItem> mapSchedule;
    private @NonNull boolean enabled = true;
    private @NonNull ScheduleType type = ScheduleType.Daily;

    public boolean isInitialized(){
        boolean ret = true;
        if (mapSchedule.size() == 0)
            ret = false;
        return ret;
    }

    public void createSchedule(ScheduleItem si){
        for (Days day : Days.values()) {
            mapSchedule.put(day,si);
        }

    }

    public ScheduleItem getcheduleItem(Days day) throws ParseException {
        ScheduleItem si = mapSchedule.get(day);
        return si;
    }
        //@RequiresApi(api = Build.VERSION_CODES.O)
    public ScheduleItem hasScheduleItem(int iday, Date time) throws ParseException {
        Days day = Days.get(iday);
        ScheduleItem si = mapSchedule.get(day);
        //long diffmints = Duration.between(si.time , time).toMinutes();
        if (si != null) {
            SimpleDateFormat df = new SimpleDateFormat("hh:mm");
            String strscheduledtime = df.format(si.getTime());
            String strnowtime = df.format(time);
            Date scheduledtime = df.parse(strscheduledtime);
            Date nowtime = df.parse(strnowtime);

            long diffmints = (scheduledtime.getTime() - nowtime.getTime()) / (1000 * 60);
            if ((diffmints > 0) && (diffmints < 15)) {
                return si;
            }
        }
        // No Schedules to be excute now , return null
        return null;
    }


//    public boolean getTriggered(ScheduleItem si) throws ParseException {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(si.getTime());
//        Days day = Days.get(calendar.get(Calendar.DAY_OF_WEEK) - 1);
//        si = mapSchedule.get(day);
//        boolean ret = false;
//        //long diffmints = Duration.between(si.time , time).toMinutes();
//        if (si != null) {
//            Date nowtime = new Date();
//            if (si.getLastTriggered() != null) {
//                long diffmints = (si.getTime().getTime() - nowtime.getTime()) / (1000 * 60);
//                if ((diffmints > 0) && (diffmints < 23 * 60)) {
//                    ret = true;
//                } else {
//                    ret = false;
//                }
//            }else{
//                ret = false;
//            }
//        }
//        return ret;
//    }

    public void setTriggered(ScheduleItem si) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(si.getTime());
        Days day = Days.get(calendar.get(Calendar.DAY_OF_WEEK) - 1);
        si = mapSchedule.get(day);
        if (si != null) {
            si.setLastTriggered(new Date());
        }
    }


    public void createSheduleFromString(String schedule) throws ParseException {
        int itype = schedule.indexOf("::");
        if (itype > 0){
            String stype = schedule.substring(0,itype);
            if (stype.equalsIgnoreCase("WEEKLY") ==true){
                type = ScheduleType.Weekly;
            }else{
                type = ScheduleType.Daily;
            }
            schedule = schedule.substring(itype + 2, schedule.length());
            String delims = "[;]";
            String[] tokens = schedule.split(delims);
            for (int i = 0; i < tokens.length; i++){
                String delim_time = "[,]";
                String[] tokens_time = tokens[i].split(delim_time);
                String sday = tokens_time[0];
                String stime = tokens_time[1];
                String sduration = tokens_time[2];
                String senabled = tokens_time[3];

                Date time = new SimpleDateFormat("HH:mm").parse(stime);
                int duration = Integer.parseInt(sduration);
                setSheduleItem(Integer.parseInt(sday),time,duration,Boolean.parseBoolean(senabled));
            }
        }
    }

    public void setSheduleItem(int iday, Date time , int duration, boolean enabled ){
        Days day = Days.get(iday);
        ScheduleItem si = new ScheduleItem("id",time,duration, enabled,null);
        mapSchedule.put(day,si);
    }

}