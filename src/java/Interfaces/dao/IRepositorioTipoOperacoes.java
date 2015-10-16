/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces.dao;

import Model.TipoOperacao;
import java.io.Serializable;
import org.hibernate.Session;

/**
 *
 * @author Consultorio
 */
public interface IRepositorioTipoOperacoes extends Serializable {

    /**
     *
     * @param tipoOperacao C- Crédito / D- Débito
     * @return
     */
    TipoOperacao getTipoOperacao(String tipoOperacao);

    /**
     *
     * @param tipoOperacao C- Crédito / D- Débito
     * @return
     */
    TipoOperacao getTipoOperacao(Session session, String tipoOperacao);
    
}
