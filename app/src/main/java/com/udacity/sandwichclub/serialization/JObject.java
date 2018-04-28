package com.udacity.sandwichclub.serialization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates the contents of a JSON object
 * Created by DI Ioffe on 4/28/2018.
 */

public class JObject{
    Map<String, Object> properties;
    String name;

    public void setProperty(String key, Object value){
        properties.put(key, value);
    }

    public JObject(String name){
        properties = new HashMap<>();
        this.name = name;
    }

    public JObject(){
        properties = new HashMap<>();
    }

    public String getName(){
        return name;
    }

    public String getString(String key){
        Object val = getValue(key);
        if(val instanceof String)
            return (String)val;

        return null;
    }

    public JObject getJObject(String key){
        Object val = getValue(key);
        if(val instanceof JObject)
            return (JObject)val;

        return null;
    }

    public List<?> getList(String key){
        Object val = getValue(key);
        if(val instanceof List)
            return (List<?>)val;

        return Collections.emptyList();
    }

    private Object getValue(String key){
        if(properties.containsKey(key))
            return properties.get(key);

        return null;
    }
}
