package com.meta.mesim.esb.scheduler;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class WeatherApiCallJob implements Job {

    private static final String PAGE_NO = "pageNo";
    private static final String NUM_OF_ROWS = "numOfRows";
    private static final String SERVICE_KEY = "serviceKey";
    private static final String BASE_DATE = "base_date";
    private static final String BASE_TIME = "base_time";
    private static final String NX = "nx";
    private static final String NY = "ny";
    private static final String DATE_TYPE = "dataType";

    protected Log log = LogFactory.getLog(getClass());

    /**
     * 스케줄러에서 주기적으로 실행되는 메서드
     * Unirest를 이용해 get요청을 보낸다.
     * 현재는 log로 데이터 표현만 해두는 중.
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //quartz 컨텍스트에서 필요한 데이터를 받아옴.
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        //쿼리파라미터로 들어가는 요소들
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put(PAGE_NO,jobDataMap.getString("pageNo"));
        queryParams.put(NUM_OF_ROWS, jobDataMap.getString("numOfRows"));
        queryParams.put(SERVICE_KEY, jobDataMap.getString("serviceKey"));
        queryParams.put(BASE_DATE, getDateNow());
        queryParams.put(BASE_TIME, getTimeNow());
        queryParams.put(NX, jobDataMap.getString("nx"));
        queryParams.put(NY, jobDataMap.getString("ny"));
        queryParams.put(DATE_TYPE, jobDataMap.getString("dataType"));

        try {
            HttpResponse<String> response = Unirest.get(jobDataMap.getString("url")).queryString(queryParams).asString();
            String body = response.getBody();
            log.info("body: " + body);
        } catch (UnirestException e) {
            log.info("### Weather API Call Error : " + e.getMessage());
        }

    }

    /**
     * 쿼리파라미터에 필요한 형태로 현재 시간을 반환하는 메서드
     * @return 현재시간(HHmm)
     */
    private String getTimeNow() {
        String[] timeSplit = LocalTime.now().toString().split(":");
        StringBuilder timeBuild = new StringBuilder();
        timeBuild.append(timeSplit[0]);
        timeBuild.append(timeSplit[1]);

        return timeBuild.toString();
    }

    /**
     * 쿼리파라미터에 필요한 형태로 현재 날짜를 반환하는 메서드
     * @return 현재날짜(yyyyMMDD)
     */
    private String getDateNow() {
        String dateNow = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return dateNow;
    }
}
