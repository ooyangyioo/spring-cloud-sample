package org.yangyi.project.oauth.mybatis;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class
        })
})
public class AutoIdInterceptor implements Interceptor {

    private static SnowflakeStrategy snowflakeStrategy = new SnowflakeStrategy();
    private static UUIDStrategy uuidStrategy = new UUIDStrategy();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object entity = args[1];
        if (mappedStatement.getSqlCommandType().equals(SqlCommandType.INSERT)) {
            // 获取实体集合
            Set<Object> entitySet = new HashSet<>();
            if (entity instanceof Map) {    //  批量插入对象
                Collection values = (Collection) ((Map) entity).get("list");
                for (Object value : values) {
                    if (value instanceof Collection) {
                        entitySet.addAll((Collection) value);
                    } else {
                        entitySet.add(value);
                    }
                }
            } else {    //  单个插入对象
                entitySet.add(entity);
            }
            // 批量设置id
            for (Object object : entitySet)
                process(object);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private void process(Object object) throws Throwable {
        // 反射工具类 获取带有AutoId注解的所有属性字段
        Set<Field> allFields = ReflectionUtils.getAllFields(object.getClass(), input -> input != null && input.getAnnotation(AutoId.class) != null);
        for (Field field : allFields) {
            AutoId annotation = field.getAnnotation(AutoId.class);
            if (annotation.value().equals(IdType.UUID)) {   //  UUID作为主键
                uuidStrategy.handle(object, field);
            } else if (annotation.value().equals(IdType.SNOWFLAKE)) {   //  雪花ID作为主键
                snowflakeStrategy.handle(object, field);
            }
        }
    }

}
