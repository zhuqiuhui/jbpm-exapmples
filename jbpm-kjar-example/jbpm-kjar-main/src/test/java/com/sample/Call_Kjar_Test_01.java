package com.sample;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import junit.framework.TestCase;
import org.jbpm.services.task.identity.JBossUserGroupCallbackImpl;
import org.jbpm.test.JBPMHelper;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.task.TaskService;
import org.kie.api.task.UserGroupCallback;
import org.kie.api.task.model.TaskSummary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Call_Kjar_Test_01 extends TestCase {

    @Test
    public void testProcess() {

        String groupId = "org.example";
        String artifactId = "jbpm-kjar-deploy-01";
        String version = "1.0-SNAPSHOT";

        KieServices ks = KieServices.Factory.get();
        ReleaseId releaseId = ks.newReleaseId(groupId, artifactId, version);
        RuntimeManager manager = createRuntimeManager(releaseId);
        RuntimeEngine engine = manager.getRuntimeEngine(null);
        KieSession ksession = engine.getKieSession();

        Map<String, Object> params = new HashMap<String, Object>();
        ksession.startProcess("jbpm-kjar-deploy-01", params);

        TaskService taskService = engine.getTaskService();

        // -----bpmsAdmin get task and start task-------
        {
            List<TaskSummary> list = taskService.getTasksAssignedAsPotentialOwner("bpmsAdmin", "en-UK");
            for (TaskSummary taskSummary : list) {
                System.out.println("bpmsAdmin starts a task : taskId = " + taskSummary.getId());
                taskService.start(taskSummary.getId(), "bpmsAdmin");
                taskService.complete(taskSummary.getId(), "bpmsAdmin", null);
            }
        }

        manager.disposeRuntimeEngine(engine);
    }

    private RuntimeManager createRuntimeManager(ReleaseId releaseId) {
        Properties properties = new Properties();
        properties.setProperty("bpmsAdmin", "");
        UserGroupCallback userGroupCallback = new JBossUserGroupCallbackImpl(properties);

        JBPMHelper.startH2Server();
        JBPMHelper.setupDataSource();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
        RuntimeEnvironmentBuilder builder = RuntimeEnvironmentBuilder.Factory.get()
                .newDefaultBuilder(releaseId).entityManagerFactory(emf).userGroupCallback(userGroupCallback)
                ;
        return RuntimeManagerFactory.Factory.get()
                .newSingletonRuntimeManager(builder.get());
    }

}
