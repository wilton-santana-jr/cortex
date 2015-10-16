package Validator;

import Interfaces.validator.IVendaTicketValidator;
import Dao.RepositorioContasBDR;
import Dao.RepositorioTicketsBDR;
import Dao.RepositorioTipoOperacoesBDR;
import Dao.RepositorioUsuariosBDR;
import Interfaces.dao.IRepositorioContas;
import Interfaces.dao.IRepositorioTickets;
import Interfaces.dao.IRepositorioTipoOperacoes;
import Interfaces.dao.IRepositorioUsuarios;
import Model.Autorizacao;
import Model.RegistroOperacao;
import Model.TipoOperacao;
import Model.Usuario;
import Model.VendaTicket;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import org.hibernate.Session;

public class VendaTicketValidator implements IVendaTicketValidator {

    //Verificar se o usuario logado esta desistindo de um ticket para ele mesmo
    //verificar ainda se o ticket desistido é um ticket válido
    //ou seja se ele se encontra no momento da desistencia disponivel para desistencia
    //se não estiver disponivel para desistencia lançar uma exceção
    @Override
    public void onDesistirTicketByUsuarioLogadoInTransaction(Session session, Usuario selectedUsuario,
            VendaTicket vendaTicket, Usuario usuarioOperador, IRepositorioUsuarios usuarioDao, IRepositorioContas contaDao,
            IRepositorioTickets ticketDao, IRepositorioTipoOperacoes tipoOperacaoDao) {

        setaCamposBasicosDaVendaTicket(session, selectedUsuario, usuarioDao, vendaTicket,
                usuarioOperador, ticketDao, contaDao);

        //****************88
        //Se o usuario tiver 0 zero ticket não dará para ele decrementar tickets
        //So se ele tiver um ou dois
        //*******************                
        if (vendaTicket == null || (vendaTicket.getQuantidade().intValue() <= 0 && vendaTicket.getQuantidade().intValue() > 2)) {
            throw new RuntimeException("Erro ao desistir do ticket selecionado. Você ainda não comprou nenhum ticket para a refeição selecionada por isso não poderá desistir da mesma.");
        }

        //**************************
//        //Não poderá dessitir do ticket depois do tempo máximo de compra e venda do ticket se esgotar
//        ///****************************8
        try {

            if (!(ticketDao.estaNoPeriodoDeVenda(session, vendaTicket))) {
                throw new RuntimeException("Erro ao desistir de ticket selecionado. O tempo limite para desistência do ticket já foi atingido. O ticket"
                        + "só poderá ser cancelado até um hora e meia antes do início da refeição.");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao desistir de ticket selecionado. O tempo limite para desistência do ticket já foi atingido. O ticket"
                    + "só poderá ser cancelado até um hora e meia antes do início da refeição.");
        }

        //***********************************************************************************
        //Quando for feito a desistencia deverá incrementar a conta do usuario no valor do ticket desistido
        //Criar um novo registro de operacao do tipo de credito tipo 1 no valor do ticket para a data corrente
        // com o saldo anterior da conta
        //e decrementar a venda do ticket em 1 ticket
        //Se ocasionar na decrementação a quantidade de zero tickets o usuário dará um delete na linha que
        //representa a venda do ticket        
        //***********************************************************************************
        double valorTicket = vendaTicket.getUsuarioOperador().getTipoUsuario().getValor();

        RegistroOperacao regOperacaoDesistencia = new RegistroOperacao();
        regOperacaoDesistencia.setBoleto(null);
        regOperacaoDesistencia.setConta(vendaTicket.getContaComprador());
        regOperacaoDesistencia.setDataRegistro(Calendar.getInstance());
        regOperacaoDesistencia.setSaldoAnterior(vendaTicket.getContaComprador().getSaldo());
        regOperacaoDesistencia.setTipoOperacao(tipoOperacaoDao.getTipoOperacao(session, "C"));//Credito
        regOperacaoDesistencia.setUsuarioOperador(usuarioOperador);
        regOperacaoDesistencia.setValor(new BigDecimal(valorTicket));

        vendaTicket.getContaComprador().setSaldo(vendaTicket.getContaComprador().getSaldo().add(BigDecimal.valueOf(valorTicket)));
        vendaTicket.getContaComprador().setDataAtualizacao(regOperacaoDesistencia.getDataRegistro());

        vendaTicket.setQuantidade(vendaTicket.getQuantidade() - 1);
        vendaTicket.setRegistroOperacao(regOperacaoDesistencia);

        session.save(regOperacaoDesistencia);
        session.update(vendaTicket.getContaComprador());
        session.update(vendaTicket);

        if (vendaTicket.getQuantidade().intValue() == 0) {
            session.delete(vendaTicket);
        }

        verificaSeUsuarioLogadoEstaComprandoTicketParaSiMesmo(vendaTicket);

        verificaSeTicketEstaNoPeriodoDeVenda(session, ticketDao, vendaTicket);

        validaUsuarioCompradorTicket(session, usuarioDao, vendaTicket);

        setaStatusVendaTicketComoNaoUsado(vendaTicket);

    }

