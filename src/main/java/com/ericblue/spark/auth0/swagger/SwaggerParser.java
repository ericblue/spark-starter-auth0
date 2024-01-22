package com.ericblue.spark.auth0.swagger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Swagger;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class SwaggerParser {

    protected static final Logger logger = LoggerFactory.getLogger(SwaggerParser.class);

    /* Code imported from https://github.com/srlk/spark-swagger
    Note: Controller classes were not being found by the Reflections library.  Removing the commons-lang3 dependency
    resolved the issue.
     */
    public static String getSwaggerJson(String packageName) throws JsonProcessingException {
        logger.info("Getting swagger json for package: " + packageName);
        Swagger swagger = getSwagger(packageName);
        String json = swaggerToJson(swagger);
        return json;
    }

    public static Swagger getSwagger(String packageName) {
        logger.info("Getting swagger for package: " + packageName);
        Reflections reflections = new Reflections(packageName);
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setResourcePackage(packageName);
        beanConfig.setScan(true);
        beanConfig.scanAndRead();

        Swagger swagger = beanConfig.getSwagger();

        Reader reader = new Reader(swagger);

        Set<Class<?>> apiClasses = reflections.getTypesAnnotatedWith(Api.class);
        for (Class<?> apiClass : apiClasses) {

            logger.info("Found API class: " + apiClass.getName());
        }
        
        return reader.read(apiClasses);
    }

    public static String swaggerToJson(Swagger swagger) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        String json = objectMapper.writeValueAsString(swagger);
        return json;
    }
}
