package com.google.api.client.auth.jsontoken;

import com.google.api.client.auth.jsontoken.JsonWebToken.Payload;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.Key;
import com.google.api.client.util.StringUtils;
import com.google.common.base.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class JsonWebSignature extends JsonWebToken {
    private final byte[] signatureBytes;
    private final byte[] signedContentBytes;

    public static final class Parser {
        private Class<? extends Header> headerClass = Header.class;
        private final JsonFactory jsonFactory;
        private Class<? extends Payload> payloadClass = Payload.class;

        public Parser(JsonFactory jsonFactory) {
            this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        }

        public Class<? extends Header> getHeaderClass() {
            return this.headerClass;
        }

        public Parser setHeaderClass(Class<? extends Header> headerClass) {
            this.headerClass = headerClass;
            return this;
        }

        public Class<? extends Payload> getPayloadClass() {
            return this.payloadClass;
        }

        public Parser setPayloadClass(Class<? extends Payload> payloadClass) {
            this.payloadClass = payloadClass;
            return this;
        }

        public JsonFactory getJsonFactory() {
            return this.jsonFactory;
        }

        public JsonWebSignature parse(String tokenString) throws IOException {
            boolean z;
            boolean z2 = true;
            int firstDot = tokenString.indexOf(46);
            if (firstDot != -1) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z);
            byte[] headerBytes = Base64.decodeBase64(tokenString.substring(0, firstDot));
            int secondDot = tokenString.indexOf(46, firstDot + 1);
            if (secondDot != -1) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z);
            if (tokenString.indexOf(46, secondDot + 1) == -1) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z);
            byte[] payloadBytes = Base64.decodeBase64(tokenString.substring(firstDot + 1, secondDot));
            byte[] signatureBytes = Base64.decodeBase64(tokenString.substring(secondDot + 1));
            byte[] signedContentBytes = StringUtils.getBytesUtf8(tokenString.substring(0, secondDot));
            Header header = (Header) this.jsonFactory.fromInputStream(new ByteArrayInputStream(headerBytes), this.headerClass);
            if (header.getAlgorithm() == null) {
                z2 = false;
            }
            Preconditions.checkArgument(z2);
            return new JsonWebSignature(header, (Payload) this.jsonFactory.fromInputStream(new ByteArrayInputStream(payloadBytes), this.payloadClass), signatureBytes, signedContentBytes);
        }
    }

    public static class Header extends com.google.api.client.auth.jsontoken.JsonWebToken.Header {
        @Key("alg")
        private String algorithm;
        @Key("jku")
        private String jwkUrl;
        @Key("kid")
        private String keyId;
        @Key("x5t")
        private String x509Thumbprint;
        @Key("x5u")
        private String x509Url;

        public Header setType(String type) {
            super.setType(type);
            return this;
        }

        public final String getAlgorithm() {
            return this.algorithm;
        }

        public Header setAlgorithm(String algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        public String getJwkUrl() {
            return this.jwkUrl;
        }

        public Header setJwkUrl(String jwkUrl) {
            this.jwkUrl = jwkUrl;
            return this;
        }

        public String getKeyId() {
            return this.keyId;
        }

        public Header setKeyId(String keyId) {
            this.keyId = keyId;
            return this;
        }

        public String getX509Url() {
            return this.x509Url;
        }

        public Header setX509Url(String x509Url) {
            this.x509Url = x509Url;
            return this;
        }

        public String getX509Thumbprint() {
            return this.x509Thumbprint;
        }

        public Header setX509Thumbprint(String x509Thumbprint) {
            this.x509Thumbprint = x509Thumbprint;
            return this;
        }
    }

    public JsonWebSignature(Header header, Payload payload, byte[] signatureBytes, byte[] signedContentBytes) {
        super(header, payload);
        this.signatureBytes = signatureBytes;
        this.signedContentBytes = signedContentBytes;
    }

    public Header getHeader() {
        return (Header) super.getHeader();
    }

    public final byte[] getSignatureBytes() {
        return this.signatureBytes;
    }

    public final byte[] getSignedContentBytes() {
        return this.signedContentBytes;
    }

    public static JsonWebSignature parse(JsonFactory jsonFactory, String tokenString) throws IOException {
        return parser(jsonFactory).parse(tokenString);
    }

    public static Parser parser(JsonFactory jsonFactory) {
        return new Parser(jsonFactory);
    }
}
