package com.atguigu.servicebase.exception;

import com.atguigu.commonutils.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * TODO
 *
 * @ClassName: GlobalExceptionHandler
 * @author: yan
 * @since: 2021/4/2 15:11
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        // TODO
        return R.error().message("执行了全局异常。。。");
    }

    //自定义异常
    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException e){
        e.printStackTrace();
        // TODO
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
