/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces.validator;

import Interfaces.dao.IRepositorioAutorizacoes;
import Interfaces.dao.IRepositorioCidades;
import Interfaces.dao.IRepositorioTipoUsuarios;
import Interfaces.dao.IRepositorioUsuarios;
import Model.Usuario;
import java.io.Serializable;

/**
 *
 * @author Consultorio
 */
public interface IUsuarioValidator extends Serializable {

    void onCreatePreCadatro(Usuario usuario, IRepositorioUsuarios daoUser, IRepositorioCidades daoCidade, IRepositorioTipoUsuarios daoTipoUsuario);

    void onCreateUsuario(Usuario usuario, IRepositorioUsuarios daoUser, IRepositorioCidades daoCidade, IRepositorioTipoUsuarios daoTipoUsuario, IRepositorioAutorizacoes daoAutorizacao);

    void onCreateUsuarioPelaLojinha(Usuario usuario, IRepositorioUsuarios daoUser, IRepositorioCidades daoCidade, IRepositorioTipoUsuarios daoTipoUsuario, IRepositorioAutorizacoes daoAutorizacao);

    void onUpdateUsuario(Usuario usuarioLogado, Usuario usuarioEditado, IRepositorioUsuarios daoUser, IRepositorioCidades daoCidade, IRepositorioTipoUsuarios daoTipoUsuario, IRepositorioAutorizacoes daoAutorizacao);

    void onUpdateUsuarioAtravesDaLojinha(Usuario usuarioLogado, Usuario usuarioEditado, IRepositorioUsuarios daoUser, IRepositorioCidades daoCidade, IRepositorioTipoUsuarios daoTipoUsuario, IRepositorioAutorizacoes daoAutorizacao);
    
}
