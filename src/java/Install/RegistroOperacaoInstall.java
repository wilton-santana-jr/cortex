package Install;

import Model.Conta;
import Model.RegistroOperacao;
import java.math.BigDecimal;
import java.util.Calendar;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class RegistroOperacaoInstall {

    public RegistroOperacaoInstall(Session session) {
        Transaction t = session.beginTransaction();
        try {
            t.begin();
            instalarRegistrodeOperacoes(session);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar o cadastro. Aguarde um momento e tente novamente mais tarde.");
        } finally {
            session.close();
        }

    }

    
    //A inserção de um Registro de Operação Implica em atualizacao do saldo da conta;
    private void instalarRegistrodeOperacoes(Session session) throws HibernateException {
        System.out.println("\n ---- Início da Instalação de Registro de Operacoes para conta 1 de admin ---- \n");
                                
        ////////////////////////////////////////////////
        Long idContaOperacao1 = new Long(1);
        Long idTipoDeOperacao1 = new Long(1); //credito 1;
        BigDecimal valorOperacao1 = new BigDecimal(5);        
        Calendar dataOperacao1 = Calendar.getInstance();
        
        dataOperacao1.set(Calendar.DAY_OF_MONTH, -60);
        dataOperacao1.set(Calendar.HOUR_OF_DAY, 9);
        dataOperacao1.set(Calendar.MINUTE, 15);
        dataOperacao1.set(Calendar.SECOND, 30);
        dataOperacao1.set(Calendar.MILLISECOND, 0);
        String nboleto1 = "11111111111-1 11111111111-1 11111111111-1 11111111111-1";
        BigDecimal saldoAnteriorConta1Operacao1 = new BigDecimal(0);    
        String loginUsuarioOperadorOperacao1 = "lojinha";
        session.save(new RegistroOperacao(idContaOperacao1, idTipoDeOperacao1, valorOperacao1, dataOperacao1, nboleto1, saldoAnteriorConta1Operacao1,loginUsuarioOperadorOperacao1));                
        Conta contaOperacao1 = (Conta) session.get(Conta.class, idContaOperacao1);
        contaOperacao1.setSaldo(saldoAnteriorConta1Operacao1.add(valorOperacao1));
        session.update(contaOperacao1);

        ////////////////////////////////////////////////
        Long idContaOperacao2 = new Long(1);
        Long idTipoDeOperacao2 = new Long(1); //credito 1;
        BigDecimal valorOperacao2 = new BigDecimal(10);
        Calendar dataOperacao2 = Calendar.getInstance();        
        dataOperacao2.set(Calendar.DAY_OF_MONTH, -30);
        dataOperacao2.set(Calendar.HOUR_OF_DAY, 9);
        dataOperacao2.set(Calendar.MINUTE, 45);
        dataOperacao2.set(Calendar.SECOND, 33);
        dataOperacao2.set(Calendar.MILLISECOND, 0);
        String nboleto2 = "22222222222-2 22222222222-2 22222222222-2 22222222222-2";
        BigDecimal saldoAnteriorConta1Operacao2 = new BigDecimal(5);        
        String loginUsuarioOperadorOperacao2 = "lojinha";
        session.save(new RegistroOperacao(idContaOperacao2, idTipoDeOperacao2, valorOperacao2, dataOperacao2, nboleto2, saldoAnteriorConta1Operacao2,loginUsuarioOperadorOperacao2));       
        Conta contaOperacao2 = (Conta) session.get(Conta.class, idContaOperacao2);
        contaOperacao2.setSaldo(saldoAnteriorConta1Operacao2.add(valorOperacao2));
        session.update(contaOperacao2);
        
        ////////////////////////////////////////////////
        Long idContaOperacao3 = new Long(1);
        Long idTipoDeOperacao3 = new Long(1); //credito 1;
        BigDecimal valorOperacao3 = new BigDecimal(15);
        Calendar dataOperacao3 = Calendar.getInstance();        
        dataOperacao3.set(Calendar.DAY_OF_MONTH, -15);
        dataOperacao3.set(Calendar.HOUR_OF_DAY, 9);
        dataOperacao3.set(Calendar.MINUTE, 5);
        dataOperacao3.set(Calendar.SECOND, 45);
        dataOperacao3.set(Calendar.MILLISECOND, 0);
        String nboleto3 = "33333333333-3 33333333333-3 33333333333-3 33333333333-3";
        BigDecimal saldoAnteriorConta1Operacao3 = new BigDecimal(15);  
        String loginUsuarioOperadorOperacao3 = "lojinha";
        session.save(new RegistroOperacao(idContaOperacao3, idTipoDeOperacao3, valorOperacao3, dataOperacao3, nboleto3, saldoAnteriorConta1Operacao3,loginUsuarioOperadorOperacao3));
        Conta contaOperacao3 = (Conta) session.get(Conta.class, idContaOperacao3);
        contaOperacao3.setSaldo(saldoAnteriorConta1Operacao3.add(valorOperacao3));
        session.update(contaOperacao3);
        
        /////////////////////////////////////////////
        Long idContaOperacao4 = new Long(1);
        Long idTipoDeOperacao4 = new Long(2); //debito 2;
        BigDecimal valorOperacao4 = new BigDecimal(5);
        Calendar dataOperacao4 = Calendar.getInstance();        
        dataOperacao4.set(Calendar.DAY_OF_MONTH, -7);
        dataOperacao4.set(Calendar.HOUR_OF_DAY, 8);
        dataOperacao4.set(Calendar.MINUTE, 37);
        dataOperacao4.set(Calendar.SECOND, 13);
        dataOperacao4.set(Calendar.MILLISECOND, 0);        
        String nboleto4 = null; //POIS a operação e de debito
        BigDecimal saldoAnteriorConta1Operacao4 = new BigDecimal(30);   
        String loginUsuarioOperadorOperacao4 = "lojinha";
        session.save(new RegistroOperacao(idContaOperacao4, idTipoDeOperacao4, valorOperacao4, dataOperacao4, nboleto4, saldoAnteriorConta1Operacao4,loginUsuarioOperadorOperacao4));
        Conta contaOperacao4 = (Conta) session.get(Conta.class, idContaOperacao4);
        contaOperacao4.setSaldo(saldoAnteriorConta1Operacao4.subtract(valorOperacao4)); //subtrai pois é débito
        session.update(contaOperacao4);


        System.out.println(" \n --- Fim da  Instalação de Registro de Operacoes para a conta 1 de admin--- \n ");
    }
}
