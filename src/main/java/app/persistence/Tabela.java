package app.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Tabela { // @annotation
    
    String nome();

}

// @Tabela <== válido
// class Algo {}

// @Tabela <== inválido
// private Integer att;