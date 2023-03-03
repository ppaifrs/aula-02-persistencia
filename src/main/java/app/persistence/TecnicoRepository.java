package app.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import app.modelo.entidade.Tecnico;

public class TecnicoRepository {
    
    public void save(Tecnico t) {

        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:mochinho.db");

            PreparedStatement stmt = con.prepareStatement("INSERT INTO tecnico VALUES (? ,?)");

            stmt.setString(1, t.getNome());
            stmt.setBoolean(2, t.getAtivo());

            stmt.execute();

            con.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
