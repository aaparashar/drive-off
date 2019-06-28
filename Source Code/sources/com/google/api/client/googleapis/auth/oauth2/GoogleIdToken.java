package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.jsontoken.JsonWebSignature;
import com.google.api.client.auth.jsontoken.JsonWebSignature.Header;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Key;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleIdToken extends JsonWebSignature {

    public static class Payload extends com.google.api.client.auth.jsontoken.JsonWebToken.Payload {
        @Key("token_hash")
        private String accessTokenHash;
        @Key("email")
        private String email;
        @Key("verified_email")
        private boolean emailVerified;
        @Key("hd")
        private String hostedDomain;
        @Key("cid")
        private String issuee;
        @Key("id")
        private String userId;

        public Payload() {
            this(Clock.SYSTEM);
        }

        public Payload(Clock clock) {
            super(clock);
        }

        public String getUserId() {
            return this.userId;
        }

        public Payload setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public String getIssuee() {
            return this.issuee;
        }

        public Payload setIssuee(String issuee) {
            this.issuee = issuee;
            return this;
        }

        public String getAccessTokenHash() {
            return this.accessTokenHash;
        }

        public Payload setAccessTokenHash(String accessTokenHash) {
            this.accessTokenHash = accessTokenHash;
            return this;
        }

        public String getHostedDomain() {
            return this.hostedDomain;
        }

        public Payload setHostedDomain(String hostedDomain) {
            this.hostedDomain = hostedDomain;
            return this;
        }

        public String getEmail() {
            return this.email;
        }

        public Payload setEmail(String email) {
            this.email = email;
            return this;
        }

        public boolean getEmailVerified() {
            return this.emailVerified;
        }

        public Payload setEmailVerified(boolean emailVerified) {
            this.emailVerified = emailVerified;
            return this;
        }
    }

    public static GoogleIdToken parse(JsonFactory jsonFactory, String idTokenString) throws IOException {
        JsonWebSignature jws = JsonWebSignature.parser(jsonFactory).setPayloadClass(Payload.class).parse(idTokenString);
        return new GoogleIdToken(jws.getHeader(), (Payload) jws.getPayload(), jws.getSignatureBytes(), jws.getSignedContentBytes());
    }

    public GoogleIdToken(Header header, Payload payload, byte[] signatureBytes, byte[] signedContentBytes) {
        super(header, payload, signatureBytes, signedContentBytes);
    }

    public boolean verify(GoogleIdTokenVerifier verifier) throws GeneralSecurityException, IOException {
        return verifier.verify(this);
    }

    public Payload getPayload() {
        return (Payload) super.getPayload();
    }
}
