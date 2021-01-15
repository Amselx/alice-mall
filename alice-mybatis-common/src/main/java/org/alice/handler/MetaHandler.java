package org.alice.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *
 *
 * @author  Amse
 * @date  15/01/2021 18:14
 * @version 1.0
 */
@Slf4j
public class MetaHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insert:{}", metaObject.toString());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update:{}", metaObject.toString());

    }
}
