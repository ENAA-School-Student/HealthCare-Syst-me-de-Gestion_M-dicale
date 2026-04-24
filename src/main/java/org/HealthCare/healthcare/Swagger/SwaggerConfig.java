package org.HealthCare.healthcare.Swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI fleetFlowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("HealthCare API")
                        .description("API pour la gestion des patient, medecin et dossierMedical et rendezVous")
                        .version("1.0.0"));
    }
}
