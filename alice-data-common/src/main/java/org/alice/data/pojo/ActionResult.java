package org.alice.data.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * The type Action result.
 *
 * @param <T> the type parameter
 * @author Amse
 * @version 1.0.0
 * @date 28 /01/2021 10:58
 */
@Data
@Builder
@AllArgsConstructor
public class ActionResult<T> {

    private int code;

    private String message;

    private T data;

    /**
     * 成功返回结果
     *
     * @param <T>  the type parameter
     * @param data 获取的数据
     * @return the action result
     */
    public static <T> ActionResult<T> success(T data) {
        return new ActionResult<T>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    /**
     * 成功返回结果
     *
     * @param <T>     the type parameter
     * @param data    获取的数据
     * @param message 提示信息
     * @return the action result
     */
    public static <T> ActionResult<T> success(T data, String message) {
        return new ActionResult<T>(HttpStatus.OK.value(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param <T>    the type parameter
     * @param status 错误码
     * @return the action result
     */
    public static <T> ActionResult<T> failed(HttpStatus status) {
        return new ActionResult<T>(status.value(), status.getReasonPhrase(), null);
    }

    /**
     * 失败返回结果
     *
     * @param <T>    the type parameter
     * @param status 错误码
     * @param data   the data
     * @return the action result
     */
    public static <T> ActionResult<T> failed(HttpStatus status, T data) {
        return new ActionResult<T>(status.value(), status.getReasonPhrase(), data);
    }

    /**
     * 失败返回结果
     *
     * @param <T>     the type parameter
     * @param status  错误码
     * @param message the message
     * @return the action result
     */
    public static <T> ActionResult<T> failed(HttpStatus status, String message) {
        return new ActionResult<T>(status.value(), message, null);
    }

    /**
     * 失败返回结果
     *
     * @param <T>     the type parameter
     * @param message 提示信息
     * @return the action result
     */
    public static <T> ActionResult<T> failed(String message) {
        return new ActionResult<T>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    /**
     * 失败返回结果
     *
     * @param <T> the type parameter
     * @return the action result
     */
    public static <T> ActionResult<T> failed() {
        return failed(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param <T> the type parameter
     * @return the action result
     */
    public static <T> ActionResult<T> validateFailed() {
        return failed(HttpStatus.PRECONDITION_FAILED);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param <T>     the type parameter
     * @param message 提示信息
     * @return the action result
     */
    public static <T> ActionResult<T> validateFailed(String message) {
        return failed(HttpStatus.PRECONDITION_FAILED, message);
    }

    /**
     * 未登录返回结果
     *
     * @param <T>  the type parameter
     * @param data the data
     * @return the action result
     */
    public static <T> ActionResult<T> unauthorized(T data) {
        return failed(HttpStatus.UNAUTHORIZED, data);
    }

    /**
     * 未登录返回结果
     *
     * @param <T> the type parameter
     * @return the action result
     */
    public static <T> ActionResult<T> unauthorized() {
        return failed(HttpStatus.UNAUTHORIZED);
    }

    /**
     * 未授权返回结果
     *
     * @param <T>  the type parameter
     * @param data the data
     * @return the action result
     */
    public static <T> ActionResult<T> forbidden(T data) {
        return failed(HttpStatus.FORBIDDEN, data);
    }

    /**
     * 未授权返回结果
     *
     * @param <T> the type parameter
     * @return the action result
     */
    public static <T> ActionResult<T> forbidden() {
        return failed(HttpStatus.FORBIDDEN);
    }

}
