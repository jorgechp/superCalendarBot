import java.time.Instant;
import java.util.Date;
import java.util.List;

import com.jorgechp.calendarBot.ReminderSystem.ReminderSystem;
import com.jorgechp.calendarBot.ReminderSystem.entities.components.Periodicity;
import com.jorgechp.calendarBot.TelegramBot.TelegramBot;


public class Main {

	public static void main(String[] args) throws Exception {
		Date date = new Date();
		long time = date.getTime()+3000*2;		
		
		long userId = 2677003L;
		ReminderSystem reminderSystem = new ReminderSystem();
		reminderSystem.addUser(userId);
		reminderSystem.startReminderSystem();
		
/*		long reminderId = reminderSystem.addReminder("hola Eloi", "jejejejej", userId);
		reminderSystem.addNotification(reminderId,
				Instant.ofEpochMilli(time),
				Periodicity.MINUTE*15, 2, true);
		
		reminderSystem.startReminderSystem();
		
		List<Instant> listTriggersTime = reminderSystem.getAllJobsInScheduler();
		
		for(Instant t : listTriggersTime){
			System.out.println(t);
		}*/
		
		TelegramBot telegramBot = new TelegramBot();
		telegramBot.setReminderSystem(reminderSystem);
		try{
			telegramBot.startBot();
		}catch (org.telegram.telegrambots.exceptions.TelegramApiRequestException e){
			telegramBot = null;			
			System.err.println("Ya hay un bot conectado con las misma clave privada");
			System.err.println("Desconectando");
			return;
		}
		
		reminderSystem.registerNotificationListener(telegramBot);
		



	}

}
