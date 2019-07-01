package com.zk.feign.protogenesis;

import okhttp3.*;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.TimeUnit;

//@SuppressWarnings("all")
public class HttpUtil {

//    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            .readTimeout(120000, TimeUnit.MILLISECONDS)
            .writeTimeout(120000, TimeUnit.MICROSECONDS)
            .build();

    private static final Map<String, OkHttpClient> clientHolder = new HashMap<String, OkHttpClient>();

    private static OkHttpClient getClient(long connTimeoutMills, long readTimeoutMills, long writeTimeoutMills) {
        String key = connTimeoutMills + "&" + readTimeoutMills + "&" + writeTimeoutMills;
        Object value = clientHolder.get(key);
        if (value == null) {
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .connectTimeout(connTimeoutMills, TimeUnit.MILLISECONDS)
                    .readTimeout(readTimeoutMills, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeoutMills, TimeUnit.MICROSECONDS)
                    .build();
            clientHolder.put(key, httpClient);
            value = httpClient;
        }
        return (OkHttpClient) value;
    }

    /**
     * post携带josn字符串请求
     *
     * @param url:请求地址
     * @param postBodyJson:请求参数(json字符串)
     * @param headers:请求头
     * @param connTimeoutMills:连接超时时间(单位:毫秒)
     * @param readTimeoutMills:读取超时时间(单位:毫秒)
     * @return
     * @throws IOException
     */
    public static String post(String url, String postBodyJson, Map<String, Object> headers,
                              long connTimeoutMills, long readTimeoutMills) throws IOException {
        OkHttpClient client = getClient(connTimeoutMills, readTimeoutMills, 0);

        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        Request.Builder reqBuilder = new Request.Builder().url(url);

        //add request headers
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                reqBuilder = reqBuilder.addHeader(key, (String) headers.get(key));
            }
        }

        Request request = reqBuilder.post(RequestBody.create(MEDIA_TYPE_JSON, postBodyJson)).build();
        Response response = client.newCall(request).execute();
        String returnString = response.body().string();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);

        return returnString;
    }

    /**
     * post携带josn字符串请求,使用默认超时时间
     *
     * @param url:请求地址
     * @param postBodyJson:请求参数(json字符串)
     * @param headers:请求头
     * @return
     * @throws IOException
     */
    public static String post(String url, String postBodyJson, Map<String, Object> headers) throws IOException {
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        Request.Builder reqBuilder = new Request.Builder().url(url);

        //add request headers
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                reqBuilder = reqBuilder.addHeader(key, (String) headers.get(key));
            }
        }
        Request request = reqBuilder.post(RequestBody.create(MEDIA_TYPE_JSON, postBodyJson)).build();
        Response response = client.newCall(request).execute();
        String returnString = response.body().string();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);

        return returnString;
    }

    /**
     * post表单请求
     *
     * @param url:请求地址
     * @param params:请求参数(key-value型,即form表单)
     * @param headers:请求头
     * @param connTimeoutMills:连接超时时间(单位:毫秒)
     * @param readTimeoutMills:读取超时时间(单位:毫秒)
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, Object> params, Map<String, Object> headers,
                              long connTimeoutMills, long readTimeoutMills) throws IOException {
        OkHttpClient client = getClient(connTimeoutMills, readTimeoutMills, 0);

        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        Request.Builder reqBuilder = new Request.Builder().url(url);

        RequestBody requestBody = null;
        //add request params
        if (params != null) {
            FormBody.Builder builder = new FormBody.Builder();
            Set<String> keys = params.keySet();
            for (String key : keys) {
                builder = builder.add(key, (String) params.get(key));
            }
            requestBody = builder.build();
        }

        //add request headers
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                reqBuilder = reqBuilder.addHeader(key, (String) headers.get(key));
            }
        }

        Request request = reqBuilder.post(requestBody).build();
        Response response = client.newCall(request).execute();
        String returnString = response.body().string();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);

        return returnString;
    }

    /**
     * post表单请求
     *
     * @param url:请求地址
     * @param params:请求参数(key-value型,即form表单)
     * @param connTimeoutMills:连接超时时间(单位:毫秒)
     * @param readTimeoutMills:读取超时时间(单位:毫秒)
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, Object> params,
                              long connTimeoutMills, long readTimeoutMills) throws IOException {
        OkHttpClient client = getClient(connTimeoutMills, readTimeoutMills, 0);

        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        Request.Builder reqBuilder = new Request.Builder().url(url);

        RequestBody requestBody = null;
        //add request params
        if (params != null) {
            FormBody.Builder builder = new FormBody.Builder();
            Set<String> keys = params.keySet();
            for (String key : keys) {
                builder = builder.add(key, (String) params.get(key));
            }
            requestBody = builder.build();
        }

        Request request = reqBuilder.post(requestBody).build();
        Response response = client.newCall(request).execute();
        String returnString = response.body().string();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);

        return returnString;
    }

    public static String post(String url) throws IOException {
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        Request.Builder reqBuilder = new Request.Builder().url(url);

        Request request = null;
        request = reqBuilder.post(RequestBody.create(null, "")).build();

        Response response = timeOutResend(request, client, 3);
        String returnString = response.body().string();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);

        return returnString;
    }

    /**
     * post表单请求,使用默认超时时间
     *
     * @param url:请求地址
     * @param params:请求参数(key-value型,即form表单)
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, Object> params) throws IOException {
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        Request.Builder reqBuilder = new Request.Builder().url(url);

        RequestBody requestBody = null;
        //add request params
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                builder = builder.add(key, (String) params.get(key));
            }
        }
        requestBody = builder.build();

        Request request = reqBuilder.post(requestBody).build();
        Response response = client.newCall(request).execute();
        String returnString = response.body().string();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);

        return returnString;
    }

    /**
     * @param url:请求地址
     * @param params:请求参数
     * @param headers:请求头
     * @param connTimeoutMills:连接超时时间(单位:毫秒)
     * @param readTimeoutMills:读取超时时间(单位:毫秒)
     * @return
     * @throws IOException
     */
    public static String get(String url, HashMap<String, Object> params, Map<String, Object> headers,
                             long connTimeoutMills, long readTimeoutMills) throws IOException {
        OkHttpClient client = getClient(connTimeoutMills, readTimeoutMills, 0);

        //add request params
        if (params != null) {
            Set<String> keys = params.keySet();
            StringBuffer paramString = new StringBuffer();
            for (String key : keys) {
                paramString.append(key + "=" + params.get(key) + "&");
            }
            url = url + "?" + paramString.substring(0, paramString.length() - 1);
        }
        Request.Builder reqBuilder = new Request.Builder().url(url);

        //add request headers
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                reqBuilder = reqBuilder.addHeader(key, (String) headers.get(key));
            }
        }

        Request request = reqBuilder.build();
        Response response = null;
        response = client.newCall(request).execute();
        String returnString = response.body().string();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);
        return returnString;
    }

    /**
     * get请求,使用默认超时
     *
     * @param url:请求地址
     * @param params:请求参数
     * @param headers:请求头
     * @return
     * @throws IOException
     */
    public static String get(String url, HashMap<String, Object> params, Map<String, Object> headers) throws IOException {
        //add request params
        if (params != null) {
            Set<String> keys = params.keySet();
            StringBuffer paramString = new StringBuffer();
            for (String key : keys) {
                paramString.append(key + "=" + params.get(key) + "&");
            }
            url = url + "?" + paramString.substring(0, paramString.length() - 1);
        }
        Request.Builder reqBuilder = new Request.Builder().url(url);

        //add request headers
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                reqBuilder = reqBuilder.addHeader(key, (String) headers.get(key));
            }
        }

        Request request = reqBuilder.build();
        Response response = null;
        response = client.newCall(request).execute();
        String returnString = response.body().string();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);
        return returnString;
    }

    /**
     * get请求,使用默认超时
     *
     * @param url:请求地址
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        Request.Builder reqBuilder = new Request.Builder().url(url);

        Request request = reqBuilder.build();
        Response response = null;
        response = timeOutResend(request, client, 3);
        String returnString = response.body().string();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);
        return returnString;
    }

    /**
     * get请求,使用默认超时
     *
     * @param url:请求地址
     * @return
     * @throws IOException
     */
    public static byte[] getByte(String url) throws IOException {
        Request.Builder reqBuilder = new Request.Builder().url(url);

        Request request = reqBuilder.build();
        Response response = null;
        response = timeOutResend(request, client, 3);
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);
        byte[] bytes = response.body().bytes();
        return bytes;
    }

    /**
     * @param url
     * @param files      key：文件名，value：文件
     * @param formParams 普通form参数
     * @param headers    http表头
     * @return
     * @throws Exception
     */
    public static byte[] uploadFile(String url, List<byte[]> files,
                                    Map<String, Object> formParams, Map<String, Object> headers,
                                    long connTimeoutMills, long readTimeoutMills) throws IOException {
        OkHttpClient client = getClient(connTimeoutMills, readTimeoutMills, 0);

        /* form的分割线,自己定义 */
        String boundary = "xx--------------------------------------------------------------xx";

        MultipartBody.Builder builder = new MultipartBody.Builder(boundary).setType(MultipartBody.FORM);

        //添加普通表单参数
        if (formParams != null) {
            for (String key : formParams.keySet()) {
                String value = formParams.get(key).toString();
                builder.addFormDataPart(key, value);
            }
        }
        //添加上传的文件
        for (byte[] file : files) {
//            File file = files.get(fileName);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            builder.addFormDataPart("file", UUID.randomUUID().toString(), requestBody);
        }

        MultipartBody mBody = builder.build();

        //下边的就和post一样了
        Request.Builder reqBuilder = new Request.Builder().url(url);

        //add request headers
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                reqBuilder = reqBuilder.addHeader(key, (String) headers.get(key));
            }
        }

        Request request = reqBuilder.post(mBody).build();

        Response response = null;
        response = client.newCall(request).execute();
        byte[] result = response.body().bytes();
