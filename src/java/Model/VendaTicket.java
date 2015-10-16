
package Model;

import java.io.Serializable;
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
@SequenceGenerator(name = "SEQ_GEN_VENDATICKET", sequenceName = "SEQ_GEN_VENDATICKET", allocationSize = 1)
public class VendaTicket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_GEN_VENDATICKET")
    private Long id;        
    
    
    @ManyToOne
    @JoinColumn(name = "contaId", nullable = false, unique = false)
    private Conta contaComprador; //conta do usuario que comprou o ticket
    
    
    @ManyToOne
    @JoinColumn(name = "usuarioId", nullable = false, unique = false)
    private Usuario usuarioOperador; //usuario operador do sistema quem realizou o registro de venda do ticket;
    
    @ManyToOne
    @JoinColumn(name = "ticketId", nullable = false, unique = false)
    private Ticket ticketComprado; //ticket que o usuario comprou
    
    
    
    //representa a quantidade de tickets limitadas a 3 tickets por refeição
    //então pode ter no mínimo 1 e no máximo 3 tickets por venda registrada
    @Column(nullable = false, unique = false)
    private Integer quantidade; 
        
    
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar dataHoraCompra;
    
    
    //registra a operação de venda com valor total da venda dos tickets 
    //juntamente com o tipo de operacao no caso debito na conta do cliente
    //se o saldo da conta do cliente for menor que o preço total dos tickets a operação de venda dos tickets 
    //não será efetivada
    
    @ManyToOne
    @JoinColumn(name = "regOperacaoId", nullable = false, unique = false)
    private RegistroOperacao registroOperacao; 
    
    
    //true representa que a quantidade de ticket vendido foi usado -
    //false - representa que a quantidade de ticket vendido ainda não foi usado pelo consumidor
    @Column(name = "status", columnDefinition = "BOOLEAN",nullable = false)
    private boolean status; 

    public VendaTicket() {                        
        contaComprador = (contaComprador == null) ? new Conta() : contaComprador;                
        usuarioOperador = (usuarioOperador == null) ? new Usuario() : usuarioOperador;                
        ticketComprado = (ticketComprado == null) ? new Ticket() : ticketComprado;                
        registroOperacao = (registroOperacao == null) ? new RegistroOperacao() : registroOperacao;                
    }

    public VendaTicket(Long id, Conta contaComprador, Usuario usuarioOperador, Ticket ticketComprado, Integer quantidade, Calendar dataHoraCompra, RegistroOperacao registroOperacao, boolean status) {
        this.id = id;
        this.contaComprador = contaComprador;
        this.usuarioOperador = usuarioOperador;
        this.ticketComprado = ticketComprado;
        this.quantidade = quantidade;
        this.dataHoraCompra = dataHoraCompra;
        this.registroOperacao = registroOperacao;
        this.status = status;
    }
    
    

    public VendaTicket(Conta contaComprador, Usuario usuarioOperador, Ticket ticketComprado, Integer quantidade, Calendar dataHoraCompra, RegistroOperacao registroOperacao, boolean status) {
        this.contaComprador = contaComprador;
        this.usuarioOperador = usuarioOperador;
        this.ticketComprado = ticketComprado;
        this.quantidade = quantidade;
        this.dataHoraCompra = dataHoraCompra;
        this.registroOperacao = registroOperacao;
        this.status = status;
    }

    
    
  
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conta getContaComprador() {
        return contaComprador;
    }

    public void setContaComprador(Conta contaComprador) {
        this.contaComprador = contaComprador;
    }

    public Usuario getUsuarioOperador() {
        return usuarioOperador;
    }

    public void setUsuarioOperador(Usuario usuarioOperador) {
        this.usuarioOperador = usuarioOperador;
    }

    public Ticket getTicketComprado() {
        return ticketComprado;
    }

    public void setTicketComprado(Ticket ticketComprado) {
        this.ticketComprado = ticketComprado;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
    
    

    public Calendar getDataHoraCompra() {
        return dataHoraCompra;
    }

    public void setDataHoraCompra(Calendar dataHoraCompra) {
        dataHoraCompra.set(Calendar.SECOND, 0);
        dataHoraCompra.set(Calendar.MILLISECOND, 0);
        this.dataHoraCompra = dataHoraCompra;
    }

    public RegistroOperacao getRegistroOperacao() {
        return registroOperacao;
    }

    public void setRegistroOperacao(RegistroOperacao registroOperacao) {
        this.registroOperacao = registroOperacao;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public String getStatusTicket(){
        
        String status="";//F-Não Usado, T-Usado, E-Expirado       
        
        try{
                                    
        Date horarioInicio = ticketComprado.getHoraInicioRefeicao();
        
        Calendar calendarHorarioInicio = Calendar.getInstance();
        
        calendarHorarioInicio.setTime(horarioInicio);
        calendarHorarioInicio.add(Calendar.MINUTE, -10);
        
        
        Calendar horaCorrente = Calendar.getInstance();
        
        if(isStatus()==false && horaCorrente.getTime().compareTo(ticketComprado.getHoraFimRefeicao())>0)             
        return "E";//Ticket Expirou
        
        if(isStatus()==false && horaCorrente.getTime().compareTo(ticketComprado.getHoraFimRefeicao())<=0  
                &&
                (horaCorrente.getTime().compareTo(calendarHorarioInicio.getTime())>=0))             
        return "N";//Ticket Não Usado
        
        if(isStatus()==true && horaCorrente.getTime().compareTo(ticketComprado.getHoraFimRefeicao())<=0
                  &&
                (horaCorrente.getTime().compareTo(calendarHorarioInicio.getTime())>=0))     
        return "U";//Ticket Usado
        
        if(isStatus()==true)
            return "UF"; //Ticket Usado em seu estado final ou seja não poderá mais ser editado
        
        if(isStatus()==false)
            return "NF";//Ticket Não Usado em seu estado final ou seja não poderá mais ser editado
        
        
        }catch(Exception Ex){
            status="";
        }
        return status;
        
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final VendaTicket other = (VendaTicket) obj;

        
        if(!(this.id.longValue()==other.id.longValue())){
            return false;
        }
        
        return true;
    }


    
    

    
    
  
    
    
    
}
