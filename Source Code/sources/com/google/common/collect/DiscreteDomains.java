package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
@Deprecated
public final class DiscreteDomains {
    private DiscreteDomains() {
    }

    public static DiscreteDomain<Integer> integers() {
        return DiscreteDomain.integers();
    }

    public static DiscreteDomain<Long> longs() {
        return DiscreteDomain.longs();
    }
}
