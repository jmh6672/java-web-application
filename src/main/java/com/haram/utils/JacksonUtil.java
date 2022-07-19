package com.haram.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JacksonUtil {
    /**
    * Jackson의 JsonNode 를 map 으로 변환
    */
    public static Map<String, String> transformJsonToMap(JsonNode node, String prefix){

        Map<String,String> jsonMap = new HashMap<>();

        if(node.isArray()) { //JsonNode가 array 일떄
            //key에 인덱스를 붙여서 저장
            int i = 0;
            for (JsonNode arrayElement : node) {
                jsonMap.putAll(transformJsonToMap(arrayElement, prefix+"[" + i + "]"));
                i++;
            }
        }else if(node.isObject()) { //JsonNode가 entity 등의 객체일떄
            Iterator<String> fieldNames = node.fieldNames();
            String curPrefixWithDot = (prefix==null || prefix.trim().length()==0) ? "" : prefix+".";
            //json node 를 반복 처리
            while(fieldNames.hasNext()){
                String fieldName = fieldNames.next();
                JsonNode fieldValue = node.get(fieldName);
                jsonMap.putAll(transformJsonToMap(fieldValue, curPrefixWithDot+fieldName));
            }
        }else {
            jsonMap.put(prefix,node.asText());
        }

        return jsonMap;
    }

    public static Map<String, String> transformJsonToMap(JsonNode node){
        return transformJsonToMap(node,null);
    }
}
