package com.haram.singleton;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haram.webserver.Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HostMapper {
    private final static Logger logger = LoggerFactory.getLogger(HostMapper.class);
    private final Environment environment = Environment.getInstance();
    private HashMap<String, Host> hostMap = new HashMap<>();

    public HostMapper(){
        setHostMapper();
    }

    private void setHostMapper(){
        //host 매핑
        try {
            HashMap serverMap = (HashMap) environment.get("server");
            TypeReference<ArrayList<Host>> typeRef = new TypeReference<ArrayList<Host>>() {};
            List<Host> hostList = new ObjectMapper().convertValue(serverMap.get("hosts"), typeRef);

            hostList.forEach(host -> hostMap.put(host.getName(), host));
        }catch (Exception ex){
            logger.error("Failed to Mapping Hosts.",ex);
        }
    }

    private static class SingletonClass {
        private static final HostMapper instance = new HostMapper();
    }

    public static HostMapper getInstance() {
        return HostMapper.SingletonClass.instance;
    }


    public HashMap<String, Host> getMapper(){
        return this.hostMap;
    }
}
