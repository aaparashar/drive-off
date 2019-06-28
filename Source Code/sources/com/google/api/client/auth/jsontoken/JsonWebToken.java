package com.google.api.client.auth.jsontoken;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Key;
import com.google.common.base.Preconditions;

public class JsonWebToken {
    private final Header header;
    private final Payload payload;

    public static class Header extends GenericJson {
        @Key("typ")
        private String type;

        public final String getType() {
            return this.type;
        }

        public Header setType(String type) {
            this.type = type;
            return this;
        }
    }

    public static class Payload extends GenericJson {
        @Key("aud")
        private String audience;
        private final Clock clock;
        @Key("exp")
        private Long expirationTimeSeconds;
        @Key("iat")
        private Long issuedAtTimeSeconds;
        @Key("iss")
        private String issuer;
        @Key("jti")
        private String jwtId;
        @Key("nbf")
        private Long notBeforeTimeSeconds;
        @Key("prn")
        private String principal;
        @Key("typ")
        private String type;

        public Payload() {
            this(Clock.SYSTEM);
        }

        public Payload(Clock clock) {
            this.clock = (Clock) Preconditions.checkNotNull(clock);
        }

        public Long getExpirationTimeSeconds() {
            return this.expirationTimeSeconds;
        }

        public Payload setExpirationTimeSeconds(Long expirationTimeSeconds) {
            this.expirationTimeSeconds = expirationTimeSeconds;
            return this;
        }

        public Long getNotBeforeTimeSeconds() {
            return this.notBeforeTimeSeconds;
        }

        public Payload setNotBeforeTimeSeconds(Long notBeforeTimeSeconds) {
            this.notBeforeTimeSeconds = notBeforeTimeSeconds;
            return this;
        }

        public Long getIssuedAtTimeSeconds() {
            return this.issuedAtTimeSeconds;
        }

        public Payload setIssuedAtTimeSeconds(Long issuedAtTimeSeconds) {
            this.issuedAtTimeSeconds = issuedAtTimeSeconds;
            return this;
        }

        public String getIssuer() {
            return this.issuer;
        }

        public Payload setIssuer(String issuer) {
            this.issuer = issuer;
            return this;
        }

        public String getAudience() {
            return this.audience;
        }

        public Payload setAudience(String audience) {
            this.audience = audience;
            return this;
        }

        public String getPrincipal() {
            return this.principal;
        }

        public Payload setPrincipal(String principal) {
            this.principal = principal;
            return this;
        }

        public String getJwtId() {
            return this.jwtId;
        }

        public Payload setJwtId(String jwtId) {
            this.jwtId = jwtId;
            return this;
        }

        public String getType() {
            return this.type;
        }

        public Payload setType(String type) {
            this.type = type;
            return this;
        }

        public boolean isValidTime(long acceptableTimeSkewSeconds) {
            long now = this.clock.currentTimeMillis();
            return (this.expirationTimeSeconds == null || now <= (this.expirationTimeSeconds.longValue() + acceptableTimeSkewSeconds) * 1000) && (this.issuedAtTimeSeconds == null || now >= (this.issuedAtTimeSeconds.longValue() - acceptableTimeSkewSeconds) * 1000);
        }
    }

    public JsonWebToken(Header header, Payload payload) {
        this.header = (Header) Preconditions.checkNotNull(header);
        this.payload = (Payload) Preconditions.checkNotNull(payload);
    }

    public Header getHeader() {
        return this.header;
    }

    public Payload getPayload() {
        return this.payload;
    }
}
