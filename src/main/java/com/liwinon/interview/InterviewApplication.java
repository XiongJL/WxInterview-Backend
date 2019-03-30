package com.liwinon.interview;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InterviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterviewApplication.class, args);
	}
	
	/*以下通过Nginx实现*/
	
//	 /**
//	    * http重定向到https
//	    * @return
//	    */
//	   @Bean
//	   public TomcatServletWebServerFactory servletContainer() {
//	      TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//	         @Override
//	         protected void postProcessContext(Context context) {
//	            SecurityConstraint constraint = new SecurityConstraint();
//	            constraint.setUserConstraint("CONFIDENTIAL");
//	            SecurityCollection collection = new SecurityCollection();
//	            collection.addPattern("/*");
//	            constraint.addCollection(collection);
//	            context.addConstraint(constraint);
//	         }
//	      };
//	      tomcat.addAdditionalTomcatConnectors(httpConnector());
//	      return tomcat;
//	   }

//	   @Bean
//	   public Connector httpConnector() {
//	      Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//	      connector.setScheme("http");
//	      //Connector监听的http的端口号
//	      connector.setPort(8098);
//	      connector.setSecure(false);
//	      //监听到http的端口号后转向到的https的端口号
//	      HttpServletRequest request = connector.createRequest();
//	      HttpServletResponse response = connector.createResponse(); 
//	      String requestUrl = request.getRequestURL().toString();
//	      connector.setRedirectPort(443);
//	      return connector;
//	   }
}
