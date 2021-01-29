package org.alice.web.config;

import lombok.extern.slf4j.Slf4j;
import org.alice.data.pojo.ActionResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.File;

/**
 * The type Response body advice.
 *
 * @author Amse
 * @version 1.0.0
 * @date 28 /01/2021 10:44
 */
@Slf4j
@ControllerAdvice
public class ResponseBodyAdvice implements org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converter) {
        return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converter);
    }

    @Override
    public Object beforeBodyWrite(Object result, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String path = serverHttpRequest.getURI().getPath();
        log.info("{} the response body: {}", path, result);
        if (result == null) {
            return ActionResult.success(HttpStatus.OK);
        }

        if (result instanceof ActionResult || result instanceof File) {
            return result;
        }

        return ActionResult.success(result);
    }
}
