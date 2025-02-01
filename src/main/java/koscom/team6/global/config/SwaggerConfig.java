package koscom.team6.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        String securitySchemeName = "bearerAuth"; // 인증 방식 이름
        Set<String> pathsToExclude = Set.of("/login", "/join"); // 🔥 JWT 인증 제외할 API 경로

        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("My API")
                        .version("1.0")
                        .description("JWT 토큰 인증이 적용된 API 문서"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));

        // 기본적으로 모든 API에 JWT 적용
        openAPI.addSecurityItem(new SecurityRequirement().addList(securitySchemeName));

        // 특정 API만 JWT 인증 제외
        if (openAPI.getPaths() != null) {
            for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
                String path = entry.getKey();
                if (pathsToExclude.contains(path)) {
                    for (Operation operation : entry.getValue().readOperations()) {
                        operation.setSecurity(List.of()); // 🔥 JWT 인증 제외
                    }
                }
            }
        }

        return openAPI;
    }
}