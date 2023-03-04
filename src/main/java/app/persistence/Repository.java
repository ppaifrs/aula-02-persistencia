package app.persistence;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Repository<Chamado> repoChamado;
// Repository<Tecnico> repoTecnico;

public class Repository<T> { // parâmetro de tipo <T>

    public void save(T o) {

        try {
            // hard-coded (config estática no código) TODO: var ambiente
            Connection con = DriverManager.getConnection("jdbc:sqlite:mochinho.db"); 

            Field[] fields = o.getClass().getDeclaredFields();

            String var = Stream.of(fields) //
                .map(f -> "?") // [?, ? ,?]
                .collect(Collectors.joining(",")); // "?, ?, ?""

            String columns = Stream.of(fields)
                .map(Field::getName) // nome, ativo, etc
                .collect(Collectors.joining(",")); // nome, ativo

            String insert = String.format(
                "INSERT INTO tecnico (%s) VALUES (%s)", columns, var);

            System.out.println(insert);

            PreparedStatement stmt = con.prepareStatement(insert);
            int i = 1;
            for (Field f : fields) {
                f.setAccessible(true);
                if (String.class.equals(f.getType())) {
                    String valor = (String) f.get(o);                    
                    if (valor == null) {
                        stmt.setNull(i, Types.VARCHAR);
                    } else {
                        stmt.setString(i, valor);
                    }
                }

                if (Boolean.class.equals(f.getType())) {
                    Boolean valor = (Boolean) f.get(o);
                    if (valor == null) {
                        stmt.setNull(i, Types.BOOLEAN);
                    } else {
                        stmt.setBoolean(i, valor);
                    }
                }

                i++;
            }

            stmt.execute();

            con.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    // public void save(T o) {

    //     try {
    //         // hard-coded (config estática no código) TODO: var ambiente
    //         Connection con = DriverManager.getConnection("jdbc:sqlite:mochinho.db");

    //         Field[] fields = o.getClass().getDeclaredFields();

    //         String var = Stream.of(fields) //
    //                 .map(f -> "?") // [?, ? ,?]
    //                 .collect(Collectors.joining(",")); // "?, ?, ?""

    //         String columns = Stream.of(fields)
    //                 .map(Field::getName) // nome, ativo, etc
    //                 .collect(Collectors.joining(",")); // nome, ativo

    //         String insert = String.format(
    //                 "INSERT INTO tecnico (%s) VALUES (%s)", columns, var);

    //         System.out.println(insert);

    //         PreparedStatement stmt = con.prepareStatement(insert);
    //         int i = 1;
    //         for (Field f : fields) {
    //             f.setAccessible(true);

    //             Object valorObjeto = f.get(o);

    //             if (valorObjeto instanceof String) {
    //                 String valor = (String) valorObjeto;
    //                 if (valor == null) {
    //                     stmt.setNull(i, Types.VARCHAR);
    //                 } else {
    //                     stmt.setString(i, valor);
    //                 }
    //             }

    //             if (valorObjeto instanceof Boolean) {
    //                 Boolean valor = (Boolean) valorObjeto;
    //                 if (valor == null) {
    //                     stmt.setNull(i, Types.BOOLEAN);
    //                 } else {
    //                     stmt.setBoolean(i, valor);
    //                 }
    //             }

    //             i++;
    //         }

    //         stmt.execute();

    //         con.close();
    //     } catch (Exception e) {
    //         System.err.println(e.getMessage());
    //     }
    // }
}
