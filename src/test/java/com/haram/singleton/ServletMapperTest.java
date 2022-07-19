package com.haram.singleton;

import com.haram.servlet.ServletMapping;
import com.haram.servlet.SimpleServlet;
import com.haram.servlet.Time;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

import static com.haram.utils.ReflectionUtil.getClassesByAnnotion;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServletMapperTest {
    private final static Logger logger = LoggerFactory.getLogger(ServletMapperTest.class);
    private HashMap<Object, SimpleServlet> sevletMap = new HashMap<>();


    @Test
    public void setServletMapperTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
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

            assertEquals(simpleServlet, sevletMap.get(servletClass.getName()));
            assertEquals(simpleServlet, sevletMap.get(servletClass.getSimpleName()));
            assertTrue(annotation.value().startsWith("/"));
        }
    }


    @Test
    public void instanceServletMapperTest() {
        ServletMapper servletMapper = ServletMapper.getInstance();

        HashMap<Object, SimpleServlet> map = servletMapper.getMapper();

        SimpleServlet servlet = map.get("/time");
        assertTrue(servlet instanceof Time);

        servlet = map.get("Time");
        assertTrue(servlet instanceof Time);

        servlet = map.get("com.haram.servlet.Time");
        assertTrue(servlet instanceof Time);
    }
}
