package com.zqh.jndi.config;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.naming.NamingException;
import javax.sql.DataSource;

@Slf4j
@Configuration
public class TomcatConfig {


    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;



//    @Lazy
    @Bean(destroyMethod="")
    public javax.sql.DataSource jndiDataSource() throws IllegalArgumentException,
            NamingException {
        log.info("in side jndi ds");
        JndiObjectFactoryBean bean = new JndiObjectFactoryBean();           // create JNDI data source
        bean.setJndiName("java:/comp/env/jndiDataSource");  // jndiDataSource is name of JNDI data source
        bean.setProxyInterface(javax.sql.DataSource.class);
        bean.setLookupOnStartup(true);
        bean.afterPropertiesSet();
        return (javax.sql.DataSource) bean.getObject();
    }

    /**
     * 这里使用 spring boot 内嵌 tomcat，spring boot 启动时候会启动 tomact，其中重写了 postProcessContext 方法初始化了 JNDI JDBC 配置
     */
    @Bean
    public TomcatServletWebServerFactory tomcatFactory() {
        log.info("initializing tomcat factory... ");
        return new TomcatServletWebServerFactory() {

            @Override
            protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
                System.setProperty("catalina.useNaming", "true");
                tomcat.enableNaming();
                return new TomcatWebServer(tomcat, getPort() >= 0);
            }

            protected void postProcessContext(Context context) {
                log.info("in side post process");
                ContextResource resource = new ContextResource();
                resource.setName("jndiDataSource");
                resource.setType(DataSource.class.getName());
                resource.setProperty("driverClassName", driverClassName);
                resource.setProperty("url", url);
                resource.setProperty("username", username);
                resource.setProperty("password", password);
                context.getNamingResources().addResource(resource);
            }
        };
    }
}
