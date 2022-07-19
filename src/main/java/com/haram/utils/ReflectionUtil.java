package com.haram.utils;

import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionUtil {

    public static final String DEFAULT_PACKAGE = "com.haram";


    public static Set<Class> getClassesByAnnotion(String packageName, Class annotation){
        Reflections reflections = new Reflections(packageName);
        return reflections.getTypesAnnotatedWith(annotation);
   	}

    public static Set<Class> getClassesByAnnotion(Class annotation){
        return getClassesByAnnotion(DEFAULT_PACKAGE,annotation);
   	}

    public static Set<Class> getClassesByPackage(String packageName){
        Reflections reflections = new Reflections(packageName);
        return reflections.getSubTypesOf(Object.class)
                .stream()
                .collect(Collectors.toSet());
   	}

    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
              + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }
}
