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
@SequenceGenerator(name = "SEQ_GEN_ESTADO", sequenceName = "SEQ_GEN_ESTADO", allocationSize = 1)
public class Estado implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_GEN_ESTADO")
    private  Long id;    
    
    @ManyToOne
    @JoinColumn(name = "paisID", nullable = true, unique = false)
    private Pais pais;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private String sigla;    

    public Estado() {
        pais = (pais==null)?new Pais():pais;
    }

    public Estado(Long id) {
        this.id = id;
    }
    
    

    public Estado(Long id, Long paisId, String sigla, String nome) {
        this.id = id;
        this.pais = new Pais(paisId);
        this.sigla = sigla;
        this.nome = nome;
        
    }
    
    

    public Estado(Pais pais, String nome, String sigla) {
        this.pais = pais;
        this.nome = nome;
        this.sigla = sigla;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final Estado other = (Estado) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    
    
    
    
}
