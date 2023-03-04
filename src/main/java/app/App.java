package app;

import app.modelo.entidade.Chamado;
import app.modelo.entidade.Tecnico;
import app.persistence.Repository;
import app.persistence.TecnicoRepository;

public class App {
    

    public static void main(String[] args) {

        Tecnico t = new Tecnico();
        t.setNome("Geraldo dos Santos");
        t.setAtivo(false);

        Repository<Tecnico> repo = new Repository<>();

        repo.save(t);

        Chamado c = new Chamado();
        c.setAutor("Marcio");

        // Repository<Chamado> repoChamado = new Repository<>();
        // repoChamado.save(c); // insert into chamado (autor, etc) values (?, ?)


        TecnicoRepository tecnicoRepository = 
            new TecnicoRepository();
        //tecnicoRepository.save(t);

        tecnicoRepository.findAll()
            .forEach(tec -> 
            System.out.println(tec.getNome()));

    }

}
