package com.sagar.sms.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
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
    public OpenAPI studentManagementOpenAPI() {

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local Development Server");

        Contact contact = new Contact();
        contact.setName("Sagar Kirtakar");
        contact.setEmail("sagarkirtakar@2002@gmail.com");
        contact.setUrl("https://github.com/SagarKirtakar");

        License license = new License();
        license.setName("Apache License 2.0");
        license.setUrl("https://www.apache.org/licenses/LICENSE-2.0");

        Info info = new Info()
                .title("Student Management System REST API")
                .version("1.0.0")
                .description("""
                        REST API for managing students, courses,
                        enrollments, grades, and reports.
                        
                        Features:
                        • Student Management
                        • Course Management
                        • Enrollment Management
                        • Grade Assignment
                        • Student Reports
                        • Course Reports
                        """)
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                        .url("https://github.com/SagarKirtakar/student-management-system.git"));
    }
}