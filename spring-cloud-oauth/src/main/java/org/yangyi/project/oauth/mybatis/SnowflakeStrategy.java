package org.yangyi.project.oauth.mybatis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yangyi.project.oauth.util.Sequence;

import java.lang.reflect.Field;

public class SnowflakeStrategy implements IdStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnowflakeStrategy.class);

    private final Sequence sequence = new Sequence(1, 1);

    @Override
    public void handle(Object object, Field field) throws IllegalAccessException {
        if (!field.canAccess(object))
            field.setAccessible(true);
        if (field.get(object) != null) {
            LOGGER.warn("主键已赋值，不自动生成！");
            return;
        }
        field.set(object, sequence.nextId());
    }

}
