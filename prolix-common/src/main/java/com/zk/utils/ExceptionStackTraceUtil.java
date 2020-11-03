package com.zk.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取异常堆栈信息
 */
@Slf4j
public class ExceptionStackTraceUtil {

    private static final String CLASS_LINE_REGEX = "\\((\\w+\\.java:\\d+)\\)";// 匹配示例：(SyncMessageToWjsProcessor.java:1056)

    /**
     * 返回指定包名下的堆栈信息。若没有符合条件的堆栈信息，则返回最上面一条
     * 
     * @param throwable 异常对象
     * @param includePackage 指定包
     * @param limitLine 限制返回指定报名堆栈信息的条数
     * @return 返回示例如：at
     *         com.zhongan.castle.claim.front.service.AmethystService.pushHistoryCaseToWjs(AmethystService.java:160)
     */
    public static List<String> getIncludePackageClassStackTraceList(Throwable throwable, String includePackage,
                                                                    int limitLine) {
        List<String> result = new ArrayList<>();
        String stackTrace = getStackTrace(throwable);
        String[] startTraceArr = stackTrace.split("\n");
        if (startTraceArr.length == 0) {
            log.info("无堆栈信息");
            return Collections.emptyList();
        }

        // 匹配需要的对账内容
        int count = 0;
        for (String lineStackTrace : startTraceArr) {
            lineStackTrace = lineStackTrace.trim();
            // 筛选指定报下的堆栈信息
            if (lineStackTrace.contains(includePackage) && lineStackTrace.startsWith("at")
                    && Pattern.compile(CLASS_LINE_REGEX).matcher(lineStackTrace).find()) {
                result.add(lineStackTrace);
                if (++count == limitLine) {
                    break;
                }
            }
        }

        // 若没有匹配到，则返回第一条的堆栈信息
        if (result == null || result.isEmpty()) {
            if (startTraceArr.length > 2) {
                result.add(startTraceArr[1].trim());
            } else {
                result.add(startTraceArr[0].trim());
            }
        }
        return result;
    }

    /**
     * 返回指定包名下的简略堆栈信息
     *
     * @param throwable 异常
     * @param includePackage 指定包
     * @param limitLine 限制返回指定报名堆栈信息的条数
     * @return 返回示例如：SyncMessageToWjsProcessor.java:1056,AmethystService.java:160
     */
    public static String getSimpleIncludePackageClassStackTrace(Throwable throwable, String includePackage,
                                                                int limitLine) {
        List<String> traceLineList = getIncludePackageClassStackTraceList(throwable, includePackage, limitLine);
        if (traceLineList == null || traceLineList.isEmpty()) {
            return "";
        }
        Pattern pattern = Pattern.compile(CLASS_LINE_REGEX);
        return traceLineList.stream().map(traceLine -> {
            Matcher matcher = pattern.matcher(traceLine);
            if (matcher.find()) {
                return matcher.group(1);
            } else {
                return traceLine;
            }
        }).reduce((s1, s2) -> s1 + ", " + s2).orElse("");
    }

    /**
     * 返回异常堆栈信息
     * 
     * @param throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
}
