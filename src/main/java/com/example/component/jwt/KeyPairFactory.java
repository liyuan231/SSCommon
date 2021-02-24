package com.example.component.jwt;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

public class KeyPairFactory {
    private KeyStore keyStore;

    public KeyPair create(String keyPath, String keyAlias, String keyPass) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, InvalidKeySpecException {
        ClassPathResource resource = new ClassPathResource(keyPath);
        char[] pwd = keyPass.toCharArray();
        synchronized (this) {
            if (this.keyStore == null) {
                synchronized (this) {
                    this.keyStore = KeyStore.getInstance("jks");
                    this.keyStore.load(resource.getInputStream(), pwd);
                }
            }
        }
        RSAPrivateCrtKey privateCrtKey = (RSAPrivateCrtKey) this.keyStore.getKey(keyAlias, pwd);
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(privateCrtKey.getModulus(), privateCrtKey.getPublicExponent());
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(rsaPublicKeySpec);
        return new KeyPair(publicKey, privateCrtKey);
    }
}
