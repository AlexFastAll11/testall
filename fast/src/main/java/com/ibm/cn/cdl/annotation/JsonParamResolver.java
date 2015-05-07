package com.ibm.cn.cdl.annotation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class JsonParamResolver implements HandlerMethodArgumentResolver {

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JsonParam.class);
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Class<?> paramType = parameter.getParameterType();
        JsonParam methodAnnotation = parameter.getParameterAnnotation(JsonParam.class);

        if(!StringUtils.isEmpty(methodAnnotation.value())){
            Map<String, String[]> parameterMap = webRequest.getParameterMap();
            String[] jsons = parameterMap.get(methodAnnotation.value());
            if(jsons == null || StringUtils.isEmpty(jsons[0])){
                if(methodAnnotation.require()){
                    throw new RuntimeException("JsonParam require but is null or ''");
                }else{
                    return null;
                }
            }
            return parseJSON(paramType,jsons[0] , parameter);
        }else{
            HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
            BufferedReader reader = nativeRequest.getReader();
            StringBuilder builder = new StringBuilder();
            CharStreams.copy(reader, builder);
            String json = builder.toString();
            if(StringUtils.isEmpty(json)){
                if(methodAnnotation.require()){
                    throw new RuntimeException("JsonParam require but is null or ''");
                }else{
                    return null;
                }
            }
            return parseJSON(paramType , json , parameter);
        }
    }

    private Object parseJSON(Class<?> paramType , String json , MethodParameter parameter){
        if(JSONObject.class.isAssignableFrom(paramType)){
            return JSON.parseObject(json);
            //array
        }
        else if(paramType.isArray()){
            Class<?> componentType = paramType.getComponentType();
            List<?> objects = JSON.parseArray(json, componentType);
            Object[] os = (Object[]) Array.newInstance(componentType, objects.size());
            for(int i = 0; i< objects.size() ; i++){
                os[i] = objects.get(i);
            }
            return os;
            //list
        }else if(List.class.isAssignableFrom(paramType)){
            Type[] types = ((ParameterizedType)parameter.getGenericParameterType()).getActualTypeArguments();
            return JSON.parseArray(json, types);
            //set
        }else if(Set.class.isAssignableFrom(paramType)){
            Type[] types = ((ParameterizedType)parameter.getGenericParameterType()).getActualTypeArguments();
            return Sets.newHashSet(JSON.parseArray(json, types).iterator());
        }
        return JSON.parseObject(json , paramType);
    }


}
