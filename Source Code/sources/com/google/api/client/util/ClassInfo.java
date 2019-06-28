package com.google.api.client.util;

import com.google.common.base.Preconditions;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.WeakHashMap;

public final class ClassInfo {
    private static final Map<Class<?>, ClassInfo> CACHE = new WeakHashMap();
    private static final Map<Class<?>, ClassInfo> CACHE_IGNORE_CASE = new WeakHashMap();
    private final Class<?> clazz;
    private final boolean ignoreCase;
    private final IdentityHashMap<String, FieldInfo> nameToFieldInfoMap = new IdentityHashMap();
    final List<String> names;

    /* renamed from: com.google.api.client.util.ClassInfo$1 */
    class C00071 implements Comparator<String> {
        C00071() {
        }

        public int compare(String s0, String s1) {
            if (s0 == s1) {
                return 0;
            }
            if (s0 == null) {
                return -1;
            }
            return s1 == null ? 1 : s0.compareTo(s1);
        }
    }

    public static ClassInfo of(Class<?> underlyingClass) {
        return of(underlyingClass, false);
    }

    public static ClassInfo of(Class<?> underlyingClass, boolean ignoreCase) {
        if (underlyingClass == null) {
            return null;
        }
        ClassInfo classInfo;
        Map<Class<?>, ClassInfo> cache = ignoreCase ? CACHE_IGNORE_CASE : CACHE;
        synchronized (cache) {
            classInfo = (ClassInfo) cache.get(underlyingClass);
            if (classInfo == null) {
                classInfo = new ClassInfo(underlyingClass, ignoreCase);
                cache.put(underlyingClass, classInfo);
            }
        }
        return classInfo;
    }

    public Class<?> getUnderlyingClass() {
        return this.clazz;
    }

    public final boolean getIgnoreCase() {
        return this.ignoreCase;
    }

    public FieldInfo getFieldInfo(String name) {
        if (name != null) {
            if (this.ignoreCase) {
                name = name.toLowerCase();
            }
            name = name.intern();
        }
        return (FieldInfo) this.nameToFieldInfoMap.get(name);
    }

    public Field getField(String name) {
        FieldInfo fieldInfo = getFieldInfo(name);
        return fieldInfo == null ? null : fieldInfo.getField();
    }

    public boolean isEnum() {
        return this.clazz.isEnum();
    }

    public Collection<String> getNames() {
        return this.names;
    }

    private ClassInfo(Class<?> srcClass, boolean ignoreCase) {
        this.clazz = srcClass;
        this.ignoreCase = ignoreCase;
        boolean z = (ignoreCase && srcClass.isEnum()) ? false : true;
        Preconditions.checkArgument(z, "cannot ignore case on an enum: " + srcClass);
        TreeSet<String> nameSet = new TreeSet(new C00071());
        Class<?> superClass = srcClass.getSuperclass();
        if (superClass != null) {
            ClassInfo superClassInfo = of(superClass, ignoreCase);
            this.nameToFieldInfoMap.putAll(superClassInfo.nameToFieldInfoMap);
            nameSet.addAll(superClassInfo.names);
        }
        for (Field field : srcClass.getDeclaredFields()) {
            FieldInfo fieldInfo = FieldInfo.of(field);
            if (fieldInfo != null) {
                String fieldName = fieldInfo.getName();
                if (ignoreCase) {
                    fieldName = fieldName.toLowerCase().intern();
                }
                FieldInfo conflictingFieldInfo = (FieldInfo) this.nameToFieldInfoMap.get(fieldName);
                z = conflictingFieldInfo == null;
                String str = "two fields have the same %sname <%s>: %s and %s";
                Object[] objArr = new Object[4];
                objArr[0] = ignoreCase ? "case-insensitive " : "";
                objArr[1] = fieldName;
                objArr[2] = field;
                objArr[3] = conflictingFieldInfo == null ? null : conflictingFieldInfo.getField();
                Preconditions.checkArgument(z, str, objArr);
                this.nameToFieldInfoMap.put(fieldName, fieldInfo);
                nameSet.add(fieldName);
            }
        }
        this.names = nameSet.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList(nameSet));
    }
}
