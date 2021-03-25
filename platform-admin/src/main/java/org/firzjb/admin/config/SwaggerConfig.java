/*******************************************************************************
 *
 * Auphi Data Integration PlatformKettle Platform
 * Copyright C 2011-2018 by Auphi BI : http://www.doetl.com

 * Support：support@pentahochina.com
 *
 *******************************************************************************
 *
 * Licensed under the LGPL License, Version 3.0 the "License";
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    https://opensource.org/licenses/LGPL-3.0

 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package org.firzjb.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 *  配置类 SwaggerConfig
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 20:07
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
@ComponentScan(value = "org.firzjb",includeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION,value = RestController.class)
}, useDefaultFilters = false)
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .select()
               // .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.basePackage("org.firzjb"))
                //.paths(PathSelectors.any())
                .build()
                .globalOperationParameters(setHeaderToken())
                .ignoredParameterTypes(ApiIgnore.class);
    }


    private ApiInfo apiInfo() {
        @SuppressWarnings("deprecation")
        ApiInfo info = new ApiInfo(
                "傲飞数据整合平台接口文档",
                "傲飞数据整合平台接口文档",
                "3.9",
                "urn:tos",
                "Auphi Data Management Platform",
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0");
        return info;
    }

    private List<Parameter> setHeaderToken() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("Authorization").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return pars;
    }
}
