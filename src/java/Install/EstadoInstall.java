package Install;

import Model.Estado;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class EstadoInstall {

    public EstadoInstall(Session session) {
        Transaction t = session.beginTransaction();
        try {
            t.begin();
            instalarEstados(session);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            throw new RuntimeException("Ocorreu um erro ao realizar o cadastro. Aguarde um momento e tente novamente mais tarde.");
            
        } finally {
            session.close();
        }

    }

    private void instalarEstados(Session session) throws HibernateException {
        System.out.println(" \n --- Inicío da  Instalação de estados --- \n ");
        
            session.save(new Estado(new Long(1), new Long(1), "AC", "Acre"));            
            session.save(new Estado(new Long(2), new Long(1), "AL", "Alagoas"));
            session.save(new Estado(new Long(3), new Long(1), "AP", "Amapá"));
            session.save(new Estado(new Long(4), new Long(1), "AM", "Amazonas"));
            session.save(new Estado(new Long(5), new Long(1), "BA", "Bahia"));
            session.save(new Estado(new Long(6), new Long(1), "CE", "Ceará"));
            session.save(new Estado(new Long(7), new Long(1), "DF", "Distrito Federal"));
            session.save(new Estado(new Long(8), new Long(1), "ES", "Espírito Santo"));
            session.save(new Estado(new Long(9), new Long(1), "GO", "Goiás"));
            session.save(new Estado(new Long(10), new Long(1), "MA", "Maranhão"));
            session.save(new Estado(new Long(11), new Long(1), "MT", "Mato Grosso"));
            session.save(new Estado(new Long(12), new Long(1), "MS", "Mato Grosso do Sul"));
            session.save(new Estado(new Long(13), new Long(1), "MG", "Minas Gerais"));
            session.save(new Estado(new Long(14), new Long(1), "PA", "Pará"));
            session.save(new Estado(new Long(15), new Long(1), "PB", "Paraíba"));
            session.save(new Estado(new Long(16), new Long(1), "PR", "Paraná"));
            session.save(new Estado(new Long(17), new Long(1), "PE", "Pernambuco"));
            session.save(new Estado(new Long(18), new Long(1), "PI", "Piauí"));
            session.save(new Estado(new Long(19), new Long(1), "RJ", "Rio de Janeiro"));
            session.save(new Estado(new Long(20), new Long(1), "RN", "Rio Grande do Norte"));
            session.save(new Estado(new Long(21), new Long(1), "RS", "Rio Grande do Sul"));
            session.save(new Estado(new Long(22), new Long(1), "RO", "Rondônia"));
            session.save(new Estado(new Long(23), new Long(1), "RR", "Roraima"));
            session.save(new Estado(new Long(24), new Long(1), "SC", "Santa Catarina"));
            session.save(new Estado(new Long(25), new Long(1), "SP", "São Paulo"));
            session.save(new Estado(new Long(26), new Long(1), "SE", "Sergipe"));
            session.save(new Estado(new Long(27), new Long(1), "TO", "Tocantins"));
            
        System.out.println(" \n --- Fim da  Instalação de estados --- \n ");  
    }
    
    
}
