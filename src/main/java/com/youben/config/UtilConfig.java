package com.youben.config;

import com.youben.utils.SpringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*
*hxp(hxpwangyi@126.com)
*2017年9月16日
*
*/
@Configuration
public class UtilConfig {

	
	@Bean(name="springUtils")
	public SpringUtils springUtils(ApplicationContext applicationContext){
		SpringUtils springUtils=new SpringUtils();
		springUtils.setApplicationContext(applicationContext);
		return springUtils;
	}
	

}
