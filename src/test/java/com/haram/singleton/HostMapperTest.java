package com.haram.singleton;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haram.webserver.Host;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HostMapperTest {
    private final static Logger logger = LoggerFactory.getLogger(HostMapperTest.class);
    private final Environment environment = Environment.getInstance();

    HashMap<String, Host> hostMap = new HashMap<>();

    @Test
    public void setHostMappingTest() {
        try {
            HashMap serverMap = (HashMap) environment.get("server");

            TypeReference<ArrayList<Host>> typeRef = new TypeReference<ArrayList<Host>>() {};
            List<Host> hostList = new ObjectMapper().convertValue(serverMap.get("hosts"), typeRef);

            hostList.forEach(host -> hostMap.put(host.getName(), host));
        }catch (Exception ex){
            logger.error("Failed to Mapping Hosts.",ex);
        }

        Host host = hostMap.get("a.com");

        assertEquals("a.com", host.getName());
        assertEquals("/time", host.getServletMap().get("/Atime"));
    }


    @Test
    public void instanceHostMapperTest() {
        HostMapper hostMapper = HostMapper.getInstance();

        HashMap<String, Host> hostHashMap = hostMapper.getMapper();

        Host host = hostHashMap.get("a.com");

        assertEquals("a.com", host.getName());
        assertEquals("/time", host.getServletMap().get("/Atime"));
    }

}
