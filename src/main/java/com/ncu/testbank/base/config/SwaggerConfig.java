package com.ncu.testbank.base.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.paths.SwaggerPathProvider;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
/**
 * 使用注解的方式来扫描API
 * 无需在Spring的xml配置文件来配置，由 @see @EnableWebMvc 代替
 * <p/>
 * <p> @author 刘新宇
 *
 * <p> @date 2015年1月30日 下午1:18:48
 * <p> @version 0.0.1
 */
//@Configuration
//@EnableWebMvc
@EnableSwagger
@ComponentScan(basePackages ={"com.ncu.testbank.admin.controller"})
public class SwaggerConfig extends WebMvcConfigurerAdapter {
   private SpringSwaggerConfig springSwaggerConfig;
   @Autowired
   public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
      this.springSwaggerConfig = springSwaggerConfig;
   }
   /**
    * 链式编程 来定制API样式
    * 后续会加上分组信息
    * @return
    */
   @Bean
   public SwaggerSpringMvcPlugin customImplementation(){
      return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
            .apiInfo(apiInfo())
            .includePatterns(".*")
//            .pathProvider(new GtPaths())
            .apiVersion("0.0.1")
            .swaggerGroup("user");
   }
    private ApiInfo apiInfo() {
      ApiInfo apiInfo = new ApiInfo(
              "admin API",
              "testbank 后台API文档",
              "<a href=\"http://127.0.0.1:9081/api\" \"=\"\" style=\"color: rgb(59, 115, 175); text-decoration: none; border-radius: 0px !important; border: 0px !important; bottom: auto !important; float: none !important; height: auto !important; left: auto !important; margin: 0px !important; outline: 0px !important; overflow: visible !important; padding: 0px !important; position: static !important; right: auto !important; top: auto !important; vertical-align: baseline !important; width: auto !important; box-sizing: content-box !important; min-height: inherit !important; background: none !important;\">http://127.0.0.1:9081/api",
              "bugkillers@163.com",
              "My License",
              "My Apps API License URL"
        );
      return apiInfo;
    }
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
      configurer.enable();
    }
     
    class GtPaths extends SwaggerPathProvider{
        @Override
        protected String applicationPath() {
            return "/restapi";
        }
        @Override
        protected String getDocumentationPath() {
            return "/restapi";
        }
    }
     
}