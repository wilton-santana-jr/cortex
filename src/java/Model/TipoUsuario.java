
package Model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 *
 * @author Wilton
 */
@Entity
@SequenceGenerator(name = "SEQ_GEN_TIPO_USUARIO", sequenceName = "SEQ_GEN_TIPO_USUARIO", allocationSize = 1)
public class TipoUsuario  implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_GEN_TIPO_USUARIO")
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String descricao;
    
    @Column(nullable = false)
    private double valor; //VALOR DO TICKET A SER PAGO DE ACORDO COM O TIPO DO USUARIO

    public TipoUsuario() {
    }

    public TipoUsuario(Long id) {
        this.id = id;
    }
    
    

    public TipoUsuario(Long id, String descricao, double valor) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TipoUsuario other = (TipoUsuario) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TipoUsuario{" + "id=" + id + '}';
    }
    
    
    
    
    
    
}
