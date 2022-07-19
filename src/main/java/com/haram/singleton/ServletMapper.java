package com.haram.singleton;

import com.haram.servlet.ServletMapping;
import com.haram.servlet.SimpleServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

import static com.haram.utils.ReflectionUtil.getClassesByAnnotion;

public class ServletMapper {
    private final static Logger logger = LoggerFactory.getLogger(ServletMapper.class);
    private HashMap<Object, SimpleServlet> sevletMap = new HashMap<>();

    public ServletMapper(){
        try {
            setServletMapper();
        }catch (Exception ex){
            logger.error("Failed to Mapping servlet.",ex);
        }
    }

    private void setServletMapper() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Set<Class> sevletClasses = getClassesByAnnotion(ServletMapping.class);

        for(Class<SimpleServlet> servletClass:sevletClasses){
            logger.debug(servletClass.getName());
            ServletMapping annotation = servletClass.getAnnotation(ServletMapping.class);

            Constructor constructor = servletClass.getConstructor();
            constructor.setAccessible(true);
            SimpleServlet simpleServlet = (SimpleServlet) constructor.newInstance();

            sevletMap.put(servletClass.getName(),simpleServlet);
            sevletMap.put(servletClass.getSimpleName(),simpleServlet);
            sevletMap.put(annotation.value(),simpleServlet);
        }
    }

    private static class SingletonClass {
        private static final ServletMapper instance = new ServletMapper();
    }

    public static ServletMapper getInstance() {
        return ServletMapper.SingletonClass.instance;
    }

    public HashMap<Object, SimpleServlet> getMapper(){
        return this.sevletMap;
    }
}
