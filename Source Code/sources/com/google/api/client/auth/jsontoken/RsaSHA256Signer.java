package com.google.api.client.auth.jsontoken;

import com.google.api.client.auth.jsontoken.JsonWebSignature.Header;
import com.google.api.client.auth.jsontoken.JsonWebToken.Payload;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.StringUtils;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Signature;

public class RsaSHA256Signer {
    public static String sign(PrivateKey privateKey, JsonFactory jsonFactory, Header header, Payload payload) throws GeneralSecurityException {
        String content = Base64.encodeBase64URLSafeString(jsonFactory.toByteArray(header)) + "." + Base64.encodeBase64URLSafeString(jsonFactory.toByteArray(payload));
        byte[] contentBytes = StringUtils.getBytesUtf8(content);
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(contentBytes);
        return content + "." + Base64.encodeBase64URLSafeString(signer.sign());
    }

    private RsaSHA256Signer() {
    }
}
