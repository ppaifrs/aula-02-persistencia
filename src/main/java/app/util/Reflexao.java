package app.util;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.Map;

public class Reflexao {
    
    public static void teste(Object o) {
        // refletir sobre este objeto
        Class classe = o.getClass();
        System.out.println(classe.getName());

        System.out.println("CREATE TABLE " + classe.getSimpleName().toLowerCase()); // CREATE TABLE chamado

        Field[] atributos = classe.getDeclaredFields();
        for (Field atributo : atributos) {
            System.out.println(atributo.getName());
            System.out.println(atributo.getType().getSimpleName());
        }
    }

    static Map<String, String> tipos = Map.of(
        "String", "TEXT",
        "Long", "LONG",
        "LocalDateTime", "TIMESTAMP",
        "Boolean", "TINYINT"
    );
    // CREATE TABLE chamado (
    //   numero LONG
    // );
    public static String ddl(Class classe) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ")
            .append(classe.getSimpleName().toLowerCase())
            .append(" (\n");
        
        Arrays.stream(classe.getDeclaredFields())
            .forEach(atributo -> builder.append("\t")
                    .append(atributo.getName())
                    .append(" ")
                    .append(tipos.get(atributo.getType().getSimpleName()))
                    .append(",\n")
        );

        builder.append(");");
        return builder.toString();
    }
}
