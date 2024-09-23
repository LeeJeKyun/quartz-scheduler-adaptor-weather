package com.meta.mesim.esb;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WeatherApp {

	public static void main(String[] args) {

		//resources의 bean.xml을 통해 컨텍스트 생성
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");

	}

}
