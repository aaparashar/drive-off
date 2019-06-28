package com.google.api.client.auth.security;

import com.google.api.client.util.Base64;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class PrivateKeys {
    private static final String BEGIN = "-----BEGIN PRIVATE KEY-----";
    private static final String END = "-----END PRIVATE KEY-----";

    public static PrivateKey loadFromKeyStore(InputStream keyStream, String storePass, String alias, String keyPass) throws IOException, GeneralSecurityException {
        return loadFromKeyStore(KeyStore.getInstance(KeyStore.getDefaultType()), keyStream, storePass, alias, keyPass);
    }

    public static PrivateKey loadFromKeyStore(KeyStore keyStore, InputStream keyStream, String storePass, String alias, String keyPass) throws IOException, GeneralSecurityException {
        try {
            keyStore.load(keyStream, storePass.toCharArray());
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyPass.toCharArray());
            return privateKey;
        } finally {
            keyStream.close();
        }
    }

    public static PrivateKey loadFromPk8File(File file) throws IOException, GeneralSecurityException {
        byte[] privKeyBytes = new byte[((int) file.length())];
        DataInputStream inputStream = new DataInputStream(new FileInputStream(file));
        try {
            inputStream.readFully(privKeyBytes);
            String str = new String(privKeyBytes);
            if (str.startsWith(BEGIN) && str.endsWith(END)) {
                str = str.substring(BEGIN.length(), str.lastIndexOf(END));
            }
            return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(str)));
        } finally {
            inputStream.close();
        }
    }

    public static PrivateKey loadFromP12File(File p12File, String storePass, String alias, String keyPass) throws GeneralSecurityException, IOException {
        return loadFromKeyStore(KeyStore.getInstance("PKCS12"), new FileInputStream(p12File), storePass, alias, keyPass);
    }

    private PrivateKeys() {
    }
}
