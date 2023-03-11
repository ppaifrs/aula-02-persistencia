package app.modelo.entidade;

import app.persistence.Chave;
import app.persistence.Tabela;

// Table: tecnico => Convention Over Configuration (CoC)
// Ter a possibilidade de Configuração

// POJO: Plain Old Java Objects (Velhos e simples objetos Java (sem estender ou implementar nada))
// POCO: Plain Old C# Objects ...

@Tabela(nome = "tecnicos")
public class Tecnico { // Entidade => precisa de uma identidade/chave primária
    
    @Chave
    private Long codigo; // UUID, Integer, String
    
    private Boolean ativo;
    
    // @Coluna(nome = "nome_completo")
    private String nome;

    // @Ignorar
    // private Boolean logado;

    // public Boolean getLogado() {
    //     return logado;
    // }

    // public void setLogado(Boolean logado) {
    //     this.logado = logado;
    // }
    
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return codigo + " " + nome;
    }
}
