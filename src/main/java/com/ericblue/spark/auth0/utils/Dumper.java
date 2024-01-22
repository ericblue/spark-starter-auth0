package com.ericblue.spark.auth0.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Dumper {
    
    public static String Dump(Object obj) {
        return ToStringBuilder.reflectionToString(obj, ToStringStyle.MULTI_LINE_STYLE);
    }
    
    public static <T> String DumpMap(Map<T,T> map) {
        
        String template = "    %s=\"%s\"\n";
        StringBuilder sb = new StringBuilder();
        for (Entry<?, ?> e : map.entrySet()) {
            sb.append(String.format(template, e.getKey(), e.getValue()));
        }
      
        return sb.toString();
        
        
    }
    
    public static String DumpRecursive(Object o) {
        StringBuffer buffer = new StringBuffer();
        Class<?> oClass = o.getClass();
         if (oClass.isArray()) {
             buffer.append("Array: ");
            buffer.append("[");
            for (int i = 0; i < Array.getLength(o); i++) {
                Object value = Array.get(o, i);
                if (value.getClass().isPrimitive() ||
                        value.getClass() == java.lang.Long.class ||
                        value.getClass() == java.lang.Integer.class ||
                        value.getClass() == java.lang.Boolean.class ||
                        value.getClass() == java.lang.String.class ||
                        value.getClass() == java.lang.Double.class ||
                        value.getClass() == java.lang.Short.class ||
                        value.getClass() == java.lang.Byte.class
                        ) {
                    buffer.append(value);
                    if(i != (Array.getLength(o)-1)) buffer.append(",");
                } else {
                    buffer.append(Dump(value));
                 }
            }
            buffer.append("]\n");
        } else {
             buffer.append("Class: " + oClass.getName());
             buffer.append("{\n");
            while (oClass != null) {
                Field[] fields = oClass.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    buffer.append(fields[i].getName());
                    buffer.append("=");
                    try {
                        Object value = fields[i].get(o);
                        if (value != null) {
                            if (value.getClass().isPrimitive() ||
                                    value.getClass() == java.lang.Long.class ||
                                    value.getClass() == java.lang.String.class ||
                                    value.getClass() == java.lang.Integer.class ||
                                    value.getClass() == java.lang.Boolean.class ||
                                        value.getClass() == java.lang.Double.class ||
                                    value.getClass() == java.lang.Short.class ||
                                    value.getClass() == java.lang.Byte.class
                                    ) {
                                buffer.append(value);
                            } else {
                                buffer.append(Dump(value));
                            }
                        }
                    } catch (IllegalAccessException e) {
                        buffer.append(e.getMessage());
                    }
                    buffer.append("\n");
                }
                oClass = oClass.getSuperclass();
            }
            buffer.append("}\n");
        }
        return buffer.toString();
    }

}
