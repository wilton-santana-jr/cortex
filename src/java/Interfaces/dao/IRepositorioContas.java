/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces.dao;

import Model.Conta;
import Model.RegistroOperacao;
import Model.Usuario;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Consultorio
 */
public interface IRepositorioContas extends Serializable {

    boolean boletoFoiPago(String numeroBoleto);

    Conta getContaByUsuario(Usuario selectedUsuario, Session session);

    Conta getContaByUsuario(Usuario selectedUsuario);

    Conta getContaByUsuario(Session session, Usuario selectedUsuario);

    List<RegistroOperacao> getExtrato(Usuario usuarioLogado, Calendar dataInicio, Calendar dataFim);

    BigDecimal getSaldo(Usuario usuarioSelecionado);

    BigDecimal getSaldoByLogin(String login);

    //Verifica se conta de usuário existe se não tenta criar uma conta nova para o usuário selecionado
    boolean verificaContaDeUsuario(Usuario usuarioSelecionado);

    //Verifica se conta de usuário existe se não tenta criar uma conta nova para o usuário selecionado
    boolean verificaContaDeUsuario(Session session, Usuario usuarioSelecionado);
    
}
