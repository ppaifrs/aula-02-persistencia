package app.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import app.modelo.entidade.Tecnico;

public class TecnicoRepository {

    public void delete(Tecnico t) {
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:mochinho.db");

            PreparedStatement stmt = con.prepareStatement("DELETE FROM tecnico WHERE codigo = ?");

            stmt.setLong(1, t.getCodigo());

            stmt.execute();

            con.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    public void save(Tecnico t) {

        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:mochinho.db");

            PreparedStatement stmt = con.prepareStatement("INSERT INTO tecnico VALUES (? ,?)");

            if (t.getNome() == null) {
                stmt.setNull(1, Types.VARCHAR);
            } else {
                stmt.setString(1, t.getNome());
            }

            stmt.setBoolean(2, t.getAtivo());

            stmt.execute();

            con.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Tecnico> findAll() {
        List<Tecnico> lista = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:mochinho.db");

            PreparedStatement stmt = con.prepareStatement("SELECT nome, ativo FROM tecnico");

            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                Tecnico t = new Tecnico();
                t.setNome(result.getString("nome"));
                t.setAtivo(result.getBoolean("ativo"));
                lista.add(t);
            }

            con.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return lista;
    }
}
