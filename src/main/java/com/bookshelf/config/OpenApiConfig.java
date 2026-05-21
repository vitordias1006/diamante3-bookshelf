package com.bookshelf.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("📚 Bookshelf API")
                        .version("1.0.0")
                        .description("""
                                API REST para gerenciamento de uma biblioteca virtual.
                                
                                **Funcionalidades:**
                                - CRUD completo de Autores, Livros e Resenhas
                                - Paginação e ordenação em todas as listagens
                                - Projections para respostas otimizadas
                                - Cache com Caffeine para melhor performance
                                - HATEOAS com links de navegação
                                - Filtros por gênero, autor, nota, spoiler e mais
                                
                                **Entidades:**
                                - `Autor` - Escritores com bio e dados biográficos
                                - `Livro` - Obras com ISBN, gênero e sinopse
                                - `Resenha` - Reviews com nota (1-5) e flag de spoiler
                                """)
                        .contact(new Contact()
                                .name("Bookshelf Team")
                                .email("contato@bookshelf.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor de Desenvolvimento")
                ));
    }
}
