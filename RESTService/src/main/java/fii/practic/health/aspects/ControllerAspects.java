package fii.practic.health.aspects;

import fii.practic.health.boundry.dto.DoctorDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

// advice
// joinpoint
// aspect
@Aspect
@Component
public class ControllerAspects {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Pointcut("execution(public * *(..))")
	public void publicMethods() {

	}

	@Pointcut("within(fii.practic.health.boundry.controller.*Controller)")
	public void controllers() {

	}

	@Pointcut("publicMethods()&&controllers()")
	public void publicMethodsWithinControllers() {

	}

	@Pointcut("execution(* fii.practic.health.boundry.controller.DoctorController.getDoctors())")
	public void getDoctors() {

	}

	@Pointcut("execution(* fii.practic.health.boundry.controller.DoctorController.save(..))")
	public void saveDoctor() {

	}

	@Around("@annotation(Timed)")
	public Object measure(ProceedingJoinPoint joinPoint) throws Throwable {
		String taskName = joinPoint.getSignature().toString();
		StopWatch watch = new StopWatch();
		watch.start(taskName);
		try {
			return joinPoint.proceed();
		} finally {
			watch.stop();
			logger.info(watch.prettyPrint());
		}
	}

	@Before("saveDoctor()")
	public void beforeSaveDoctor(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		for (Object object : args) {
			if (object instanceof DoctorDTO) {
				DoctorDTO doctorDTO = (DoctorDTO) object;
				if (doctorDTO.getEmail().getEmail().contains("@yahoo.com")) {
					throw new IllegalArgumentException("Yahoo domain is not supported!");
				}
			}
		}
		logger.info("Email address is valid");
	}

	@Before("getDoctors()")
	public void beforeGetDoctorsAdvice() {
		logger.info("Before get doctors");
	}

	@After("getDoctors()")
	public void afterGetDoctorsAdvice() {
		logger.info("After get doctors");
	}
}
