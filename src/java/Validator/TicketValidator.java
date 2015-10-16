package Validator;

import Interfaces.validator.ITicketValidator;
import Dao.RepositorioTicketsBDR;
import Dao.RepositorioTipoRefeicoesBDR;
import Dao.RepositorioUsuariosBDR;
import Interfaces.dao.IRepositorioTickets;
import Interfaces.dao.IRepositorioTipoRefeicoes;
import Interfaces.dao.IRepositorioUsuarios;
import Model.Autorizacao;
import Model.Ticket;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TicketValidator implements ITicketValidator {

    @Override
    public void onCreateTicket(Ticket ticket, IRepositorioTickets daoTicket, IRepositorioTipoRefeicoes daoTipoRefeicao, IRepositorioUsuarios daoUser) {
        


        verificaSeUsuarioLogadoTemPermissaoParaCadastrarTicket(daoUser, ticket);

        verificaSeTipoRefeicaoEscolhidoÉVálido(daoTipoRefeicao, ticket);

        verificaSeDataRefeicaoInformadaÉUmaDataAtualOuPosterioreValidaParaCadastroDoTicket(ticket);

        verificaSeTicketACadastrarJaExisteNoBanco(daoTicket, ticket);

        setaDataRefeicaoEmHorarioInicioEFimDaRefeicao(ticket);

        verificaSeHorarioInicioAlmocotEstaEntre11h00minE13h59min(ticket);

        verificaSeHorarioInicioJantarEstaEntre17h00minE18h59min(ticket);

        verificaSeHorarioFimAlmocotEstaEntre12h00minE15h59min(ticket);

        verificaSeHorarioFimJantartEstaEntre18h00minE20h59min(ticket);

        verificaSeHorarioInicioAlmocorÉMenorQueHorarioFim(ticket);

        verificaSeHorarioInicioJantarÉMenorQueHorarioFim(ticket);

        verificaPreenchimentoDoCardapio(ticket);

    }

    @Override
    public void onUpdateTicket(Ticket ticket, IRepositorioTickets daoTicket, IRepositorioTipoRefeicoes daoTipoRefeicao, IRepositorioUsuarios daoUser) {

        verificaSeOTicketJaFoiCompradoPorAlgumUsuario(ticket, daoTicket);


        //verificar se o operador do sistema que está logado possui a role: ROLE_TICKET
        //somente essses usuários poderão editar tickets
        verificaSeUsuarioLogadoTemPermissaoParaCadastrarTicket(daoUser, ticket);


        //verificar se o ticket a ser editado pertence a uma data atual ou posteriore somente tickets de hoje em diante poderão ser editados   
        verificaSeDataRefeicaoInformadaÉUmaDataAtualOuPosterioreValidaParaCadastroDoTicket(ticket);


        //verificar se o tipo de refeição escolhido é um tipo de refeição válida do banco de dados
        verificaSeTipoRefeicaoEscolhidoÉVálido(daoTipoRefeicao, ticket);

        //verificar  se a data da refeição é válida                
        //verificar se a data da refeição é uma data atual ou posteriore  pois somente poderá alterar a data dos tickets para data atual ou posteriore ou seja de hoje em diante
        verificaSeDataRefeicaoInformadaÉUmaDataAtualOuPosterioreValidaParaCadastroDoTicket(ticket);


        //setar as datas todas para o mesmo dia tanto em datarefeicao como em horarioinicial e horariofinal
        setaDataRefeicaoEmHorarioInicioEFimDaRefeicao(ticket);

        //verificar se horaInicioRefeicao possui uma hora válida e
        // se for almoço a hora de inicio do almoço tem que estar entre 11:00 horas e 13:59 horas
        verificaSeHorarioInicioAlmocotEstaEntre11h00minE13h59min(ticket);

        //verificar se horaInicioRefeicao possui uma hora válida e
        // se for jantar a hora de inicio do jantar tem que estar entre 17:00 horas e 18:59 horas  
        verificaSeHorarioInicioJantarEstaEntre17h00minE18h59min(ticket);

        //verificar se horaFimRefeicao possui uma hora válida     
        // se for almoço a hora de encerramento do almoço tem que estar entre 12:00 horas e 15:59 horas
        verificaSeHorarioFimAlmocotEstaEntre12h00minE15h59min(ticket);

        //verificar se horaFimRefeicao possui uma hora válida     
        // se for jantar a hora de encerramento do jantar tem que estar entre 18:00 horas e 20:59 horas                     
        verificaSeHorarioFimJantartEstaEntre18h00minE20h59min(ticket);

        //verificar ainda se o horario de inicio do almoço é menor que o horário de encerramento do almoço
        verificaSeHorarioInicioAlmocorÉMenorQueHorarioFim(ticket);

        //verificar ainda se o horario de inicio do jantar é menor que o horário de encerramento do jantar   
        verificaSeHorarioInicioJantarÉMenorQueHorarioFim(ticket);
        
        
        verificaPreenchimentoDoCardapio(ticket);
    }

    private void verificaSeHorarioInicioJantarÉMenorQueHorarioFim(Ticket ticket) throws RuntimeException {
        //verificar ainda se o horario de inicio do jantar é menor que o horário de encerramento do jantar    
        try {

            if (ticket.getTipoRefeicao().getAbreviacao().equalsIgnoreCase("J")
                    && !(ticket.getHoraInicioRefeicao().before(ticket.getHoraFimRefeicao()))) {
                throw new RuntimeException("Não foi possível criar o ticket! O horário de início dO jantar deve ser menor que o horário de encerramento do jantar.");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o ticket! O horário de início do jantar deve ser menor que o horário de encerramento do jantar.");
        }
    }

    private void verificaSeHorarioInicioAlmocorÉMenorQueHorarioFim(Ticket ticket) throws RuntimeException {
        //verificar ainda se o horario de inicio do almoço é menor que o horário de encerramento do almoço
        try {

            if (ticket.getTipoRefeicao().getAbreviacao().equalsIgnoreCase("A")
                    && !(ticket.getHoraInicioRefeicao().before(ticket.getHoraFimRefeicao()))) {
                throw new RuntimeException("Não foi possível criar o ticket! O horário de início do almoço deve ser menor que o horário de encerramento do almoço.");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o ticket! O horário de início do almoço deve ser menor que o horário de encerramento do almoço.");
        }
    }

    private void verificaSeHorarioFimJantartEstaEntre18h00minE20h59min(Ticket ticket) throws RuntimeException {
        //verificar se horaFimRefeicao possui uma hora válida     
        // se for jantar a hora de encerramento do jantar tem que estar entre 18:00 horas e 20:59 horas                
        try {

            if ((ticket.getTipoRefeicao().getAbreviacao().equalsIgnoreCase("J")
                    && !(ticket.getHoraFimRefeicao().getHours() >= 18
                    && ticket.getHoraFimRefeicao().getHours() <= 20
                    && ticket.getHoraFimRefeicao().getMinutes() >= 0
                    && ticket.getHoraFimRefeicao().getMinutes() <= 59))) {
                throw new RuntimeException("Não foi possível criar o ticket! Verifique o horário de encerramento do jantar informado. Ele deve estar entre 18h:00min e 20h:59min.");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o ticket! Verifique o horário de encerramento do jantar informado. Ele deve estar entre 18h:00min e 20h:59min.");
        }
    }

    private void verificaSeHorarioFimAlmocotEstaEntre12h00minE15h59min(Ticket ticket) throws RuntimeException {
        //verificar se horaFimRefeicao possui uma hora válida             
        // se for almoço a hora de encerramento do almoço tem que estar entre 12:00 horas e 15:59 horas
        try {

            if ((ticket.getTipoRefeicao().getAbreviacao().equalsIgnoreCase("A")
                    && !(ticket.getHoraFimRefeicao().getHours() >= 12
                    && ticket.getHoraFimRefeicao().getHours() <= 15
                    && ticket.getHoraFimRefeicao().getMinutes() >= 0
                    && ticket.getHoraFimRefeicao().getMinutes() <= 59))) {
                throw new RuntimeException("Não foi possível criar o ticket! Verifique o horário de encerramento do almoço informado. Ele deve estar entre 12h:00min e 15h:59min.");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o ticket! Verifique o horário de encerramento do almoço informado. Ele deve estar entre 12h:00min e 15h:59min.");
        }
    }

    private void verificaSeHorarioInicioJantarEstaEntre17h00minE18h59min(Ticket ticket) throws RuntimeException {
        //verificar se horaInicioRefeicao possui uma hora válida e
        // se for jantar a hora de inicio do jantar tem que estar entre 17:00 horas e 18:59 horas    
        try {

            if ((ticket.getTipoRefeicao().getAbreviacao().equalsIgnoreCase("J")
                    && !(ticket.getHoraInicioRefeicao().getHours() >= 17
                    && ticket.getHoraInicioRefeicao().getHours() <= 18
                    && ticket.getHoraInicioRefeicao().getMinutes() >= 0
                    && ticket.getHoraInicioRefeicao().getMinutes() <= 59))) {
                throw new RuntimeException("Não foi possível criar o ticket! Verifique o horário de início do jantar informado. Ele deve estar entre 17h:00min e 18h:59min.");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o ticket! Verifique o horário de início do jantar informado. Ele deve estar entre 17h:00min e 18h:59min.");
        }
    }

    private void verificaSeHorarioInicioAlmocotEstaEntre11h00minE13h59min(Ticket ticket) throws RuntimeException {
        //verificar se horaInicioRefeicao possui uma hora válida e
        // se for almoço a hora de inicio do almoço tem que estar entre 11:00 horas e 13:59 horas
        //senão estiver lançar uma excecao
        try {

            if ((ticket.getTipoRefeicao().getAbreviacao().equalsIgnoreCase("A")
                    && !(ticket.getHoraInicioRefeicao().getHours() >= 11
                    && ticket.getHoraInicioRefeicao().getHours() <= 13
                    && ticket.getHoraInicioRefeicao().getMinutes() >= 0
                    && ticket.getHoraInicioRefeicao().getMinutes() <= 59))) {
                throw new RuntimeException("Não foi possível criar o ticket! Verifique o horário de início do almoço informado. Ele deve estar entre 11h:00min e 13h:59min.");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o ticket! Verifique o horário de início do almoço informado. Ele deve estar entre 11h:00min e 13h:59min.");
        }
    }

    private void setaDataRefeicaoEmHorarioInicioEFimDaRefeicao(Ticket ticket) throws RuntimeException {
        //setar a dataRefeicao para os campos horarioInicioRefeicao e horarioFimRefeicao mantendo os horários das respectivas datas
        try {

            if (ticket.getHoraInicioRefeicao() != null && ticket.getHoraFimRefeicao() != null) {

                ticket.getHoraInicioRefeicao().setDate(ticket.getDataRefeicao().getDate());
                ticket.getHoraInicioRefeicao().setMonth(ticket.getDataRefeicao().getMonth());
                ticket.getHoraInicioRefeicao().setYear(ticket.getDataRefeicao().getYear());

                ticket.getHoraFimRefeicao().setDate(ticket.getDataRefeicao().getDate());
                ticket.getHoraFimRefeicao().setMonth(ticket.getDataRefeicao().getMonth());
                ticket.getHoraFimRefeicao().setYear(ticket.getDataRefeicao().getYear());

            } else {
                throw new RuntimeException("Não foi possível criar o ticket! Verifique se as informações dos horários de início e de encerramento da refeição foram informadas corretamente.");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o ticket! Verifique se as informações do ticket foram informadas corretamente.");
        }
    }

    private void verificaSeTicketACadastrarJaExisteNoBanco(IRepositorioTickets daoTicket, Ticket ticket) throws RuntimeException {
        //verificar se para a data da refeição informada  e o tipo de refeição informado  se existe outro ticket ja criado no banco
        //com a mesma data de refeição e tipo de refeicao informado se existir devere ser lançado uma exceção           
        try {
            if (daoTicket.existeNoBanco(ticket.getDataRefeicao(), ticket.getTipoRefeicao())) {
                throw new RuntimeException("Não foi possível criar o ticket pois o mesmo já encontra-se cadastrado no banco de dados! Localize-o e edite-o se quiser atualizar alguma informação referente ao ticket.");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o ticket pois o mesmo já encontra-se cadastrado no banco de dados! Localize-o e edite-o se quiser atualizar alguma informação referente ao ticket.");
        }
    }

    private void verificaSeDataRefeicaoInformadaÉUmaDataAtualOuPosterioreValidaParaCadastroDoTicket(Ticket ticket) throws RuntimeException {
        //verificar  se a data da refeição é válida
        //verificar se a data da refeição é uma data atual ou posteriore  pois somente poderá criar-se tickets da data de hoje em diante

        try {
            Calendar dataAtual = Calendar.getInstance();
            dataAtual.set(Calendar.HOUR_OF_DAY, ticket.getDataRefeicao().getHours());
            dataAtual.set(Calendar.MINUTE, ticket.getDataRefeicao().getMinutes());
            dataAtual.set(Calendar.SECOND, ticket.getDataRefeicao().getSeconds());

            String dataAtualToString = "";
            Date dataHoje = dataAtual.getTime();
            if (dataAtual != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                dataAtualToString = sdf.format(dataHoje);
                dataAtualToString = dataAtualToString + " (" + Utils.Utils.retornarDiaSemana(dataHoje) + ")";
            }

            boolean ticketDataRefeicaoFoiPreenchido = (ticket.getDataRefeicao() != null);
            boolean dataRefeicaoIgualAHoje = ticket.getDataRefeicaoToString().equals(dataAtualToString);
            boolean dataRefeicaoDepoisDeHoje = ticket.getDataRefeicao().after(dataHoje);
            if ((ticketDataRefeicaoFoiPreenchido
                    && !(dataRefeicaoIgualAHoje || dataRefeicaoDepoisDeHoje))) {
                throw new RuntimeException("Escolha uma data válida para inserção do ticket! A data deve ser uma data atual ou posteriore a data dos tickets já cadastrados.");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Escolha uma data válida para inserção do ticket! A data deve ser uma data atual ou posteriore a data dos tickets já cadastrados.");
        }
    }

    private void verificaSeTipoRefeicaoEscolhidoÉVálido(IRepositorioTipoRefeicoes daoTipoRefeicao, Ticket ticket) throws RuntimeException {
        //verificar se o tipo de refeição escolhido é um tipo de refeição válida do banco de dados
        try {

            if (!daoTipoRefeicao.isValida(ticket.getTipoRefeicao())) {
                throw new RuntimeException("Tipo de Refeição escolhido inválida! Escolha um tipo de refeição válida.");
            } else {
                //se for válida seta a refeição válida
                ticket.setTipoRefeicao(daoTipoRefeicao.getTipoRefeicao(ticket.getTipoRefeicao().getId()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Tipo de Refeição escolhida inválida! Escolha um tipo de refeição válida.");
        }
    }

    private void verificaSeUsuarioLogadoTemPermissaoParaCadastrarTicket(IRepositorioUsuarios daoUser, Ticket ticket) throws RuntimeException {
        //verificar se o operador do sistema que está logado possui a role: ROLE_TICKET
        //somente essses usuários poderão criar tickets para serem vendidos
        //SE USUARIO NÃO POSSUIR ESSA ROLE LANÇAR UMA EXCEÇÃO NA TELA DIZENDO QUE APENAS USUÁRIOS AUTORIZADOS COM A PERMISSÃO DE CRIAÇÃO
        // DE TÍCKETS PODEM CRIAR TICKETS    
        try {
            if (!daoUser.isUserPossuiRole(new Autorizacao("ROLE_TICKET"), ticket.getUsuarioOperador())) {
                throw new RuntimeException("Usuário não está autorizado a criar tickets no sistema."
                        + " Verifique as permissões com o administrador do sistema.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Usuário não está autorizado a criar tickets no sistema."
                    + " Verifique as permissões com o administrador do sistema.");
        }
    }

    private void verificaSeOTicketJaFoiCompradoPorAlgumUsuario(Ticket ticket, IRepositorioTickets daoTicket) throws RuntimeException {
        //verificar se o ticket ja foi comprado por alguém  se ja tiver sido comprado não deixar editar a data do ticket; 
        try {
            if (ticket != null && ticket.getId() != null && ticket.getId() != 0 && daoTicket.getTicket(ticket.getId()) != null) {
                if (daoTicket.verificaSeAlguemJaComprouTicket(ticket)) {
                    throw new RuntimeException("Não será possível editar o ticket pois o mesmo já foi comprado por algum usuário do refeitório!");
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Não será possível editar o ticket pois o mesmo já foi comprado por algum usuário do refeitório!");
        }
    }

    private void verificaPreenchimentoDoCardapio(Ticket ticket) throws RuntimeException {
        //verificar se a descrição do cardápio foi preenchido
        //se não preeencher lançar uma exception
        try {
            if (!(ticket != null && ticket.getCardapioRefeicao() != null
                    && !ticket.getCardapioRefeicao().isEmpty() && !ticket.getCardapioRefeicao().equalsIgnoreCase(""))) {
                throw new RuntimeException("Cardápio não preenchido. Preencha a descrição do cardápio da refeição.");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Cardápio não preenchido. Preencha a descrição do cardápio da refeição.");
        }
    }
}
