package app;

import java.lang.annotation.Annotation;
import java.util.Optional;

import app.modelo.entidade.Chamado;
import app.modelo.entidade.Tecnico;
import app.persistence.Repository;
import app.persistence.Tabela;
import app.persistence.TecnicoRepository;

public class App {
    
    public static void main(String[] args) {

        Tecnico t = new Tecnico();
        t.setCodigo(123L);
        t.setNome("Geraldo dos Santos");
        t.setAtivo(true);


        Repository<Tecnico, Long> repo = new Repository<>(Tecnico.class);  

        //repo.delete(123L);
        //repo.save(t);

        Optional<Tecnico> find = repo.findById(123L);

        System.out.println(find);



         /* 
         * Chamado c = new Chamado();
         * c.setAutor("Marcio");
         * 
         * Repository<Chamado> repoChamado = new Repository<>();
         * repoChamado.save(c); // insert into chamado (autor, etc) values (?, ?)
         * 
         * 
         * TecnicoRepository tecnicoRepository =
         * new TecnicoRepository();
         * //tecnicoRepository.save(t);
         * 
         * tecnicoRepository.findAll()
         * .forEach(tec ->
         * System.out.println(tec.getNome()));
         */
    }

}
