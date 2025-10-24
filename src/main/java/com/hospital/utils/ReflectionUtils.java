package com.hospital.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionUtils {
    
    public static String inspectClass(Class<?> clazz) {
        StringBuilder info = new StringBuilder();
        
        info.append("=== Class Information ===\n");
        info.append("Class Name: ").append(clazz.getName()).append("\n");
        info.append("Package: ").append(clazz.getPackage().getName()).append("\n");
        info.append("Superclass: ").append(clazz.getSuperclass() != null ? clazz.getSuperclass().getName() : "None").append("\n");
        info.append("Modifiers: ").append(Modifier.toString(clazz.getModifiers())).append("\n\n");
        
        info.append("=== Interfaces ===\n");
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            for (Class<?> iface : interfaces) {
                info.append("  - ").append(iface.getName()).append("\n");
            }
        } else {
            info.append("  No interfaces implemented\n");
        }
        info.append("\n");
        
        info.append("=== Fields ===\n");
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                info.append("  ").append(Modifier.toString(field.getModifiers()))
                    .append(" ").append(field.getType().getSimpleName())
                    .append(" ").append(field.getName()).append("\n");
            }
        } else {
            info.append("  No fields declared\n");
        }
        info.append("\n");
        
        info.append("=== Methods ===\n");
        Method[] methods = clazz.getDeclaredMethods();
        if (methods.length > 0) {
            for (Method method : methods) {
                info.append("  ").append(Modifier.toString(method.getModifiers()))
                    .append(" ").append(method.getReturnType().getSimpleName())
                    .append(" ").append(method.getName()).append("(");
                
                Class<?>[] params = method.getParameterTypes();
                for (int i = 0; i < params.length; i++) {
                    info.append(params[i].getSimpleName());
                    if (i < params.length - 1) info.append(", ");
                }
                info.append(")\n");
            }
        } else {
            info.append("  No methods declared\n");
        }
        
        return info.toString();
    }
    
    public static Object getFieldValue(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }
    
    public static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}
