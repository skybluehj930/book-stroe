package com.lhj.bookstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.classmate.TypeResolver;
import com.lhj.bookstore.dto.res.BookInfoRes;
import com.lhj.bookstore.dto.res.SupplyRes;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api(TypeResolver typeResolver) {
        return new Docket(DocumentationType.OAS_30)
        		// * 리턴하는 클래스를 명시하고자 할 때 해당 모델을 추가해준다.
        		.additionalModels(
        		typeResolver.resolve(BookInfoRes.class)
        		, typeResolver.resolve(SupplyRes.class))
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lhj.bookstore"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Book Store")
                .version("v1")
                .description("Book Store Api")
                .build();
    }
}
