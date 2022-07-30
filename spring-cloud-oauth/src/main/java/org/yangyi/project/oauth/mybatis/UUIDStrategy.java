package org.yangyi.project.oauth.mybatis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.UUID;

public class UUIDStrategy implements IdStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(UUIDStrategy.class);

    @Override
    public void handle(Object object, Field field) throws IllegalAccessException {
        if (!field.canAccess(object))
            field.setAccessible(true);
        if (field.get(object) != null) {
            LOGGER.warn("主键已赋值，不自动生成！");
            return;
        }
        field.set(object, UUID.randomUUID().toString().replace("-", ""));
    }
}
