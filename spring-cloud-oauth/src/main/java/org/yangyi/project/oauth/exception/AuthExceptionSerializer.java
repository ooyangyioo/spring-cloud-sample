package org.yangyi.project.oauth.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * AuthException 的序列化
 */
public class AuthExceptionSerializer extends StdSerializer<AuthException> {

    protected AuthExceptionSerializer() {
        super(AuthException.class);
    }

    @Override
    public void serialize(AuthException e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("code", e.getHttpErrorCode());
        jsonGenerator.writeStringField("msg", e.getSummary());
        jsonGenerator.writeStringField("path", request.getServletPath());
        jsonGenerator.writeStringField("timestamp", String.valueOf(new Date().getTime()));
        if (e.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : e.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                jsonGenerator.writeStringField(key, add);
            }
        }
        jsonGenerator.writeEndObject();
    }
}
