package com.haram.webserver;

import java.util.HashMap;
import java.util.Map;

public class Host {
    private String name;

    private String root;

    private Map<String, String> servletMap = new HashMap<>();
    private Map<Integer, String> errorServletMap = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public Map<String, String> getServletMap() {
        return this.servletMap;
    }

    public Map<Integer, String> getErrorServletMap() {
        return this.errorServletMap;
    }

    public void setMapping(Map<Object,Object> mapping){
        setErrorServletMap((Map<String,String>) mapping.get("error"));
        setServletMap((Map<String,String>) mapping.get("path"));
    }

    private void setErrorServletMap(Map<String,String> map){
        for(String key:map.keySet()){
            errorServletMap.put(Integer.parseInt(key),map.get(key));
        }
    }

    private void setServletMap(Map<String,String> map){
        servletMap.putAll(map);
    }
}
