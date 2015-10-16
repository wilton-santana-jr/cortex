
package Escalonador;

import Dao.RepositorioUsuariosBDR;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Consultorio
 */
//Tarefa escalonada todos os dias as 11:30 que verifica os usuarios que compraram
//tickets para o dia corrente e não usaram o ticket no dia corrente os usuarios são bloqueados
public class TarefaBloqueiaUsuario implements Job {

    public TarefaBloqueiaUsuario() {
    }
    
    
    

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        System.out.println("Início da Tarefa de Bloqueio de Alunos que compram tickets e não utilizam os mesmos! -----------------------------------------" );
        //Bloqueia alunos que adiquirirem tickets duas ou mais vezes e não utilizarem esses tickets
        RepositorioUsuariosBDR.bloqueiaAlunosQueAdiquiriremDoisOuMaisTicketsAndNaoUtilizaremOsTickets();
        //UsuarioDao.bloqueiaAlunosQueAdquiremTicketsAndNaoUtilizamOsTickets();
        System.out.println("Fim da Tarefa de Bloqueio de Alunos que compram tickets e não utilizam os mesmos! -----------------------------------------" );                
    }
    
}

