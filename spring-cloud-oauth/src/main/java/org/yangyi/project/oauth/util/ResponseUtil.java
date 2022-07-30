package org.yangyi.project.oauth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.yangyi.project.oauth.vo.ResponseVO;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ResponseUtil {

    public static void okResponse(HttpServletResponse response, ResponseVO responseVO) {
        wrapResponse(response, HttpStatus.OK, responseVO);
    }

    public static void unauthorizedResponse(HttpServletResponse response, ResponseVO responseVO) {
        wrapResponse(response, HttpStatus.UNAUTHORIZED, responseVO);
    }

    public static void forbiddenResponse(HttpServletResponse response, ResponseVO responseVO) {
        wrapResponse(response, HttpStatus.FORBIDDEN, responseVO);
    }

    public static void methodNotAllowedResponse(HttpServletResponse response, ResponseVO responseVO) {
        wrapResponse(response, HttpStatus.METHOD_NOT_ALLOWED, responseVO);
    }

    public static void notFoundResponse(HttpServletResponse response, ResponseVO responseVO) {
        wrapResponse(response, HttpStatus.NOT_FOUND, responseVO);
    }

    public static void serverResponse(HttpServletResponse response, ResponseVO responseVO) {
        wrapResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, responseVO);
    }

    private static void wrapResponse(HttpServletResponse response, HttpStatus httpStatus, ResponseVO responseVO) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(httpStatus.value());
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            printWriter.write(new ObjectMapper().writeValueAsString(responseVO));
        } catch (Exception e) {
        }
        printWriter.flush();
        printWriter.close();
    }

}
