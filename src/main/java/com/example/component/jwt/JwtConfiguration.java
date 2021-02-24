package com.example.component.jwt;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

@EnableConfigurationProperties({JwtProperties.class})
@ConditionalOnProperty(prefix = "jwt.config", name = "enabled")
@Configuration
public class JwtConfiguration {
    private Logger logger = LoggerFactory.getLogger(getClass());


    @Bean
    public JwtTokenGenerator jwtTokenGenerator(JwtProperties jwtProperties) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyStoreException, InvalidKeySpecException {
        return new JwtTokenGenerator(new JwtPayloadBuilder(), jwtProperties);
    }
}
