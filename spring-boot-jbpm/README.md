# spring-boot-jbpm
spring boot with jbpm



- Step 1：start spring boot
- Step 2： visit：http://localhost:8080/, then input username and pw, such as: mary=mary1
- Step 3： post visit：http://localhost:8080/deployment/deploy?id=org.example:jbpm-base-example:1.0-SNAPSHOT&strategy=SINGLETON
(you can use postman, before visit you should copy cookie)
```bash
2022-07-31 18:54:05,879 [http-nio-8080-exec-9] INFO  KieModule was added: ZipKieModule[releaseId=org.example:jbpm-base-example:1.0-SNAPSHOT,file=C:\Users\NINGMEI\.m2\repository\org\example\jbpm-base-example\1.0-SNAPSHOT\jbpm-base-example-1.0-SNAPSHOT.jar]
2022-07-31 18:54:07,836 [http-nio-8080-exec-9] INFO  Deployment unit org.example:jbpm-base-example:1.0-SNAPSHOT stored successfully
```
- Step 4: visit other request, such as: http://localhost:8080/processdef/new?deploymentId=org.example:jbpm-base-example:1.0-SNAPSHOT&processId=com.sample.bpmn.hello

note: here used project: org.example:jbpm-base-example:1.0-SNAPSHOT, please mvn install it