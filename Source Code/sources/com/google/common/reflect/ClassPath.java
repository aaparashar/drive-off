package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet.Builder;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@Beta
public final class ClassPath {
    private static final String CLASS_FILE_NAME_EXTENSION = ".class";
    private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();
    private static final Logger logger = Logger.getLogger(ClassPath.class.getName());
    private final ImmutableSet<ResourceInfo> resources;

    @Beta
    public static class ResourceInfo {
        final ClassLoader loader;
        private final String resourceName;

        static ResourceInfo of(String resourceName, ClassLoader loader) {
            if (!resourceName.endsWith(ClassPath.CLASS_FILE_NAME_EXTENSION) || resourceName.contains("$")) {
                return new ResourceInfo(resourceName, loader);
            }
            return new ClassInfo(resourceName, loader);
        }

        ResourceInfo(String resourceName, ClassLoader loader) {
            this.resourceName = (String) Preconditions.checkNotNull(resourceName);
            this.loader = (ClassLoader) Preconditions.checkNotNull(loader);
        }

        public final URL url() {
            return (URL) Preconditions.checkNotNull(this.loader.getResource(this.resourceName), "Failed to load resource: %s", this.resourceName);
        }

        public final String getResourceName() {
            return this.resourceName;
        }

        public int hashCode() {
            return this.resourceName.hashCode();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ResourceInfo)) {
                return false;
            }
            ResourceInfo that = (ResourceInfo) obj;
            if (this.resourceName.equals(that.resourceName) && this.loader == that.loader) {
                return true;
            }
            return false;
        }

        public String toString() {
            return this.resourceName;
        }
    }

    @Beta
    public static final class ClassInfo extends ResourceInfo {
        private final String className;

        ClassInfo(String resourceName, ClassLoader loader) {
            super(resourceName, loader);
            this.className = ClassPath.getClassName(resourceName);
        }

        public String getPackageName() {
            return Reflection.getPackageName(this.className);
        }

        public String getSimpleName() {
            String packageName = getPackageName();
            if (packageName.isEmpty()) {
                return this.className;
            }
            return this.className.substring(packageName.length() + 1);
        }

        public String getName() {
            return this.className;
        }

        public Class<?> load() {
            try {
                return this.loader.loadClass(this.className);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }

        public String toString() {
            return this.className;
        }
    }

    private ClassPath(ImmutableSet<ResourceInfo> resources) {
        this.resources = resources;
    }

    public static ClassPath from(ClassLoader classloader) throws IOException {
        Builder<ResourceInfo> resources = new Builder(Ordering.usingToString());
        Iterator i$ = getClassPathEntries(classloader).entrySet().iterator();
        while (i$.hasNext()) {
            Entry<URI, ClassLoader> entry = (Entry) i$.next();
            browse((URI) entry.getKey(), (ClassLoader) entry.getValue(), resources);
        }
        return new ClassPath(resources.build());
    }

    public ImmutableSet<ResourceInfo> getResources() {
        return this.resources;
    }

    public ImmutableSet<ClassInfo> getTopLevelClasses() {
        ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
        Iterator i$ = this.resources.iterator();
        while (i$.hasNext()) {
            ResourceInfo resource = (ResourceInfo) i$.next();
            if (resource instanceof ClassInfo) {
                builder.add((ClassInfo) resource);
            }
        }
        return builder.build();
    }

    public ImmutableSet<ClassInfo> getTopLevelClasses(String packageName) {
        Preconditions.checkNotNull(packageName);
        ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
        Iterator i$ = getTopLevelClasses().iterator();
        while (i$.hasNext()) {
            Object classInfo = (ClassInfo) i$.next();
            if (classInfo.getPackageName().equals(packageName)) {
                builder.add(classInfo);
            }
        }
        return builder.build();
    }

    public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(String packageName) {
        Preconditions.checkNotNull(packageName);
        String packagePrefix = packageName + '.';
        ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
        Iterator i$ = getTopLevelClasses().iterator();
        while (i$.hasNext()) {
            Object classInfo = (ClassInfo) i$.next();
            if (classInfo.getName().startsWith(packagePrefix)) {
                builder.add(classInfo);
            }
        }
        return builder.build();
    }

    @VisibleForTesting
    static ImmutableMap<URI, ClassLoader> getClassPathEntries(ClassLoader classloader) {
        LinkedHashMap<URI, ClassLoader> entries = Maps.newLinkedHashMap();
        ClassLoader parent = classloader.getParent();
        if (parent != null) {
            entries.putAll(getClassPathEntries(parent));
        }
        if (classloader instanceof URLClassLoader) {
            URL[] arr$ = ((URLClassLoader) classloader).getURLs();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                try {
                    URI uri = arr$[i$].toURI();
                    if (!entries.containsKey(uri)) {
                        entries.put(uri, classloader);
                    }
                    i$++;
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
        return ImmutableMap.copyOf(entries);
    }

    private static void browse(URI uri, ClassLoader classloader, ImmutableSet.Builder<ResourceInfo> resources) throws IOException {
        if (uri.getScheme().equals("file")) {
            browseFrom(new File(uri), classloader, resources);
        }
    }

    @VisibleForTesting
    static void browseFrom(File file, ClassLoader classloader, ImmutableSet.Builder<ResourceInfo> resources) throws IOException {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            browseDirectory(file, classloader, resources);
        } else {
            browseJar(file, classloader, resources);
        }
    }

    private static void browseDirectory(File directory, ClassLoader classloader, ImmutableSet.Builder<ResourceInfo> resources) {
        browseDirectory(directory, classloader, "", resources);
    }

    private static void browseDirectory(File directory, ClassLoader classloader, String packagePrefix, ImmutableSet.Builder<ResourceInfo> resources) {
        for (File f : directory.listFiles()) {
            String name = f.getName();
            if (f.isDirectory()) {
                browseDirectory(f, classloader, packagePrefix + name + "/", resources);
            } else {
                resources.add(ResourceInfo.of(packagePrefix + name, classloader));
            }
        }
    }

    private static void browseJar(File file, ClassLoader classloader, ImmutableSet.Builder<ResourceInfo> resources) throws IOException {
        try {
            JarFile jarFile = new JarFile(file);
            try {
                Iterator i$ = getClassPathFromManifest(file, jarFile.getManifest()).iterator();
                while (i$.hasNext()) {
                    browse((URI) i$.next(), classloader, resources);
                }
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = (JarEntry) entries.nextElement();
                    if (!(entry.isDirectory() || entry.getName().startsWith("META-INF/"))) {
                        resources.add(ResourceInfo.of(entry.getName(), classloader));
                    }
                }
            } finally {
                try {
                    jarFile.close();
                } catch (IOException e) {
                }
            }
        } catch (IOException e2) {
        }
    }

    @VisibleForTesting
    static ImmutableSet<URI> getClassPathFromManifest(File jarFile, @Nullable Manifest manifest) {
        if (manifest == null) {
            return ImmutableSet.of();
        }
        ImmutableSet.Builder<URI> builder = ImmutableSet.builder();
        String classpathAttribute = manifest.getMainAttributes().getValue("Class-Path");
        if (classpathAttribute != null) {
            for (String path : CLASS_PATH_ATTRIBUTE_SEPARATOR.split(classpathAttribute)) {
                try {
                    builder.add(getClassPathEntry(jarFile, path));
                } catch (URISyntaxException e) {
                    logger.warning("Invalid Class-Path entry: " + path);
                }
            }
        }
        return builder.build();
    }

    @VisibleForTesting
    static URI getClassPathEntry(File jarFile, String path) throws URISyntaxException {
        URI uri = new URI(path);
        return uri.isAbsolute() ? uri : new File(jarFile.getParentFile(), path.replace('/', File.separatorChar)).toURI();
    }

    @VisibleForTesting
    static String getClassName(String filename) {
        return filename.substring(0, filename.length() - CLASS_FILE_NAME_EXTENSION.length()).replace('/', '.');
    }
}
