package com.factoryx.common.jpa;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Locale;

public class CleanNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        String text = name.getText();
        
        String snake = text.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.ROOT);
        
        if (snake.endsWith("_value")) {
            snake = snake.substring(0, snake.length() - 6);
        } else if (snake.endsWith("_amount")) {
            snake = snake.substring(0, snake.length() - 7);
        }
        
        return Identifier.toIdentifier(snake);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return Identifier.toIdentifier(name.getText().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.ROOT));
    }
}
