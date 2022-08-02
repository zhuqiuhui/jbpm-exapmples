package com.zqh.jndi.config;

import com.zqh.jndi.jbpm.BrokenWorkItemHandler;
import org.jbpm.process.instance.impl.demo.SystemOutWorkItemHandler;
import org.jbpm.services.task.identity.DBUserGroupCallbackImpl;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.*;
import org.kie.api.task.TaskService;
import org.kie.api.task.UserGroupCallback;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.persistence.EntityManagerFactory;

@Configuration
public class JbpmConfig {

    @Bean
    RuntimeEnvironment runtimeEnvironment(EntityManagerFactory entityManagerFactory) {
        return RuntimeEnvironmentBuilder.Factory.get()
                .newDefaultBuilder().entityManagerFactory(entityManagerFactory)
                .addAsset(ResourceFactory.newClassPathResource("user2.bpmn2"), ResourceType.BPMN2)
                .get();
    }

    @Bean
    RuntimeManager runtimeManager(RuntimeManagerFactory runtimeManagerFactory, RuntimeEnvironment runtimeEnvironment) {
        return runtimeManagerFactory.newSingletonRuntimeManager(runtimeEnvironment);
    }

    @Bean
    RuntimeEngine runtimeEngine(RuntimeManager runtimeManager) {
        return runtimeManager.getRuntimeEngine(EmptyContext.get());
    }

    @Bean
    KieSession kieSession(RuntimeEngine runtimeEngine) {
        KieSession kieSession = runtimeEngine.getKieSession();
        kieSession.getWorkItemManager().registerWorkItemHandler("Broken", new BrokenWorkItemHandler());
        kieSession.getWorkItemManager().registerWorkItemHandler("Human Task", new SystemOutWorkItemHandler());
        return kieSession;
    }

    @Bean
    TaskService taskService(RuntimeEngine runtimeEngine) {
        return runtimeEngine.getTaskService();
    }

    //userGroupCallback用于判断用户是否存在，组是否存在，找到用户所属的组，默认已经配置了SpringSecurityUserGroupCallback作为userGroupCallback的bean，但是我们希望使用自己的数据库表来进行判断，所以配置DBUserGroupCallbackImpl作为 userGroupCallback的bean，需要使用jbpm.usergroup.callback.properties来配置
    @Bean
    @DependsOn("jndiDataSource")
    UserGroupCallback userGroupCallback() {
        System.out.println("userGroupCallback start to init......");
        return new DBUserGroupCallbackImpl(true);
    }
}
