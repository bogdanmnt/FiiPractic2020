package fii.practic.health.scheduler;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fii.practic.health.control.service.AppointmentService;
import fii.practic.health.control.service.SchedulerService;
import fii.practic.health.entity.model.Appointment;

@ComponentScan
@Component
public class ScheduledTasks {
	// NOTE: naming should/can be improved
	// BUSINESS LOGIC: the method ScheduledTasks scans the database every 5 minutes
	// and marks the appointments that already took place.
	// !IMPORTANT --> the appointments should take place even if the served is down!
	// For example: if server is down for 1 day, when restarted, it will know to
	// mark the appointments for that day

	AppointmentService appointment;
	SchedulerService schedulerlastRecordedService;

	@Autowired
	public ScheduledTasks(AppointmentService appointment, SchedulerService schedulerlastRecordedService) {
		this.appointment = appointment;
		this.schedulerlastRecordedService = schedulerlastRecordedService;
	}

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(fixedRate = 300000)
	public synchronized void reportCurrentTime() {
		log.info("The time is now {}", dateFormat.format(new Date()));
		log.info("I am scanning the Database...");

		SchedulerLastRecordedIdValue lastIdRecordedEntity = schedulerlastRecordedService.findLastScheduled();

		if (appointment.findLastAppointmentId() == null) {
			log.info("DATABASE IS EMPTY");
			log.info("...............................................................");
		} else if (lastIdRecordedEntity == null) {
			log.warn("NO ENTRY FROM DB FROM scheduler_last_recorded_id_valued");
			log.info("Adding ALL data to the log file...");
			List<Appointment> lastRecordedList = appointment.findAllEndedAppointments();
			for (Appointment a : lastRecordedList) {
				log.info("The appointment with id=" + a.getId() + " has ended at: " + a.getEndTime());
			}
			Long idValue = lastRecordedList.get(lastRecordedList.size() - 1).getId();
			schedulerlastRecordedService.save(new SchedulerLastRecordedIdValue(idValue));
			log.info("...............................................................");
		}

		else {
			LocalDateTime date = appointment.getById(lastIdRecordedEntity.getValue()).getEndTime();
			List<Appointment> lastRecordedList = appointment.TestScheduller(date);
			if (lastRecordedList.size() > 0) {
				for (Appointment a : lastRecordedList) {
					log.info("The appointment with id=" + a.getId() + " has ended at: " + a.getEndTime());

				}
				Long idValue = lastRecordedList.get(lastRecordedList.size() - 1).getId();
				schedulerlastRecordedService.save(new SchedulerLastRecordedIdValue(idValue));
				log.info("...............................................................");

			} else
				log.info("NO NEW APPOINTMENT...");
		}

	}

}
