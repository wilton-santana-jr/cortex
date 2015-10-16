/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Escalonador;

import Dao.RepositorioUsuariosBDR;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author Consultorio
 */
public class QuartzTest {
    
      public static void main(String[] args) {

        try {
            
            
           RepositorioUsuariosBDR.bloqueiaAlunosQueAdquiremTicketsAndNaoUtilizamOsTickets();
            
            // Grab the Scheduler instance from the Factory 
            System.out.println("Inicializando o listener de bloqueio de usuários!-------------------------");
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            
            
            
            
            
       
        
       

       
       
         // define the job and tie it to our HelloJob class
          JobDetail jobBloqueiaUsuarios17_00 = JobBuilder.newJob(TarefaBloqueiaUsuario.class)
            .withIdentity("-TarefaBloqueiaUsuarios17_00", "-grupoCortexBloqueioUsuario")
            .build();
          
          JobDetail jobBloqueiaUsuarios17_05 = JobBuilder.newJob(TarefaBloqueiaUsuario.class)
            .withIdentity("-TarefaBloqueiaUsuarios17_05", "-grupoCortexBloqueioUsuario")
            .build();


            
            
            Trigger  triggerBloqueiaUsuarios17_00 = newTrigger()
                                    .withIdentity("-DisparaTarefaBloqueioUsuarios17:00", "-grupoCortexBloqueioUsuario")
                                    .withSchedule(cronSchedule("0 10 17 * * ?"))
                                    .forJob(jobBloqueiaUsuarios17_00)
                                    .build();
            
            
            Trigger  triggerBloqueiaUsuarios17_05 = newTrigger()
                                    .withIdentity("-DisparaTarefaBloqueioUsuarios17:05", "-grupoCortexBloqueioUsuario")
                                    .withSchedule(cronSchedule("0 15 17 * * ?"))
                                    .forJob(jobBloqueiaUsuarios17_05)
                                    .build();
            
            
            
       
        // Tell quartz to schedule the job using our trigger
        scheduler.scheduleJob(jobBloqueiaUsuarios17_00, triggerBloqueiaUsuarios17_00);        
        scheduler.scheduleJob(jobBloqueiaUsuarios17_05, triggerBloqueiaUsuarios17_05);
            
            
            
            
//        System.out.println("Finalizando o listener de bloqueio de usuários!-------------------------");
//            scheduler.shutdown();    
            

        } catch (SchedulerException se) {
            se.printStackTrace();
        }
        
        
        
    }
    
    
}
