/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces.dao;

import Model.Autorizacao;
import Model.Usuario;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Consultorio
 */
public interface IRepositorioUsuarios extends Serializable {

    //Cria a conta do usuario se ela ainda não existir
    //verificar se o usuário criado possui conta
    //se não tenta criar  uma nova conta para o usuário selecionado;
    boolean criaContaDoUsuario(Usuario usuario);

    Usuario getUsuario(String username);

    Usuario getUsuario(Session session, String username);

    Usuario getUsuarioByCPF(String cpf);

    Usuario getUsuarioByMatricula(String matricula);

    boolean isUserPossuiRole(Autorizacao role, Usuario usuario);

    boolean isUserPossuiRole(Session session, Autorizacao role, Usuario usuario);

    List<Usuario> list();

    //Retorna a lista de usuarios que possuem aas roles informadas em listaInAutorizacao
    // e que não possuem as roles informadas em listaNotInAutorizacao
    List<Usuario> listUsuariosComAutorizacoesNaoPermitidasAndAutorizacoesPermitidas(List<Autorizacao> listaNotInAutorizacao, List<Autorizacao> listaInAutorizacao);

    void save(Usuario usuario);

    void savePreCadastro(Usuario usuario);

    void update(Usuario usuario);
    
}
