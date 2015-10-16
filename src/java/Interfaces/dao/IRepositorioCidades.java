/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces.dao;

import Model.Cidade;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Consultorio
 */
public interface IRepositorioCidades extends Serializable {

    Cidade getCidade(Long id);

    List<Cidade> list();

    List<Cidade> listCidadesOfEstado(Long idEstado);
    
}
