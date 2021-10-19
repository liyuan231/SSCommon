package com.example.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.LinkedList;
import java.util.List;

@Configuration
public class SwaggerConfiguration {
    @Value("${swagger.enabled:true}")
    private Boolean enabled;

//    @Bean
//    public ApiInfo apiInfo() {
//        return (new ApiInfoBuilder()).title("接口文档").description("xx2").version("v0.0.1").build();
//    }

    @Bean
    public Docket docket() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> parameters = new LinkedList<>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        parameters.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2).groupName("Group-Name")
                .enable(enabled)
                .select()
                .paths(PathSelectors.any()).build().globalOperationParameters(parameters);
    }
}
