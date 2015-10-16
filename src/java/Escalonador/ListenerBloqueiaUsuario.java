/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Escalonador;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.quartz.JobBuilder;

import org.quartz.JobDetail;


import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;


import static org.quartz.SimpleScheduleBuilder.*;


import org.quartz.impl.StdSchedulerFactory;


import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.CalendarIntervalScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.DateBuilder.*;


/**
 *
 * Bloquear usuarios que compraram dois ou mais tickets e nao usaram;
 * @author Consultorio
 */
public class ListenerBloqueiaUsuario implements ServletContextListener {
    
    Scheduler scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
       
        System.out.println("-------------------Inicializando o listener de bloqueio de usuários!-------------------");
       
        scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
       
        
       JobDetail jobBloqueiaUsuarios14_30 = JobBuilder.newJob(TarefaBloqueiaUsuario.class)
        .withIdentity("TarefaBloqueiaUsuarios14_30", "grupoCortexBloqueioUsuario")
        .build();
       
       JobDetail jobBloqueiaUsuarios22_30 = JobBuilder.newJob(TarefaBloqueiaUsuario.class)
        .withIdentity("TarefaBloqueiaUsuarios22_30", "grupoCortexBloqueioUsuario")
        .build();
          
            
            Trigger  triggerBloqueiaUsuarios14_30 = newTrigger()
                                    .withIdentity("DisparaTarefaBloqueioUsuarios14:30", "grupoCortexBloqueioUsuario")
                                    .withSchedule(cronSchedule("0 30 14 * * ?"))
                                    .forJob(jobBloqueiaUsuarios14_30)
                                    .build();
            
            
            Trigger  triggerBloqueiaUsuarios22_30 = newTrigger()
                                    .withIdentity("DisparaTarefaBloqueioUsuarios22:30", "grupoCortexBloqueioUsuario")
                                    .withSchedule(cronSchedule("0 30 22 * * ?"))
                                    .forJob(jobBloqueiaUsuarios22_30)
                                    .build();
            
            
            
        scheduler.scheduleJob(jobBloqueiaUsuarios14_30, triggerBloqueiaUsuarios14_30);        
        scheduler.scheduleJob(jobBloqueiaUsuarios22_30, triggerBloqueiaUsuarios22_30);
        
                       
        } catch (Exception e) {
            e.printStackTrace();            
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            System.out.println("------------------Finalizando listener de bloqueio de usuários!-------------------------------");
            scheduler.shutdown();
        } catch (Exception e) {
            e.printStackTrace();            
        }
    }
}
