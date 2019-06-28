package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
@Beta
public final class HostAndPort implements Serializable {
    private static final Pattern BRACKET_PATTERN = Pattern.compile("^\\[(.*:.*)\\](?::(\\d*))?$");
    private static final int NO_PORT = -1;
    private static final long serialVersionUID = 0;
    private final boolean hasBracketlessColons;
    private final String host;
    private final int port;

    private HostAndPort(String host, int port, boolean hasBracketlessColons) {
        this.host = host;
        this.port = port;
        this.hasBracketlessColons = hasBracketlessColons;
    }

    public String getHostText() {
        return this.host;
    }

    public boolean hasPort() {
        return this.port >= 0;
    }

    public int getPort() {
        Preconditions.checkState(hasPort());
        return this.port;
    }

    public int getPortOrDefault(int defaultPort) {
        return hasPort() ? this.port : defaultPort;
    }

    public static HostAndPort fromParts(String host, int port) {
        Preconditions.checkArgument(isValidPort(port));
        HostAndPort parsedHost = fromString(host);
        Preconditions.checkArgument(!parsedHost.hasPort());
        return new HostAndPort(parsedHost.host, port, parsedHost.hasBracketlessColons);
    }

    public static HostAndPort fromString(String hostPortString) {
        String host;
        Preconditions.checkNotNull(hostPortString);
        String portString = null;
        boolean hasBracketlessColons = false;
        if (hostPortString.startsWith("[")) {
            Matcher matcher = BRACKET_PATTERN.matcher(hostPortString);
            Preconditions.checkArgument(matcher.matches(), "Invalid bracketed host/port: %s", hostPortString);
            host = matcher.group(1);
            portString = matcher.group(2);
        } else {
            int colonPos = hostPortString.indexOf(58);
            if (colonPos < 0 || hostPortString.indexOf(58, colonPos + 1) != -1) {
                host = hostPortString;
                hasBracketlessColons = colonPos >= 0;
            } else {
                host = hostPortString.substring(0, colonPos);
                portString = hostPortString.substring(colonPos + 1);
            }
        }
        int port = -1;
        if (!Strings.isNullOrEmpty(portString)) {
            boolean z;
            if (portString.startsWith("+")) {
                z = false;
            } else {
                z = true;
            }
            Preconditions.checkArgument(z, "Unparseable port number: %s", hostPortString);
            try {
                port = Integer.parseInt(portString);
                Preconditions.checkArgument(isValidPort(port), "Port number out of range: %s", hostPortString);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Unparseable port number: " + hostPortString);
            }
        }
        return new HostAndPort(host, port, hasBracketlessColons);
    }

    public HostAndPort withDefaultPort(int defaultPort) {
        Preconditions.checkArgument(isValidPort(defaultPort));
        return (hasPort() || this.port == defaultPort) ? this : new HostAndPort(this.host, defaultPort, this.hasBracketlessColons);
    }

    public HostAndPort requireBracketsForIPv6() {
        boolean z;
        if (this.hasBracketlessColons) {
            z = false;
        } else {
            z = true;
        }
        Preconditions.checkArgument(z, "Possible bracketless IPv6 literal: %s", this.host);
        return this;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HostAndPort)) {
            return false;
        }
        HostAndPort that = (HostAndPort) other;
        if (Objects.equal(this.host, that.host) && this.port == that.port && this.hasBracketlessColons == that.hasBracketlessColons) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(this.host, Integer.valueOf(this.port), Boolean.valueOf(this.hasBracketlessColons));
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(this.host.length() + 7);
        if (this.host.indexOf(58) >= 0) {
            builder.append('[').append(this.host).append(']');
        } else {
            builder.append(this.host);
        }
        if (hasPort()) {
            builder.append(':').append(this.port);
        }
        return builder.toString();
    }

    private static boolean isValidPort(int port) {
        return port >= 0 && port <= 65535;
    }
}
