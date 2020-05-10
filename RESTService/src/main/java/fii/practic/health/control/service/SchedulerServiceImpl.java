package fii.practic.health.control.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fii.practic.health.entity.repository.SchedulerLastRecordedRepository;
import fii.practic.health.scheduler.SchedulerLastRecordedIdValue;

@Service
public class SchedulerServiceImpl implements SchedulerService {

	private SchedulerLastRecordedRepository schdulerRepository;

	@Autowired
	public SchedulerServiceImpl(SchedulerLastRecordedRepository schdulerRepository) {
		this.schdulerRepository = schdulerRepository;
	}

	@Override
	public SchedulerLastRecordedIdValue findLastScheduled() {
		if (schdulerRepository.findAll().size() == 0)
			return null;
		else
			return schdulerRepository.findLastScheduled();
	}

	@Override
	public SchedulerLastRecordedIdValue save(SchedulerLastRecordedIdValue scLastRecord) {

		return schdulerRepository.save(scLastRecord);
	}

}