//        response.body().bytes();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);
        return result;
    }


    /**
     * post携带josn字符串请求,使用默认超时时间
     *
     * @param url:请求地址
     * @param postBodyJson:请求参数(json字符串)
     * @param headers:请求头
     * @return
     * @throws IOException
     */
    public static String put(String url, String postBodyJson, Map<String, Object> headers) throws IOException {
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        Request.Builder reqBuilder = new Request.Builder().url(url);

        //add request headers
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                reqBuilder = reqBuilder.addHeader(key, (String) headers.get(key));
            }
        }
        Request request = reqBuilder.put(RequestBody.create(MEDIA_TYPE_JSON, postBodyJson)).build();
        Response response = timeOutResend(request, client, 3);
        String returnString = response.body().string();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);

        return returnString;
    }

    /**
     * 超时重发
     * @param request
     * @param client
     * @param resendTimes 重发次数
     * @return
     * @throws Exception
     */
    private static Response timeOutResend(Request request, OkHttpClient client, int resendTimes) throws IOException {
        Response result = null;
        try {
            result = client.newCall(request).execute();
        } catch (IOException e) {
            if (e instanceof SocketTimeoutException && resendTimes > 1) {
                result = timeOutResend(request, client, resendTimes - 1);
            } else {
                throw e;
            }
        }
        return result;
    }
}
