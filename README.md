# Persistência

Dado persistente (meio persistente) se opõe ao dado transiente (dado só em memória volátil, ex banco transiente chave/valor (Redis)).

Vários meios de estrutura o meio persistente: arquivos (CSV, XML, TXT colunar, etc), banco de dados (relacional/sql, documentos (MongoDB), grafos (Neo4J), etc)

Banco Relacional (O/R M - Object/Relational Mapping):
- Mapear objetos para as tabelas, atributos para colunas,
e instâncias para os registros.

Como é estruturado/padronizado:

Padrões de Arquitetura:
- DAO: Data Access Object (Objeto de Acesso a Dados), (DAL Data Access Layer (Microsoft.NET))

Busca evitar isso:

```java
class AlunoService {

  void matricular(Aluno a, Disciplina d) {
    // lógica do negócio:
    if (a.getHistorico().contains(d)) {
      throw new MatriculaException("Aluno já cursou disciplina "+ d);
    }
    
    // .. várias regras
    
    // lógica da persistência:
    String sql = "INSERT INTO turma (id_aluno, id_disciplina) VALUES (...";
    // lógica para inserção: conexão banco, ...
  }
}
```

- Repositório (repository): semelhante ao DAO, mais genérico. Envolve interfaces de busca e critério.


Estrutura antiga (layered/em camadas)

Camada de Domínio -> Camada de Persistência

```java
class AlunoService {

    MatriculaRepository matriculaRepository;

    void matricular(Aluno a, Disciplina d) {
        // regra
        Matricula matricula; 

        matriculaRepository.save(matricula)
    }
}
// classe concreta
class MatriculaRepository {

}
```

Regra geral da camada: camadas de cima acessam as camadas de baixo.

Hoje, existe outro tipo de arquitetura: Onion, Hexagonal, Clean architecture. Não vê em camadas, mas em círculos cconcêntricos.


