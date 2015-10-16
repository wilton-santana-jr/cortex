/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "SEQ_GEN_TIPOREFEICAO", sequenceName = "SEQ_GEN_TIPOREFEICAO", allocationSize = 1)
public class TipoRefeicao implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_GEN_TIPOREFEICAO")
    private  Long id;    
    
    @Column(unique = true, nullable = false)
    private String nome; //Almoço / Jantar
    
    @Column(unique = true, nullable = false)
    private String abreviacao; //A-Almoço / J-Jantar

    public TipoRefeicao() {
    }

    public TipoRefeicao(Long id) {
        this.id = id;
    }
    
    

    public TipoRefeicao(Long id, String nome, String abreviacao) {
        this.id = id;
        this.nome = nome;
        this.abreviacao = abreviacao;
    }
    
    

    public TipoRefeicao(String nome, String abreviacao) {
        this.nome = nome;
        this.abreviacao = abreviacao;
    }

    

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getAbreviacao() {
        return abreviacao;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setAbreviacao(String abreviacao) {
        this.abreviacao = abreviacao;
    }
    
    
    
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final TipoRefeicao other = (TipoRefeicao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TipoRefeicao{" + "nome=" + nome + '}';
    }
     
    
    

    
    
}
