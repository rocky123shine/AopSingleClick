package com.example.aopsingleclick.aspect;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.aopsingleclick.R;
import com.example.aopsingleclick.annotation.SingleClickBehavior;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;


//切面
@Aspect
public class SingleClickAspect {
    private static final String TAG = "SingleClickAspect";
    private int viewId = R.integer.login_view_id;

    //1 找到切入点
    // exectue 以方法执行时作为切入点触发 Aspect类
    @Pointcut("execution(@com.example.aopsingleclick.annotation.SingleClickBehavior * *(..))")
    public void checkInterval() {
        System.out.println("checkInterval");
    }

    //2 处理切入点
    @Around("checkInterval()")
    public Object jointPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取方法名
        String simpleName = methodSignature.getDeclaringType().getSimpleName();
        //获取方法的注解值
        int intervalValue = methodSignature.getMethod().getAnnotation(SingleClickBehavior.class).value();
        if (intervalValue <= 0) {
            return joinPoint.proceed();
        } else {
            //时间间隔有效
            //获取view 设置tag 吧点击时间设置进入
            // 传进来的参数不是 View, 则直接执行
            Object[] args = joinPoint.getArgs();
            View view = getViewFromArgs(args);
            if (view == null) {
                Log.d(TAG, "view is null, proceed");
                return joinPoint.proceed();
            }

            Object viewTimeTag = view.getTag(viewId);
            // first click viewTimeTag is null.
            if (viewTimeTag == null) {
                Log.d(TAG, "lastClickTime is zero , proceed");
                Object proceed = joinPoint.proceed();
                // 重新赋值
                view.setTag(viewId, System.currentTimeMillis());
                return proceed;
            }
            long lastClickTime = (long) viewTimeTag;
            if (lastClickTime <= 0) {
                Log.d(TAG, "lastClickTime is zero , proceed");
                Object proceed = joinPoint.proceed();
                view.setTag(viewId, System.currentTimeMillis());
                return proceed;
            }

            // in limit time
            if (!canClick(lastClickTime, intervalValue)) {
                view.setTag(viewId, System.currentTimeMillis());
                Toast.makeText((Context) joinPoint.getThis(), "您点击的太快了", Toast.LENGTH_SHORT).show();
                System.out.println("您点击的太快了");
                return null;

            }else {
                view.setTag(viewId, System.currentTimeMillis());
                view.setEnabled(true);
                return joinPoint.proceed();
            }

        }
    }


    /**
     * 获取 view 参数
     *
     * @param args
     * @return
     */
    public View getViewFromArgs(Object[] args) {
        if (args != null && args.length > 0) {
            Object arg = args[0];
            if (arg instanceof View) {
                return (View) arg;
            }
        }
        return null;
    }

    /**
     * 判断是否达到可以点击的时间间隔
     *
     * @param lastClickTime
     * @return
     */
    public boolean canClick(long lastClickTime, int intervalTime) {
        long currentTime = System.currentTimeMillis();
        long realIntervalTime = currentTime - lastClickTime;
        return realIntervalTime >= intervalTime;
    }
}
