package com.fanqing.common;

import com.fanqing.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Querier {
    
    public static final String OPERATION = "operation";
    public static final String PROPERTY_NAME = "propertyName";
    public static final String PROPERTY_VALUE1 = "propertyValue1";
    public static final String PROPERTY_VALUE2 = "propertyValue2";
    public static final String CONJECTION_AND = "AND";
    public static final String CONJECTION_OR = "OR";
    
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    
    public String conjection =  CONJECTION_AND;

    private Integer page;

    private Integer size;

    public Map<String, String> orders = new LinkedHashMap<String, String>();

    private List<Map<String, Object>> conditions = new ArrayList<Map<String, Object>>();

    public Querier equal(String name, Object value) {
        if (null == value) {
            return this;
        }

        if (value instanceof String) {
            if (StringUtils.isBlank((String) value)) {
                return this;
            }
        }
        
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(OPERATION, "=");
        map.put(PROPERTY_NAME, name);
        map.put(PROPERTY_VALUE1, value);
        conditions.add(map);
        return this;
    }

    public Querier notEqual(String name, Object value) {
        if (null == value) {
            return this;
        }

        if (value instanceof String) {
            if (StringUtils.isBlank((String) value)) {
                return this;
            }
        }
        
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(OPERATION, "<>");
        map.put(PROPERTY_NAME, name);
        map.put(PROPERTY_VALUE1, value);
        conditions.add(map);
        return this;
    }
    
    public Querier like(String name, Object value) {
        if (null == value) {
            return this;
        }

        if (value instanceof String) {
            if (StringUtils.isBlank((String) value)) {
                return this;
            }
        }
        
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(OPERATION, "like");
        map.put(PROPERTY_NAME, name);
        map.put(PROPERTY_VALUE1, value);
        conditions.add(map);
        return this;
    }
    
    public Querier greaterThan(String name, Object value) {
        if (null == value) {
            return this;
        }

        if (value instanceof String) {
            if (StringUtils.isBlank((String) value)) {
                return this;
            }
        }
        
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(OPERATION, ">");
        map.put(PROPERTY_NAME, name);
        map.put(PROPERTY_VALUE1, value);
        conditions.add(map);
        return this;
    }
    
    public Querier lessThan(String name, Object value) {
        if (null == value) {
            return this;
        }

        if (value instanceof String) {
            if (StringUtils.isBlank((String) value)) {
                return this;
            }
        }
        
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(OPERATION, "<");
        map.put(PROPERTY_NAME, name);
        map.put(PROPERTY_VALUE1, value);
        conditions.add(map);
        return this;
    }
    
    public Querier greaterThanOrEqualTo(String name, Object value) {
        if (null == value) {
            return this;
        }

        if (value instanceof String) {
            if (StringUtils.isBlank((String) value)) {
                return this;
            }
        }
        
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(OPERATION, ">=");
        map.put(PROPERTY_NAME, name);
        map.put(PROPERTY_VALUE1, value);
        conditions.add(map);
        return this;
    }

    public Querier lessThanOrEqualTo(String name, Object value) {
        if (null == value) {
            return this;
        }

        if (value instanceof String) {
            if (StringUtils.isBlank((String) value)) {
                return this;
            }
        }
        
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(OPERATION, "<=");
        map.put(PROPERTY_NAME, name);
        map.put(PROPERTY_VALUE1, value);
        conditions.add(map);
        return this;
    }
    
    public Querier isNull(String name) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(OPERATION, "isNull");
        map.put(PROPERTY_NAME, name);
        map.put(PROPERTY_VALUE1, "");
        conditions.add(map);
        return this;
    }
    
    public Querier isNotNull(String name) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(OPERATION, "isNotNull");
        map.put(PROPERTY_NAME, name);
        map.put(PROPERTY_VALUE1, "");
        conditions.add(map);
        return this;
    }
    
    public Querier between(String name, Object value1,Object value2) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(OPERATION, "between");
        map.put(PROPERTY_NAME, name);
        map.put(PROPERTY_VALUE1, value1);
        map.put(PROPERTY_VALUE2, value2);
        conditions.add(map);
        return this;
    }
    
    public Querier in(String name, Collection<?> list) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(OPERATION, "in");
        map.put(PROPERTY_NAME, name);
        map.put(PROPERTY_VALUE1, list);
        conditions.add(map);
        return this;
    }
    
    public Querier in(String name, Object... ins ) {
        List<Object> list = Arrays.asList(ins);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(OPERATION, "in");
        map.put(PROPERTY_NAME, name);
        map.put(PROPERTY_VALUE1, list);
        conditions.add(map);
        return this;
    }
    
    public Querier findInSet(String name, Object value) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(OPERATION, "findInSet");
        map.put(PROPERTY_NAME, name);
        map.put(PROPERTY_VALUE1, value);
        conditions.add(map);
        return this;
    }
    
    public Querier andQuerier(Querier querier) {
        if(null==querier) {
            return this;
        }
        
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(OPERATION, "querier");
        map.put(PROPERTY_NAME, null);
        map.put(PROPERTY_VALUE1, querier);
        conditions.add(map);
        return this;
    }
    
    public Querier andQuerier(Querier querier, String name) {
        if(null==querier) {
            return this;
        }
        
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(OPERATION, "querier");
        map.put(PROPERTY_NAME, name);
        map.put(PROPERTY_VALUE1, querier);
        conditions.add(map);
        return this;
    }
    
    public Querier not(Querier querier) {
        if(null==querier) {
            return this;
        }
        
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put(OPERATION, "not");
        map.put(PROPERTY_NAME, null);
        map.put(PROPERTY_VALUE1, querier);
        conditions.add(map);
        return this;
    }

    public Querier isNullOrEqual(String name, Object value) {
        if (null == value) {
            return this;
        }

        if (value instanceof String) {
            if (StringUtils.isBlank((String) value)) {
                return this;
            }
        }

        Querier sq = new Querier();
        sq.setConjection(Querier.CONJECTION_OR);
        sq.isNull(name);
        sq.equal(name, value);
        andQuerier(sq, name);//不要动这块代码
        return this;
    }
    
    public Querier asc(String name) {
        orders.put(name, ASC);
        return this;
    }
    
    public Querier desc(String name) {
        orders.put(name, DESC);
        return this;
    }
    
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<Map<String, Object>> getConditions() {
        return conditions;
    }

    public void setConditions(List<Map<String, Object>> conditions) {
        this.conditions = conditions;
    }
    
    public Map<String, String> getOrders() {
        return orders;
    }

    public void setOrders(Map<String, String> orders) {
        this.orders = orders;
    }

    public void setPager(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }
    
    public Map<String, Object> getCondition(String key,String operation) {
        for (Map<String, Object> map : conditions) {
            if (null == map) {
                continue;
            }

            if (key.equals(map.get(PROPERTY_NAME)) && operation.equals(map.get(OPERATION))) {
                return map;
            }
        }

        return null;
    }
    
    public Object getConditionValue(String key,String operation) {
        Map<String, Object> orderMap = this.getCondition(key,operation);
        Object orderIdValue = orderMap.get(PROPERTY_VALUE1);
        return orderIdValue;
    }

    public String getConjection() {
        return conjection;
    }

    public void setConjection(String conjection) {
        this.conjection = conjection;
    }
}
