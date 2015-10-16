/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces.dao;

import Model.Autorizacao;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Consultorio
 */
public interface IRepositorioAutorizacoes extends Serializable {

    List<Autorizacao> list();

    List<Autorizacao> list(String whereRolesName);
    
}