    //Verificar se o usuario logado esta comprando ticket para ele mesmo
    //verificar ainda se o ticket comprado é um ticket válido
    //ou seja se ele e encontra no momento da compra disponivel para venda
    //se não estiver disponivel para venda lançar uma exceção
    @Override
    public void onComprarTicketByUsuarioLogadoInTransaction(Session session, Usuario selectedUsuario,
            VendaTicket vendaTicket, Usuario usuarioOperador, IRepositorioUsuarios usuarioDao, IRepositorioContas contaDao,
            IRepositorioTickets ticketDao, IRepositorioTipoOperacoes tipoOperacaoDao) {

        setaCamposBasicosDaVendaTicket(session, selectedUsuario, usuarioDao, vendaTicket,
                usuarioOperador, ticketDao, contaDao);

        verificaSeUsuarioLogadoEstaComprandoTicketParaSiMesmo(vendaTicket);

        //verificaSeExisteSaldoSuficienteParaCompraDeTicketPeloUsuario(session,vendaTicket, tipoOperacaoDao);
        validaQuantidadeDeTicketsCompradosPeloUsuarioNaVenda(session, ticketDao, vendaTicket);

        verificaSeExisteSaldoSuficienteParaCompraDeTicketPeloUsuario(session, vendaTicket, tipoOperacaoDao);

        verificaSeTicketEstaNoPeriodoDeVenda(session, ticketDao, vendaTicket);

        validaUsuarioCompradorTicket(session, usuarioDao, vendaTicket);

        validaContaCompradorDoTicket(session, contaDao, vendaTicket);

        setaStatusVendaTicketComoNaoUsado(vendaTicket);

    }

    @Override
    public void onComprarTicketByUsuarioInTransaction(Session session, Usuario selectedUsuario, VendaTicket vendaTicket, Usuario usuarioOperador, RepositorioUsuariosBDR usuarioDao, RepositorioContasBDR contaDao, RepositorioTicketsBDR ticketDao, RepositorioTipoOperacoesBDR tipoOperacaoDao) {

        setaCamposBasicosDaVendaTicket(session, selectedUsuario, usuarioDao, vendaTicket, usuarioOperador, ticketDao, contaDao);

        verificaSeExisteSaldoSuficienteParaCompraDeTicketPeloUsuario(session, vendaTicket, tipoOperacaoDao);

        verificaSeUsuarioLogadoTemPermissaoParaVenderTicket(session, usuarioDao, vendaTicket);

        verificaSeUsuarioOperadorEstaVendendoTicketASiMesmo(vendaTicket);

        validaQuantidadeDeTicketsCompradosPeloUsuarioNaVenda(session, ticketDao, vendaTicket);

        verificaSeTicketEstaNoPeriodoDeVenda(session, ticketDao, vendaTicket);

        validaUsuarioCompradorTicket(session, usuarioDao, vendaTicket);

        validaContaCompradorDoTicket(session, contaDao, vendaTicket);

        setaStatusVendaTicketComoNaoUsado(vendaTicket);

    }

