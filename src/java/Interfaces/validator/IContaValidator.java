/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces.validator;

import Interfaces.dao.IRepositorioContas;
import Interfaces.dao.IRepositorioTipoOperacoes;
import Interfaces.dao.IRepositorioUsuarios;
import Model.RegistroOperacao;
import Model.Usuario;
import java.io.Serializable;

/**
 *
 * @author Consultorio
 */
public interface IContaValidator extends Serializable {

    void onCreditarBoletoByUsuario(Usuario usuarioOperador, Usuario usuarioSelecionado, String valorBoleto, RegistroOperacao registroOperacao, IRepositorioUsuarios usuarioDao, IRepositorioContas contaDao, IRepositorioTipoOperacoes tipoOperacaoDao);
    
}
