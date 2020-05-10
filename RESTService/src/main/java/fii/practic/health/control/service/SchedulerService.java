package fii.practic.health.control.service;

import fii.practic.health.scheduler.SchedulerLastRecordedIdValue;

public interface SchedulerService {

	SchedulerLastRecordedIdValue findLastScheduled();

	SchedulerLastRecordedIdValue save(SchedulerLastRecordedIdValue scLastRecord);

}
