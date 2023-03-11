package app.persistence;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Repository<Chamado> repoChamado;
// Repository<Tecnico> repoTecnico;

// Metadata => Meta-dados => Dados sobre os dados.

public class Repository<T, K> { // parâmetro de tipo <T>, "K" de Key

    private Class<T> clazz;

    public Repository(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Optional<T> findById(K id) { // e se não encontrar? NotFoundException, null, Optional
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:mochinho.db");

            final String tabela;
            final Field pk = Arrays.stream(clazz.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(Chave.class))
                    .findFirst()
                    .orElse(clazz.getDeclaredFields()[0]);

            if (clazz.isAnnotationPresent(Tabela.class)) {
                tabela = ((Tabela) clazz.getAnnotation(Tabela.class)).nome();
            } else {
                tabela = clazz.getSimpleName().toLowerCase();
            }

            String colunas = Stream.of(clazz.getDeclaredFields())
                    .map(Field::getName)
                    .collect(Collectors.joining(",")); // codigo, nome, ativo

            String sql = String.format("SELECT %s FROM %s WHERE %s = ?",
                colunas, tabela, pk.getName());

            System.out.println(sql); // TODO: substituir por logging

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setObject(1, id);
            
            ResultSet result = stmt.executeQuery();

            final T objeto;

            if (result.next()) {
                objeto = clazz.getDeclaredConstructor().newInstance();

                for (String campo : colunas.split(",")) { // TODO: desfazer gambi!
                    Field f = clazz.getDeclaredField(campo);
                    f.setAccessible(true);
                    
                    if (Long.class.equals(f.getType())) {
                        f.set(objeto, result.getLong(campo));
                    } else if (Boolean.class.equals(f.getType())) {
                        f.set(objeto, result.getBoolean(campo));
                    } else if (String.class.equals(f.getType())) {
                        f.set(objeto, result.getString(campo));
                    } else {
                        f.set(objeto, result.getObject(campo));
                    }
                }
            } else {
                objeto = null;
            }

            con.close();

            return Optional.ofNullable(objeto);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void delete(K id) {
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:mochinho.db");

            final String tabela;
            final Field pk;

            if (clazz.isAnnotationPresent(Tabela.class)) {
                tabela = ((Tabela) clazz.getAnnotation(Tabela.class)).nome();
            } else {
                tabela = clazz.getSimpleName().toLowerCase();
            }
            // busque field anotado com @Chave, caso não encontrado pegue o primeiro field
            pk = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Chave.class))
                .findFirst()
                .orElse(clazz.getDeclaredFields()[0]);

            String sql = String.format("DELETE FROM %s WHERE %s = ?", tabela, pk.getName());

            PreparedStatement stmt = con.prepareStatement(sql);

            pk.setAccessible(true);

            if (Long.class.equals(id.getClass())) {
                stmt.setLong(1, (Long) id);
            }

            stmt.execute();

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(T o) { // generics por erasure (apagamento)

        try {
            // hard-coded (config estática no código) TODO: var ambiente
            Connection con = DriverManager.getConnection("jdbc:sqlite:mochinho.db"); 

            final String tabela;

            if (clazz.isAnnotationPresent(Tabela.class)) {
                tabela = ((Tabela) clazz.getAnnotation(Tabela.class)).nome();
            } else {
                tabela = clazz.getSimpleName().toLowerCase();
            }

            Field[] fields = o.getClass().getDeclaredFields();

            String var = Stream.of(fields) //
                .map(f -> "?") // [?, ? ,?]
                .collect(Collectors.joining(",")); // "?, ?, ?""

            String columns = Stream.of(fields)
                .map(Field::getName) // nome, ativo, etc
                .collect(Collectors.joining(",")); // nome, ativo

            String insert = String.format(
                "INSERT INTO %s (%s) VALUES (%s)", 
                    tabela,
                    columns, 
                    var);

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

                if (Long.class.equals(f.getType())) {
                    Long valor = (Long) f.get(o);
                    if (valor == null) {
                        stmt.setNull(i, Types.NUMERIC);
                    } else {
                        stmt.setLong(i, valor);
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
