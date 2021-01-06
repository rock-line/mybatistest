package cn.kanade.mybatistest.service;

import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Log4j2
public abstract class BaseQueryService {

    private static final String LIKE = "like:";

    private static final String IN = "in:";

    private static final String LT = "lt:";

    private static final String GT = "gt:";

    protected <T> QueryWrapper<T> createQueryWrapper(String query,Class<T> tClass){
        //将json转换成map key为String value为object
        Map<String, Object> requestMap = JSONUtil.toBean(query, new TypeReference<Map<String, Object>>() {
        }, false);
        log.info("requestMap:{}",requestMap);
        QueryWrapper<T> queryWrapper=new QueryWrapper<>();
        for (Map.Entry<String, Object> object : requestMap.entrySet()) {
            //提取key作为查询条件
            String key = object.getKey();
            log.info("key:{}",key);
            //根据key 查询出在实体类中的属性 如 属性访问级别 private 属性类型 Integer 属性名 id
            Field field = ReflectUtil.getField(tClass, key);
            log.info("field:{}",field);
            if (field!=null){
                //获得属性具体的类型
                Class<?> columnType = field.getType();
                log.info("columnType:{}",columnType);
                //convert 放入对应的参数和类型 会将参数转化成对应的类型
                log.info("convert(object.getValue(), columnType)：{}",convert(object.getValue(), columnType));
                //构造查询条件
                queryWrapper.eq(StrUtil.toUnderlineCase(key),convert(object.getValue(), columnType));
                continue;
            }
            if (key.startsWith(LIKE)) {
                //将key拆分 去除like关键字 获取真实的key值
                String subPreKey = StrUtil.subAfter(key, LIKE, false);
                log.info("subPreKey:{}",subPreKey);
                //根据拆分后的key 查询出在实体类中的属性 如 属性访问级别 private 属性类型 Integer 属性名 id
                Field subField = ReflectUtil.getField(tClass, subPreKey);
                log.info("subField:{}",subField);
                if (subField != null) {
                    queryWrapper.like(StrUtil.toUnderlineCase(subPreKey),object.getValue());
                }
                continue;
            }
            if (key.startsWith(IN)) {
                String subPreKey = StrUtil.subAfter(key, IN, false);
                Field subField = ReflectUtil.getField(tClass, subPreKey);
                if (subField != null) {
                    Class<?> type = subField.getType();
                    List<?> in = JSONUtil.parseArray(object.getValue()).toList(type);
                    queryWrapper.in(StrUtil.toUnderlineCase(subPreKey),in);
                }
                continue;
            }
            if (key.startsWith(LT)) {
                String subPreKey = StrUtil.subAfter(key, LT, false);
                Field subField = ReflectUtil.getField(tClass, subPreKey);
                if (subField != null) {
                    Class<?> type = subField.getType();
                    Object typeInstance = convert(object.getValue(), type);
                    queryWrapper.lt(StrUtil.toUnderlineCase(subPreKey),convert(typeInstance, Comparable.class));
                }
                continue;
            }
            if (key.startsWith(GT)) {
                String subPreKey = StrUtil.subAfter(key, GT, false);
                Field subField = ReflectUtil.getField(tClass, subPreKey);
                if (subField != null) {
                    Class<?> type = subField.getType();
                    Object typeInstance = convert(object.getValue(), type);
                    queryWrapper.gt(StrUtil.toUnderlineCase(subPreKey),convert(typeInstance,Comparable.class));
                }
            }
        }
        return queryWrapper;
    }

    private <T> T convert(Object obj, Class<T> tClass) {
        ConverterRegistry instance = ConverterRegistry.getInstance();
        return instance.convert(tClass, obj);
    }
}
