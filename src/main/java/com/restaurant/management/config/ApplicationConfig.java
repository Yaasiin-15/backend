package com.restaurant.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Application configuration class.
 * Provides common beans and configuration for the restaurant management system.
 */
@Configuration
@EnableTransactionManagement
public class ApplicationConfig {
    
    /**
     * Bean for method validation.
     * Enables validation of method parameters and return values.
     * 
     * @return MethodValidationPostProcessor instance
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setValidator(validator());
        return processor;
    }
    
    /**
     * Bean for validation.
     * Provides JSR-303 validation support.
     * 
     * @return LocalValidatorFactoryBean instance
     */
    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}