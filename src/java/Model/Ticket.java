package Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;

@Entity
@SequenceGenerator(name = "SEQ_GEN_TICKET", sequenceName = "SEQ_GEN_TICKET", allocationSize = 1)
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_GEN_TICKET")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false, unique = false)
    private Usuario usuarioOperador; //usuario operador do sistema quem criou o ticket a venda; Quem está logado com permissão ROLE_TICKET
    @ManyToOne
    @JoinColumn(name = "tipoRefeicaoId", nullable = false, unique = false)
    private TipoRefeicao tipoRefeicao; //usuario operador do sistema que criou o ticket a venda;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataRefeicao; //guarda o dia da refeição e a hora da degustação da mesma
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date horaInicioRefeicao; //guarda o dia da refeição e a hora da degustação da mesma
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date horaFimRefeicao; //guarda o dia da refeição e a hora da degustação da mesma

    
    @Column(nullable = false)
    private String cardapioRefeicao; 
    
    public Ticket() {
        tipoRefeicao = (tipoRefeicao == null) ? new TipoRefeicao() : tipoRefeicao;
        usuarioOperador = (usuarioOperador == null) ? new Usuario() : usuarioOperador;
    }

    public Ticket(Long id, Usuario usuarioOperador, TipoRefeicao tipoRefeicao, Date dataRefeicao, Date horaInicioRefeicao, Date horaFimRefeicao) {
        this.id = id;
        this.usuarioOperador = usuarioOperador;
        this.tipoRefeicao = tipoRefeicao;
        this.dataRefeicao = dataRefeicao;
        this.horaInicioRefeicao = horaInicioRefeicao;
        this.horaFimRefeicao = horaFimRefeicao;
    }

    public Ticket(Usuario usuarioOperador, TipoRefeicao tipoRefeicao, Date dataRefeicao, Date horaInicioRefeicao, Date horaFimRefeicao) {
        this.usuarioOperador = usuarioOperador;
        this.tipoRefeicao = tipoRefeicao;
        this.dataRefeicao = dataRefeicao;
        this.horaInicioRefeicao = horaInicioRefeicao;
        this.horaFimRefeicao = horaFimRefeicao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuarioOperador() {
        return usuarioOperador;
    }

    public void setUsuarioOperador(Usuario usuarioOperador) {
        this.usuarioOperador = usuarioOperador;
    }

    public TipoRefeicao getTipoRefeicao() {
        return tipoRefeicao;
    }

    public void setTipoRefeicao(TipoRefeicao tipoRefeicao) {
        this.tipoRefeicao = tipoRefeicao;
    }

    public Date getDataRefeicao() {
        return dataRefeicao;
    }

    public void setDataRefeicao(Date dataRefeicao) {
        this.dataRefeicao = dataRefeicao;
    }

    public String getDataRefeicaoToString() {
        String dataFormatada = "";
        if (dataRefeicao != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date data = dataRefeicao;
            dataFormatada = sdf.format(data);            
            dataFormatada=dataFormatada+" ("+Utils.Utils.retornarDiaSemana(data)+")";
        }
        return dataFormatada;
    }
    
    
    
     public String getDataRefeicaoSemana() {
        String dataFormatada = "";
        if (dataRefeicao != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date data = dataRefeicao;
            dataFormatada = sdf.format(data);            
            dataFormatada=dataFormatada+" ("+Utils.Utils.retornarDiaSemana(data)+")";
        }
        return dataFormatada;
    }
    
    public String getDataRefeicaoFormatada() {
        String dataFormatada = "";
        if (dataRefeicao != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date data = dataRefeicao;
            dataFormatada = sdf.format(data);                       
        }
        return dataFormatada;
    }
    
     public String getHorarioFuncionamento() {
        String horarioFuncionamentoFormatado = "";
        if (horaInicioRefeicao != null && horaFimRefeicao != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date horarioInicio = horaInicioRefeicao;
            Date horarioFim = horaFimRefeicao;
            horarioFuncionamentoFormatado = "de "+sdf.format(horarioInicio)+" às "+sdf.format(horarioFim);                       
        }
        return horarioFuncionamentoFormatado;
    }
     
     
    public String getDescricao(){
        return getDataRefeicaoSemana()+" "+tipoRefeicao.getNome()+" - "+ this.getCardapioRefeicao();
    }
    
 

    public Date getHoraInicioRefeicao() {
        return horaInicioRefeicao;
    }

    public void setHoraInicioRefeicao(Date horaInicioRefeicao) {
        this.horaInicioRefeicao = horaInicioRefeicao;
    }

    public Date getHoraFimRefeicao() {
        return horaFimRefeicao;
    }

    public void setHoraFimRefeicao(Date horaFimRefeicao) {
        this.horaFimRefeicao = horaFimRefeicao;
    }

    public String getCardapioRefeicao() {
        return cardapioRefeicao;
    }

    public void setCardapioRefeicao(String cardapioRefeicao) {
        this.cardapioRefeicao = cardapioRefeicao;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.id);
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
        final Ticket other = (Ticket) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
       
    
    
    //Metodo usado para verificar se o usuario pode comprar mais ticket
    public Boolean getStatusQuantidadeComprar(Integer quantidade, String descTipoUsuario){
        
        Boolean status=false;//P-Permitido, N-Não Permitido      
        
        try{
             if(descTipoUsuario.equalsIgnoreCase("Aluno")){
                if(quantidade<=0)
                    status=true; 
             }else{
                if(quantidade<=1)
                    status=true;
             }
           
        
        }catch(Exception Ex){
            status=false;
        }
        return status;
        
    }
    
    //Metodo usado para verificar se o usuario pode desistir de mais ticket
    public Boolean getStatusQuantidadeDesistir(Integer quantidade,String descTipoUsuario){
        
        Boolean status=false;//P-Permitido, N-Não Permitido      
        
        try{
            if(descTipoUsuario.equalsIgnoreCase("Aluno")){
                if(quantidade>=1&&quantidade<=1)
                    status=true;    
            }else{
            if(quantidade>=1&&quantidade<=2)
                status=true;
            }
           
        
        }catch(Exception Ex){
            status=false;
        }
        return status;
        
    }
    
    

    @Override
    public String toString() {
        return tipoRefeicao.getNome()+" Dia: "+getDataRefeicaoToString()+" Cardápio: "+this.getCardapioRefeicao();
        
    }
}
