package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public Credential getCredential(Integer credentialId){
        return credentialMapper.getCredential(credentialId);
    }

    public List<Credential> getCredentials(Integer userId){
        return credentialMapper.getCredentials(userId);
    }

    public int save(Credential credential){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setKey(encodedKey);
        return credentialMapper.save(credential);
    }

    public void update(Credential credential){
        Credential currentCredential = credentialMapper.getCredential(credential.getCredentialId());
        currentCredential.setUrl(credential.getUrl());
        currentCredential.setUsername(credential.getUsername());
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), currentCredential.getKey());
        currentCredential.setPassword(encryptedPassword);
        credentialMapper.update(currentCredential);
    }

    public void delete (Integer credentialId){
        credentialMapper.delete(credentialId);
    }
}