    private void verificaSeUsuarioLogadoTemPermissaoParaVenderTicket(Session session, RepositorioUsuariosBDR daoUser, VendaTicket vendaTicket) {

        //verificar se o usuario Logado possui a role lojinha somente usuarios
        //com essa role poderão vender ticket para os outros usuarios
        //verificar se o operador do sistema que está logado possui a role: ROLE_LOJINHA
        //somente essses usuários poderão vender tickets para  os usuários que se dirigirem a lojinha
        //SE USUARIO NÃO POSSUIR ESSA ROLE LANÇAR UMA EXCEÇÃO NA TELA DIZENDO QUE APENAS USUÁRIOS AUTORIZADOS COM A PERMISSÃO DE venda
        // DE TÍCKETS PODEM vender TICKETS            
        try {
            if (!daoUser.isUserPossuiRole(session, new Autorizacao("ROLE_LOJINHA"), vendaTicket.getUsuarioOperador())) {
                throw new RuntimeException("Usuário não está autorizado a vender tickets no sistema."
                        + " Verifique as permissões com o administrador do sistema.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Usuário não está autorizado a vender tickets no sistema."
                    + " Verifique as permissões com o administrador do sistema.");
        }
    }

    private void setaStatusVendaTicketComoNaoUsado(VendaTicket vendaTicket) throws RuntimeException {
        //verificar o o status esta false e é diferente de null 
        //pois a operação de compra de ticket deverá atribuir o ticket como sendo false
        //pois ele nunca foi usado                 
        try {

            if ((vendaTicket.isStatus() != false)) {
                vendaTicket.setStatus(false);
            }

        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro na venda do ticket. Aguarde um momento e tente novamente mais tarde.");
        }
    }

    private void validaContaCompradorDoTicket(Session session, IRepositorioContas contaDao, VendaTicket vendaTicket) throws RuntimeException {
        //verificar se conta do comprador e valida;
        try {

            boolean contaBdExiste = contaDao.verificaContaDeUsuario(session, vendaTicket.getContaComprador().getUsuario());

            if (!(contaBdExiste)) {
                throw new RuntimeException("A conta para o usuário selecionado é inválida. Selecione um usuário com conta válida para realizar a compra/venda do ticket.");
            }

        } catch (Exception ex) {
            throw new RuntimeException("A conta para o usuário selecionado é inválida. Selecione um usuário com conta válida para realizar a compra/venda do ticket.");
        }
    }

    private void validaUsuarioCompradorTicket(Session session, IRepositorioUsuarios usuarioDao, VendaTicket vendaTicket) throws RuntimeException {
        //verificar se o usuario selecionado para venda é um usuario válido;       
        try {
            Usuario userBd = usuarioDao.getUsuario(session, vendaTicket.getContaComprador().getUsuario().getUsername());

            if ((userBd == null)) {
                throw new RuntimeException("O usuário selecionado é inválido. Selecione um usuário válido para realizar a compra/venda do ticket.");
            }

        } catch (Exception ex) {
            throw new RuntimeException("O usuário selecionado é inválido. Selecione um usuário válido para realizar a compra/venda do ticket.");
        }
    }

    private void verificaSeTicketEstaNoPeriodoDeVenda(Session session, IRepositorioTickets ticketDao, VendaTicket vendaTicket) throws RuntimeException {
        //verificar se o ticket que está sendo comprado esta no periodo de venda ainda se não estiver apto a ser vendido
        //devera ser lancado um exception
        try {

            if (!(ticketDao.estaNoPeriodoDeVenda(session, vendaTicket))) {
                throw new RuntimeException("Infelizmente o ticket não poderá ser vendido pois o tempo de venda do ticket se esgotou.");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Infelizmente o ticket não poderá ser vendido pois o tempo de venda do ticket se esgotou.");
        }
    }

    private void validaQuantidadeDeTicketsCompradosPeloUsuarioNaVenda(Session session, IRepositorioTickets ticketDao, VendaTicket vendaTicket) throws RuntimeException {

        //verificar se a quantidade escolhida é maior que zero e menor igual a 2
        // se não for lancar uma exception
        try {

            //Se o usuario que for comprar ticket for um usuario do tipo aluno ele poderá comprar no máximo
            //1 ticket
            if (vendaTicket.getContaComprador().getUsuario().getTipoUsuario().getDescricao().equalsIgnoreCase("Aluno")) {
                if (!(vendaTicket.getQuantidade().intValue() >= 1 && vendaTicket.getQuantidade().intValue() <= 1)) {
                    throw new RuntimeException("Informe uma quantidade válida na solicitação/compra do ticket. O usuário poderá solicitar/comprar no mínimo 1(um) ticket e no máximo 1(um) ticket por refeição.");
                }
            } else {
                if (!(vendaTicket.getQuantidade().intValue() >= 1 && vendaTicket.getQuantidade().intValue() <= 2)) {
                    throw new RuntimeException("Informe uma quantidade válida na solicitação/compra do ticket. O usuário poderá solicitar/comprar no mínimo 1(um) ticket e no máximo 2(dois) tickets por refeição.");
                }
            }

        } catch (Exception ex) {
            throw new RuntimeException("Informe uma quantidade válida na solicitação/compra do ticket. O usuário poderá solicitar/comprar no mínimo 1(um) ticket e no máximo 2(dois) tickets por refeição.");
        }

        //verificar se o usuario Comprador ja comprou aquele ticket 
        //se ja comprou verificar a quantidade já comprada se for igual a 2
        //cancelar a venda dizendo que o limite de tickets para esta refeição ja foi atingido
        //se for menor que 2 igual a 1 incrementar a quantidade de tickets para 2;
        //verificar se a quantidade escolhida é maior que zero e menor igual a 2
        // se não for lancar uma exception
        String msgErro = "Informe uma quantidade válida de de tickets a serem comprados pelo usuário. A quantidade limite de tickets por refeição são 2 dois tickets (2 dois por refeição).";
        try {
            VendaTicket vendaTicketBd = ticketDao.getVendaTicket(session, vendaTicket);
            final boolean vendaTicketExisteNoBd = (vendaTicketBd != null);

            session.evict(vendaTicketBd);
            if (vendaTicketExisteNoBd) {

                if (vendaTicketBd.getQuantidade().intValue() > 0 && vendaTicketBd.getQuantidade().intValue() >= 2) {
                    msgErro = "Impossível solicitar/comprar mais tickets. Usuário já solicitou/comprou a quantidade limite de tickets (2 dois por refeição).";
                    throw new RuntimeException(msgErro);
                } else {

                    Integer quantidadeIncrementada = vendaTicket.getQuantidade() + vendaTicketBd.getQuantidade();

                    if (quantidadeIncrementada.intValue() > 2 || quantidadeIncrementada.intValue() <= 0) {
                        msgErro = "Impossível solicitar/comprar mais tickets. A quantidade limite de tickets solicitados/comprados por refeição deve ser no máximo (2 dois por refeição)."
                                + "Como o usuário já solicitou/comprou " + vendaTicketBd.getQuantidade().intValue() + " ticket para a refeição selecionada fica impossível solicitar/comprar mais "
                                + vendaTicket.getQuantidade().intValue() + " tickets para a refeição selecionada. Usuário só poderá solicitar/comprar no máximo mais " + (2 - vendaTicketBd.getQuantidade().intValue()) + " ticket.";
                        throw new RuntimeException(msgErro);
                    } else {
                        //Permitir o incremento da quantidade
                        vendaTicket.setQuantidade(quantidadeIncrementada);
                        vendaTicket.setId(vendaTicketBd.getId());
                        session.evict(vendaTicketBd);
                    }

                }

            }

        } catch (Exception ex) {
            throw new RuntimeException(msgErro);
        }
    }

    private void verificaSeUsuarioOperadorEstaVendendoTicketASiMesmo(VendaTicket vendaTicket) throws RuntimeException {
        //verificar se o usuario comprador é o mesmo 
        //usuario operador o usuario operador 
        //não poderá vender e comprar ticket para se mesmo
        //ao mesmo tempo
        try {

            Usuario operador = vendaTicket.getUsuarioOperador();
            Usuario comprador = vendaTicket.getContaComprador().getUsuario();

            if (operador.getUsername().equalsIgnoreCase(comprador.getUsername())) {
                throw new RuntimeException("Não será possível vender o ticket para você mesmo. Saia do sistema e entre com outra conta para solicitar/comprar ticket no seu nome!");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Não será possível vender o ticket para você mesmo. Saia do sistema e entre com outra conta para solicitar/comprar ticket no seu nome!");
        }
    }

    private void setaCamposBasicosDaVendaTicket(Session session, Usuario selectedUsuario,IRepositorioUsuarios usuarioDao, VendaTicket vendaTicket, Usuario usuarioOperador, IRepositorioTickets ticketDao, IRepositorioContas contaDao) {
        selectedUsuario = usuarioDao.getUsuario(session, selectedUsuario.getUsername());
        vendaTicket.setUsuarioOperador(usuarioOperador);//usuário responsável pelo venda do ticket ao usuario selecionado
        vendaTicket.setDataHoraCompra(Calendar.getInstance());
        vendaTicket.setStatus(false);
        vendaTicket.setTicketComprado(ticketDao.getTicket(session, vendaTicket.getTicketComprado().getId()));
        vendaTicket.setContaComprador(contaDao.getContaByUsuario(session, selectedUsuario));
    }

    private void verificaSeExisteSaldoSuficienteParaCompraDeTicketPeloUsuario(Session session, VendaTicket vendaTicket, IRepositorioTipoOperacoes tipoOperacaoDao) throws RuntimeException {
        //verificar se usuarioComprador possui credito suficiente em sua conta para realizar a operação de debito (comprar o ticket)
        //O preço do ticket deve ser menor ou igual ao saldo da conta do usuarioComprador
        //caso contrario o lancar excessao pedindo para o usuario pagar o gru e voltar a lojinha para recarga de créditos
        try {
            BigDecimal precoTicketUsuario = BigDecimal.valueOf(vendaTicket.getContaComprador().getUsuario().getTipoUsuario().getValor());
            BigDecimal saldoAtualContaUsuario = BigDecimal.valueOf(vendaTicket.getContaComprador().getSaldo().doubleValue());

            //Só serão validados os usuários que não forem alunos.
            if (!vendaTicket.getContaComprador().getUsuario().getTipoUsuario().getDescricao().equalsIgnoreCase("Aluno")) {

                final boolean nãoExisteSaldoSuficienteParaCompraDeTicket = !(precoTicketUsuario.compareTo(saldoAtualContaUsuario) <= 0);
                if (nãoExisteSaldoSuficienteParaCompraDeTicket) {
                    throw new RuntimeException("O usuário não possui crédito suficiente em sua conta para solicitar/comprar tickets. "
                            + "Avise ao usuário que gere um novo boleto GRU para pagamento no banco do brasil e volte na lojinha para recarregar seus créditos.");
                } else {

                //registra a nova operação de debito e atualiza o novo saldo da conta do usuario 
                    //depois da operação de débito
                    //obs a operação de registro deve vir antes da atualização do saldo do cliente
                    BigDecimal valorTicktesComprados = BigDecimal.valueOf(vendaTicket.getContaComprador().getUsuario().getTipoUsuario().getValor());
                    BigDecimal saldoAnterior = vendaTicket.getContaComprador().getSaldo();
                    TipoOperacao tipoOperacaoDebito = tipoOperacaoDao.getTipoOperacao(session, "D");

                    RegistroOperacao registroOperacao = new RegistroOperacao(vendaTicket.getContaComprador(), tipoOperacaoDebito, valorTicktesComprados, Calendar.getInstance(), null, saldoAnterior);
                    registroOperacao.setUsuarioOperador(vendaTicket.getUsuarioOperador());

                    vendaTicket.setRegistroOperacao(registroOperacao);

                    vendaTicket.getContaComprador().setSaldo(vendaTicket.getContaComprador().getSaldo().subtract(valorTicktesComprados)); //novo saldo com o desconto da quantidade de tickets comprados
                    vendaTicket.getContaComprador().setDataAtualizacao(Calendar.getInstance());

                }
            } else {
              //Se for aluno que está adiquirindo o ticket fazer o seguinte

                BigDecimal valorTicktesComprados = BigDecimal.valueOf(vendaTicket.getContaComprador().getUsuario().getTipoUsuario().getValor());
                BigDecimal saldoAnterior = vendaTicket.getContaComprador().getSaldo();
                TipoOperacao tipoOperacaoDebito = tipoOperacaoDao.getTipoOperacao(session, "D");

                RegistroOperacao registroOperacao = new RegistroOperacao(vendaTicket.getContaComprador(), tipoOperacaoDebito, valorTicktesComprados, Calendar.getInstance(), null, saldoAnterior);
                registroOperacao.setUsuarioOperador(vendaTicket.getUsuarioOperador());

                vendaTicket.setRegistroOperacao(registroOperacao);

                vendaTicket.getContaComprador().setSaldo(vendaTicket.getContaComprador().getSaldo().subtract(valorTicktesComprados)); //novo saldo com o desconto da quantidade de tickets comprados ficará zero pois usuario aluno é de graça os tickets
                vendaTicket.getContaComprador().setDataAtualizacao(Calendar.getInstance());

            }

        } catch (Exception ex) {
            throw new RuntimeException("O usuário não possui crédito suficiente em sua conta para solicitar/comprar tickets. "
                    + "Avise ao usuário que gere um novo boleto GRU para pagamento no banco do brasil e volte na lojinha para recarregar seus créditos.");
        }
    }

    @Override
    public void onUpdateStatusVendaTicket(VendaTicket vendaTicket, IRepositorioTickets ticketDao) {
        //falta fazer essa validação da atualização do status da venda do ticket selecionado
        //só poderá atualizar o status apartir de dez minutos antes do início da refeição ser servida
        //e até o final do horário da refeição ser servida.
        //Tenho que verificar se a horacorrente esta entre o horario inicial da refeicao-10 minutes e
        //o horario final da refeicao
        //se não estiver informar que o tempo para utilização do ticket se esgotou e o mesmo não poderá mais ser utilizado
        //você deveria ter utilizado o mesmo no período de horário da refeição
        if (!ticketDao.verificaSeVendaTicketEstaAptoAEntrarNoRefeitorioParaComer(vendaTicket)) {
            throw new RuntimeException("Seu ticket não está apto a ser utilizado no refeitório neste momento. Por conta disso não poderá ser atualizado o status do seu ticket. Aguarde o início do período da refeição ou se já passou o período de servir a refeição seu ticket não tem mais validade.");
        }
    }

    private void verificaSeUsuarioLogadoEstaComprandoTicketParaSiMesmo(VendaTicket vendaTicket) {
        //verificar se o usuario comprador é o mesmo 
        //usuario operador o usuario operador 
        //não poderá vender e comprar ticket para outro usuario só poderá comprar ticket para se mesmo
        //ao mesmo tempo
        try {

            Usuario operador = vendaTicket.getUsuarioOperador();
            Usuario comprador = vendaTicket.getContaComprador().getUsuario();

            if (!operador.getUsername().equalsIgnoreCase(comprador.getUsername())) {
                throw new RuntimeException("Você só poderá solicitar/comprar ticket para você mesmo. Saia do sistema e entre com sua conta para solicitar/comprar ticket no seu nome!");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Você só poderá solicitar/comprar ticket para você mesmo. Saia do sistema e entre com sua conta para solicitar/comprar ticket no seu nome!");
        }
    }

}
