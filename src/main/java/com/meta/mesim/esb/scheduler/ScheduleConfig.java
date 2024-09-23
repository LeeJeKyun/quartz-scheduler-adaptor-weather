package com.meta.mesim.esb.scheduler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleConfig {

    protected Log log = LogFactory.getLog(getClass());

    private static Scheduler scheduler;

    private String pageNo;
    private String numOfRows;
    private String serviceKey;
    private String nx;
    private String ny;
    private String dataType;
    private String url;

    private String cronExpression;

    /**
     * scheduleConfig빈이 생성된 후 실행되는 Init 메서드
     * 내부적으로 스케줄러를 세팅하고 실행함.
     *
     * @throws SchedulerException
     */
    public void init() throws SchedulerException {
        setScheduler();
        scheduler.start();
        log.info("########################## Scheduler Start ########################");
    }

    /**
     * quartz 스케줄러를 이용하며 스케줄러 생성 -> 잡 생성 -> 트리거 생성 -> 스케줄러에 잡과 트리거 등록까지 실행
     * 잡은 WeatherApiCallJob클래스를 이용하며 트리거는 크론식을 이용한다. 해당 크론식은 bean.xml에 등록되어있음.
     *
     * @throws SchedulerException
     */
    private void setScheduler() throws SchedulerException {
        scheduler = new StdSchedulerFactory().getScheduler();
        JobDetail jobDetail = JobBuilder.newJob().withIdentity("ApiJob").ofType(WeatherApiCallJob.class).build();
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("ApiTrigger")
                .forJob("ApiJob")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .startNow()
                .build();

        setData(jobDetail.getJobDataMap());
        scheduler.scheduleJob(jobDetail, trigger);
        log.info("################ scheduler setup success ####################");
    }

    private void setData(JobDataMap jobDataMap) {
        jobDataMap.put("pageNo", pageNo);
        jobDataMap.put("numOfRows", numOfRows);
        jobDataMap.put("serviceKey", serviceKey);
        jobDataMap.put("nx", nx);
        jobDataMap.put("ny", ny);
        jobDataMap.put("dataType", dataType);
        jobDataMap.put("url", url);
    }

    public static void setScheduler(Scheduler scheduler) {
        ScheduleConfig.scheduler = scheduler;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public void setNumOfRows(String numOfRows) {
        this.numOfRows = numOfRows;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public void setNx(String nx) {
        this.nx = nx;
    }

    public void setNy(String ny) {
        this.ny = ny;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

}
