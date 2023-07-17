package com.miguel.biblioteca.configurations;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Provides RSA key pair generation for asymmetric encryption and decryption. The class is specifically designed to be used for encoding and decoding JWTs.
 * 
 * It generates an RSA key pair consisting of a public key and a private key, which are required for encryption and decryption operations.
 * 
 * The RSA key pair generation is performed in the default constructor of the class.
 * It creates an instance of KeyPairGenerator using the RSA algorithm, initializes it with a key size of 2048 bits,
 * and generates a new RSA key pair. The generated key pair's public and private keys are assigned to the corresponding class fields.
 * 
 * @author Biblioteca
 */
@Getter @Setter
@Component
public class RSAKeyProvider {
    
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public RSAKeyProvider(){
        
        try{
            // Creates an instance of KeyPairGenerator using the RSA algorithm.
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            // Initializes the KeyPairGenerator with a key size of 2048 bits.
            keyPairGenerator.initialize(2048);
            // Generates a new RSA key pair using the initialized KeyPairGenerator and assigns it to the keyPair variable.
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            
            this.publicKey = (RSAPublicKey) keyPair.getPublic();
            this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
            
        } catch(Exception e){
            throw new IllegalStateException();
        }        
    }
}