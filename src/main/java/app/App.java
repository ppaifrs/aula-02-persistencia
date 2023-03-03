package app;

import app.modelo.entidade.Tecnico;
import app.persistence.TecnicoRepository;

public class App {
    

    public static void main(String[] args) {

        Tecnico t = new Tecnico();
        t.setNome("Marcos Goulart");
        t.setAtivo(true);

        TecnicoRepository tecnicoRepository = 
            new TecnicoRepository();

        tecnicoRepository.save(t);

    }
}
