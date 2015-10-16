package Model;

import java.io.Serializable;

public class Mensagem implements Serializable{
    
    private String destino;
    private String titulo;
    private String mensagem;

    public Mensagem() {
    }

    public String getDestino() {
        return destino.trim();
    }

    public void setDestino(String destino) {
        this.destino = destino.trim();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
      
}
