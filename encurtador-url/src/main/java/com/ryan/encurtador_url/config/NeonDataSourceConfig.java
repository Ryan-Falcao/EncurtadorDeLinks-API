package com.ryan.encurtador_url.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Configuration
@ConditionalOnProperty(name = "database.url")
public class NeonDataSourceConfig {

    @Bean
    DataSource dataSource(@Value("${DATABASE_URL}") String databaseUrl) {
        URI uri = URI.create(databaseUrl);
        String[] credentials = uri.getRawUserInfo().split(":", 2);
        if (credentials.length != 2 || uri.getHost() == null) {
            throw new IllegalStateException("DATABASE_URL do PostgreSQL não é válida.");
        }

        String query = Arrays.stream(String.valueOf(uri.getRawQuery()).split("&"))
                .filter(item -> !item.startsWith("channel_binding="))
                .reduce((left, right) -> left + "&" + right)
                .orElse("");
        String jdbcUrl = "jdbc:postgresql://" + uri.getHost()
                + (uri.getPort() == -1 ? "" : ":" + uri.getPort())
                + uri.getRawPath()
                + (query.isBlank() ? "" : "?" + query);

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(decodificar(credentials[0]));
        dataSource.setPassword(decodificar(credentials[1]));
        return dataSource;
    }

    private String decodificar(String valor) {
        return URLDecoder.decode(valor, StandardCharsets.UTF_8);
    }
}
