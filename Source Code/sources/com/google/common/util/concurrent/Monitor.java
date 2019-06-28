package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

@Beta
public final class Monitor {
    @GuardedBy("lock")
    private final ArrayList<Guard> activeGuards;
    private final boolean fair;
    private final ReentrantLock lock;

    @Beta
    public static abstract class Guard {
        final Condition condition;
        final Monitor monitor;
        @GuardedBy("monitor.lock")
        int waiterCount = 0;

        public abstract boolean isSatisfied();

        protected Guard(Monitor monitor) {
            this.monitor = (Monitor) Preconditions.checkNotNull(monitor, "monitor");
            this.condition = monitor.lock.newCondition();
        }

        public final boolean equals(Object other) {
            return this == other;
        }

        public final int hashCode() {
            return super.hashCode();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean enterIf(com.google.common.util.concurrent.Monitor.Guard r5, long r6, java.util.concurrent.TimeUnit r8) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0013 in list [B:12:0x001b]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/815992954.run(Unknown Source)
*/
        /*
        r4 = this;
        r2 = r5.monitor;
        if (r2 == r4) goto L_0x000a;
    L_0x0004:
        r2 = new java.lang.IllegalMonitorStateException;
        r2.<init>();
        throw r2;
    L_0x000a:
        r0 = r4.lock;
        r2 = r4.enter(r6, r8);
        if (r2 != 0) goto L_0x0014;
    L_0x0012:
        r1 = 0;
    L_0x0013:
        return r1;
    L_0x0014:
        r1 = 0;
        r1 = r5.isSatisfied();	 Catch:{ all -> 0x001f }
        if (r1 != 0) goto L_0x0013;
    L_0x001b:
        r0.unlock();
        goto L_0x0013;
    L_0x001f:
        r2 = move-exception;
        if (r1 != 0) goto L_0x0025;
    L_0x0022:
        r0.unlock();
    L_0x0025:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Monitor.enterIf(com.google.common.util.concurrent.Monitor$Guard, long, java.util.concurrent.TimeUnit):boolean");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean enterIfInterruptibly(com.google.common.util.concurrent.Monitor.Guard r5, long r6, java.util.concurrent.TimeUnit r8) throws java.lang.InterruptedException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0013 in list [B:12:0x001b]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/815992954.run(Unknown Source)
*/
        /*
        r4 = this;
        r2 = r5.monitor;
        if (r2 == r4) goto L_0x000a;
    L_0x0004:
        r2 = new java.lang.IllegalMonitorStateException;
        r2.<init>();
        throw r2;
    L_0x000a:
        r0 = r4.lock;
        r2 = r0.tryLock(r6, r8);
        if (r2 != 0) goto L_0x0014;
    L_0x0012:
        r1 = 0;
    L_0x0013:
        return r1;
    L_0x0014:
        r1 = 0;
        r1 = r5.isSatisfied();	 Catch:{ all -> 0x001f }
        if (r1 != 0) goto L_0x0013;
    L_0x001b:
        r0.unlock();
        goto L_0x0013;
    L_0x001f:
        r2 = move-exception;
        if (r1 != 0) goto L_0x0025;
    L_0x0022:
        r0.unlock();
    L_0x0025:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Monitor.enterIfInterruptibly(com.google.common.util.concurrent.Monitor$Guard, long, java.util.concurrent.TimeUnit):boolean");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean tryEnterIf(com.google.common.util.concurrent.Monitor.Guard r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0013 in list [B:12:0x001b]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/815992954.run(Unknown Source)
*/
        /*
        r3 = this;
        r2 = r4.monitor;
        if (r2 == r3) goto L_0x000a;
    L_0x0004:
        r2 = new java.lang.IllegalMonitorStateException;
        r2.<init>();
        throw r2;
    L_0x000a:
        r0 = r3.lock;
        r2 = r0.tryLock();
        if (r2 != 0) goto L_0x0014;
    L_0x0012:
        r1 = 0;
    L_0x0013:
        return r1;
    L_0x0014:
        r1 = 0;
        r1 = r4.isSatisfied();	 Catch:{ all -> 0x001f }
        if (r1 != 0) goto L_0x0013;
    L_0x001b:
        r0.unlock();
        goto L_0x0013;
    L_0x001f:
        r2 = move-exception;
        if (r1 != 0) goto L_0x0025;
    L_0x0022:
        r0.unlock();
    L_0x0025:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Monitor.tryEnterIf(com.google.common.util.concurrent.Monitor$Guard):boolean");
    }

    public Monitor() {
        this(false);
    }

    public Monitor(boolean fair) {
        this.activeGuards = Lists.newArrayListWithCapacity(1);
        this.fair = fair;
        this.lock = new ReentrantLock(fair);
    }

    public void enter() {
        this.lock.lock();
    }

    public void enterInterruptibly() throws InterruptedException {
        this.lock.lockInterruptibly();
    }

    public boolean enter(long time, TimeUnit unit) {
        ReentrantLock lock = this.lock;
        if (!this.fair && lock.tryLock()) {
            return true;
        }
        long startNanos = System.nanoTime();
        long timeoutNanos = unit.toNanos(time);
        long remainingNanos = timeoutNanos;
        boolean interruptIgnored = false;
        while (true) {
            try {
                boolean tryLock = lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS);
                break;
            } catch (InterruptedException e) {
                interruptIgnored = true;
                remainingNanos = timeoutNanos - (System.nanoTime() - startNanos);
            } catch (Throwable th) {
                if (1 != null) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (!interruptIgnored) {
            return tryLock;
        }
        Thread.currentThread().interrupt();
        return tryLock;
    }

    public boolean enterInterruptibly(long time, TimeUnit unit) throws InterruptedException {
        return this.lock.tryLock(time, unit);
    }

    public boolean tryEnter() {
        return this.lock.tryLock();
    }

    public void enterWhen(Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        boolean reentrant = lock.isHeldByCurrentThread();
        boolean success = false;
        lock.lockInterruptibly();
        try {
            waitInterruptibly(guard, reentrant);
            success = true;
        } finally {
            if (!success) {
                lock.unlock();
            }
        }
    }

    public void enterWhenUninterruptibly(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        boolean reentrant = lock.isHeldByCurrentThread();
        boolean success = false;
        lock.lock();
        try {
            waitUninterruptibly(guard, reentrant);
            success = true;
        } finally {
            if (!success) {
                lock.unlock();
            }
        }
    }

    public boolean enterWhen(Guard guard, long time, TimeUnit unit) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        long remainingNanos;
        ReentrantLock lock = this.lock;
        boolean reentrant = lock.isHeldByCurrentThread();
        if (this.fair || !lock.tryLock()) {
            long startNanos = System.nanoTime();
            if (!lock.tryLock(time, unit)) {
                return false;
            }
            remainingNanos = unit.toNanos(time) - (System.nanoTime() - startNanos);
        } else {
            remainingNanos = unit.toNanos(time);
        }
        boolean satisfied = false;
        try {
            satisfied = waitInterruptibly(guard, remainingNanos, reentrant);
            return satisfied;
        } finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }

    public boolean enterWhenUninterruptibly(Guard guard, long time, TimeUnit unit) {
        boolean satisfied;
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        long remainingNanos;
        ReentrantLock lock = this.lock;
        boolean reentrant = lock.isHeldByCurrentThread();
        boolean interruptIgnored = false;
        if (this.fair || !lock.tryLock()) {
            long startNanos;
            long timeoutNanos;
            try {
                startNanos = System.nanoTime();
                timeoutNanos = unit.toNanos(time);
                remainingNanos = timeoutNanos;
                while (true) {
                    break;
                }
                if (lock.tryLock(remainingNanos, TimeUnit.NANOSECONDS)) {
                    remainingNanos = timeoutNanos - (System.nanoTime() - startNanos);
                } else {
                    satisfied = false;
                    remainingNanos = timeoutNanos - (System.nanoTime() - startNanos);
                    if (interruptIgnored) {
                        Thread.currentThread().interrupt();
                    }
                    return satisfied;
                }
            } catch (InterruptedException e) {
                interruptIgnored = true;
                remainingNanos = timeoutNanos - (System.nanoTime() - startNanos);
            } catch (Throwable th) {
                if (interruptIgnored) {
                    Thread.currentThread().interrupt();
                }
            }
        } else {
            remainingNanos = unit.toNanos(time);
        }
        satisfied = false;
        satisfied = waitUninterruptibly(guard, remainingNanos, reentrant);
        if (!satisfied) {
            lock.unlock();
        }
        if (interruptIgnored) {
            Thread.currentThread().interrupt();
        }
        return satisfied;
    }

    public boolean enterIf(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        lock.lock();
        boolean satisfied = false;
        try {
            satisfied = guard.isSatisfied();
            return satisfied;
        } finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }

    public boolean enterIfInterruptibly(Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        boolean satisfied = false;
        try {
            satisfied = guard.isSatisfied();
            return satisfied;
        } finally {
            if (!satisfied) {
                lock.unlock();
            }
        }
    }

    public void waitFor(Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        } else if (this.lock.isHeldByCurrentThread()) {
            waitInterruptibly(guard, true);
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    public void waitForUninterruptibly(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        } else if (this.lock.isHeldByCurrentThread()) {
            waitUninterruptibly(guard, true);
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    public boolean waitFor(Guard guard, long time, TimeUnit unit) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        } else if (this.lock.isHeldByCurrentThread()) {
            return waitInterruptibly(guard, unit.toNanos(time), true);
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    public boolean waitForUninterruptibly(Guard guard, long time, TimeUnit unit) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        } else if (this.lock.isHeldByCurrentThread()) {
            return waitUninterruptibly(guard, unit.toNanos(time), true);
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    public void leave() {
        ReentrantLock lock = this.lock;
        if (lock.isHeldByCurrentThread()) {
            try {
                signalConditionsOfSatisfiedGuards(null);
            } finally {
                lock.unlock();
            }
        } else {
            throw new IllegalMonitorStateException();
        }
    }

    public boolean isFair() {
        return this.lock.isFair();
    }

    public boolean isOccupied() {
        return this.lock.isLocked();
    }

    public boolean isOccupiedByCurrentThread() {
        return this.lock.isHeldByCurrentThread();
    }

    public int getOccupiedDepth() {
        return this.lock.getHoldCount();
    }

    public int getQueueLength() {
        return this.lock.getQueueLength();
    }

    public boolean hasQueuedThreads() {
        return this.lock.hasQueuedThreads();
    }

    public boolean hasQueuedThread(Thread thread) {
        return this.lock.hasQueuedThread(thread);
    }

    public boolean hasWaiters(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        this.lock.lock();
        try {
            boolean z = guard.waiterCount > 0;
            this.lock.unlock();
            return z;
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }

    public int getWaitQueueLength(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        this.lock.lock();
        try {
            int i = guard.waiterCount;
            return i;
        } finally {
            this.lock.unlock();
        }
    }

    @GuardedBy("lock")
    private void signalConditionsOfSatisfiedGuards(@Nullable Guard interruptedGuard) {
        ArrayList<Guard> guards = this.activeGuards;
        int guardCount = guards.size();
        int i = 0;
        while (i < guardCount) {
            try {
                Guard guard = (Guard) guards.get(i);
                if (!(guard == interruptedGuard && guard.waiterCount == 1) && guard.isSatisfied()) {
                    guard.condition.signal();
                    return;
                }
                i++;
            } catch (Throwable throwable) {
                for (i = 0; i < guardCount; i++) {
                    ((Guard) guards.get(i)).condition.signalAll();
                }
                RuntimeException propagate = Throwables.propagate(throwable);
            }
        }
    }

    @GuardedBy("lock")
    private void incrementWaiters(Guard guard) {
        int waiters = guard.waiterCount;
        guard.waiterCount = waiters + 1;
        if (waiters == 0) {
            this.activeGuards.add(guard);
        }
    }

    @GuardedBy("lock")
    private void decrementWaiters(Guard guard) {
        int waiters = guard.waiterCount - 1;
        guard.waiterCount = waiters;
        if (waiters == 0) {
            this.activeGuards.remove(guard);
        }
    }

    @GuardedBy("lock")
    private void waitInterruptibly(Guard guard, boolean signalBeforeWaiting) throws InterruptedException {
        if (!guard.isSatisfied()) {
            if (signalBeforeWaiting) {
                signalConditionsOfSatisfiedGuards(null);
            }
            incrementWaiters(guard);
            try {
                Condition condition = guard.condition;
                do {
                    condition.await();
                } while (!guard.isSatisfied());
                decrementWaiters(guard);
            } catch (InterruptedException interrupt) {
                signalConditionsOfSatisfiedGuards(guard);
                throw interrupt;
            } catch (Throwable th) {
                decrementWaiters(guard);
            }
        }
    }

    @GuardedBy("lock")
    private void waitUninterruptibly(Guard guard, boolean signalBeforeWaiting) {
        if (!guard.isSatisfied()) {
            if (signalBeforeWaiting) {
                signalConditionsOfSatisfiedGuards(null);
            }
            incrementWaiters(guard);
            try {
                Condition condition = guard.condition;
                while (true) {
                    condition.awaitUninterruptibly();
                    if (guard.isSatisfied()) {
                        break;
                    }
                }
            } finally {
                decrementWaiters(guard);
            }
        }
    }

    @GuardedBy("lock")
    private boolean waitInterruptibly(Guard guard, long remainingNanos, boolean signalBeforeWaiting) throws InterruptedException {
        if (!guard.isSatisfied()) {
            if (signalBeforeWaiting) {
                signalConditionsOfSatisfiedGuards(null);
            }
            incrementWaiters(guard);
            try {
                Condition condition = guard.condition;
                while (remainingNanos > 0) {
                    remainingNanos = condition.awaitNanos(remainingNanos);
                    if (guard.isSatisfied()) {
                        decrementWaiters(guard);
                    }
                }
                decrementWaiters(guard);
                return false;
            } catch (InterruptedException interrupt) {
                signalConditionsOfSatisfiedGuards(guard);
                throw interrupt;
            } catch (Throwable th) {
                decrementWaiters(guard);
            }
        }
        return true;
    }

    @GuardedBy("lock")
    private boolean waitUninterruptibly(Guard guard, long timeoutNanos, boolean signalBeforeWaiting) {
        if (!guard.isSatisfied()) {
            long startNanos = System.nanoTime();
            if (signalBeforeWaiting) {
                signalConditionsOfSatisfiedGuards(null);
            }
            boolean interruptIgnored = false;
            try {
                incrementWaiters(guard);
                Condition condition = guard.condition;
                long remainingNanos = timeoutNanos;
                while (remainingNanos > 0) {
                    try {
                        remainingNanos = condition.awaitNanos(remainingNanos);
                    } catch (InterruptedException e) {
                        signalConditionsOfSatisfiedGuards(guard);
                        interruptIgnored = true;
                        remainingNanos = timeoutNanos - (System.nanoTime() - startNanos);
                    } catch (Throwable th) {
                        decrementWaiters(guard);
                        throw th;
                    }
                    if (guard.isSatisfied()) {
                        decrementWaiters(guard);
                        if (interruptIgnored) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                decrementWaiters(guard);
                if (!interruptIgnored) {
                    return false;
                }
                Thread.currentThread().interrupt();
                return false;
            } catch (Throwable th2) {
                if (interruptIgnored) {
                    Thread.currentThread().interrupt();
                }
                throw th2;
            }
        }
        return true;
    }
}
