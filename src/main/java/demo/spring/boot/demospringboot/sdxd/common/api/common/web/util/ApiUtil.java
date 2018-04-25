package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace.HttpTracer.DEBUG;

@Slf4j
public class ApiUtil {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String postJson(String url, Map<String, String> headers, String body) throws IOException {
        return post(url, headers, () -> RequestBody.create(JSON, body));
    }

    public static String postForm(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            params.forEach(builder::add);
        }
        return post(url, headers, builder::build);
    }

    public static String get(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OkHttpLogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().
                readTimeout(2, TimeUnit.MINUTES).
                addNetworkInterceptor(httpLoggingInterceptor).build();
        Request.Builder builder = new Request.Builder();
        if (headers != null) {
            headers.forEach(builder::addHeader);
        }
        String queryString = "";
        if (params != null && !params.isEmpty()) {
            queryString = params.entrySet().stream().reduce(
                    "",
                    (text, entry) -> StringUtils.isBlank(text) ?
                            String.format("%s=%s", entry.getKey(), entry.getValue()) :
                            String.format("&%s=%s", entry.getKey(), entry.getValue()),
                    (s1, s2) -> s1);
        }
        String fullUrl = StringUtils.isBlank(queryString) ? url : String.format("%s?%s", url, queryString);
        Request okRequest = builder.get().url(fullUrl).build();
        Response okResponse = client.newCall(okRequest).execute();
        ResponseBody responseBody = okResponse.body();
        return responseBody == null ? null : responseBody.string();
    }

    public static String post(String url, Map<String, String> headers, Supplier<RequestBody> bodySupplier) throws IOException {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OkHttpLogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().
                readTimeout(2, TimeUnit.MINUTES).
                addNetworkInterceptor(httpLoggingInterceptor).build();
        Request.Builder builder = new Request.Builder();
        if (headers != null) {
            headers.forEach(builder::addHeader);
        }
        RequestBody requestBody = bodySupplier.get();
        Request okRequest = builder.post(requestBody).url(url).build();
        Response okResponse = client.newCall(okRequest).execute();
        ResponseBody responseBody = okResponse.body();
        return responseBody == null ? null : responseBody.string();
    }

    private static class OkHttpLogger implements HttpLoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            DEBUG(log, "{}", message);
        }
    }
}
