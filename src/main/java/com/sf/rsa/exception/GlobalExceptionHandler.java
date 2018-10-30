package com.sf.rsa.exception;

import com.sf.rsa.common.ErrorStatusEnum;
import com.sf.rsa.common.R;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Zidong on 2018/10/30 下午8:07
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * spring 请求参数绑定异常处理
     *
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public Object constraintViolationException(ConstraintViolationException exception) {
        List<String> invalidArguments = new ArrayList<>();

        Set<ConstraintViolation<?>> errors = exception.getConstraintViolations();
        for (ConstraintViolation<?> violation : errors) {
            String field = ((PathImpl) violation.getPropertyPath()).getLeafNode().toString();
            invalidArguments.add(field + ":" + violation.getMessage());
        }

        String errorStr = StringUtils.join(invalidArguments, ",");
        return R.error(errorStr, ErrorStatusEnum.INVALID_ARGUMENTS.getValue());
    }


}
