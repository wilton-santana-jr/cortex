/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces.dao;

import Model.Estado;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Consultorio
 */
public interface IRepositorioEstados extends Serializable {

    List<Estado> list();
    
}
