package auto.ausiot.ausiotrest;

import auto.ausiot.ausiotrest.model.Days;
import auto.ausiot.ausiotrest.model.Schedule;
import auto.ausiot.ausiotrest.model.ScheduleItem;
import auto.ausiot.ausiotrest.model.ScheduleType;
import auto.ausiot.ausiotrest.tasks.ScheduleMaster;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AusiotrestApplicationTests {
	@Autowired
	ScheduleMaster sm ;


	@Test
	public void contextLoads() {
	}

	public Schedule getSchedule(){
		Map<Days, ScheduleItem> si = new HashMap<>();

		Schedule defaultschedule = new Schedule("10",si,true, ScheduleType.Weekly);
		//@TODO Move to constant file
		String schedule = "WEEKLY::1,13:00,9,TRUE;2,13:00,9,TRUE;3,13:00,9,TRUE;4,13:00,9,TRUE;5,13:00,9,TRUE;6,13:00,9,TRUE;0,13:00,9,TRUE";
		try {
			defaultschedule.createSheduleFromString(schedule);
			defaultschedule.setId("10");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return defaultschedule;
	}

	@Test
	public void addSIRUntime() throws SchedulerException, ParseException {
		//ScheduleMaster sm = new ScheduleMaster();
		Schedule sc = getSchedule();
		ScheduleItem scitm = sc.getcheduleItem(Days.Monday);
		sm.addScheduleItemRuntime(sc.getId(),scitm);
	}

	@Test
	public void testTriiger(){
		boolean x = sm.isTriggeredForTheDay(getSchedule().getId());
		int x1 = 0;

	}

	@Test
	public void tesCloseTriiger() throws MqttException, URISyntaxException {
		sm.closeSensor(getSchedule().getId());
		int x1 = 0;

	}

	@Test
	public void testEscape() throws MqttException, URISyntaxException {
		String s= "ozbk/U001";
		s.

	}

}
