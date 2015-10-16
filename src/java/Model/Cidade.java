package Model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "SEQ_GEN_CIDADE", sequenceName = "SEQ_GEN_CIDADE", allocationSize = 1)
public class Cidade implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_GEN_CIDADE")
    private  Long id;    
    
    @ManyToOne
    @JoinColumn(name = "estadoID", nullable = true, unique = false)
    private Estado estado;
    
    @Column(nullable = false)
    private String nome;

    public Cidade() {
        estado = (estado==null)?new Estado():estado;
    }

    public Cidade(Long id) {
        this.id = id;
    }
    
    

    public Cidade(Long estadoId, String nome) {        
        this.estado = new Estado(estadoId);
        this.nome = nome;
    }
    
    

    public Cidade(Estado estado, String nome) {
        this.estado = estado;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final Cidade other = (Cidade) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    
    
    
    
}
