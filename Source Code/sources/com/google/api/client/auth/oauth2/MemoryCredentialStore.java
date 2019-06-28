package com.google.api.client.auth.oauth2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MemoryCredentialStore implements CredentialStore {
    private final Lock lock = new ReentrantLock();
    private final Map<String, MemoryPersistedCredential> store = new HashMap();

    public void store(String userId, Credential credential) {
        this.lock.lock();
        try {
            MemoryPersistedCredential item = (MemoryPersistedCredential) this.store.get(userId);
            if (item == null) {
                item = new MemoryPersistedCredential();
                this.store.put(userId, item);
            }
            item.store(credential);
        } finally {
            this.lock.unlock();
        }
    }

    public void delete(String userId, Credential credential) {
        this.lock.lock();
        try {
            this.store.remove(userId);
        } finally {
            this.lock.unlock();
        }
    }

    public boolean load(String userId, Credential credential) {
        this.lock.lock();
        try {
            MemoryPersistedCredential item = (MemoryPersistedCredential) this.store.get(userId);
            if (item != null) {
                item.load(credential);
            }
            boolean z = item != null;
            this.lock.unlock();
            return z;
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }
}
