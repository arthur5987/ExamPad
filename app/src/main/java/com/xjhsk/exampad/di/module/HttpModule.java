package com.xjhsk.exampad.di.module;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.xjhsk.exampad.BuildConfig;
import com.xjhsk.exampad.api.Api;
import com.xjhsk.exampad.app.AppContext;
import com.xjhsk.exampad.di.qualifier.ExamUrl;
import com.xjhsk.exampad.model.http.api.ExamApis;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：weidingqiang on 2017/7/10 14:44
 * 邮箱：dqwei@iflytek.com
 */
@Module
public class HttpModule {

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }


    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpBuilder() {
        return new OkHttpClient.Builder();
    }

    @Singleton
    @Provides
    OkHttpClient provideClient(OkHttpClient.Builder builder) {

        OkHttpClient okHttpClient = RetrofitUrlManager.getInstance().with(builder).build();

        File cacheFile =  new File(AppContext.getInstance().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        //设置缓存
        builder.addInterceptor(Api.mRewriteCacheControlInterceptor);
        builder.addNetworkInterceptor(Api.mRewriteCacheControlInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(Api.CONNECT_TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(Api.READ_TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);

        return okHttpClient;

//        if (BuildConfig.DEBUG) {
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
//            builder.addInterceptor(loggingInterceptor);
//        }
//        File cacheFile =  new File(AppContext.getInstance().getCacheDir(), "cache");
//        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
//        //增加头部信息
//        //需要在请求中添加token
////        Interceptor headerInterceptor =new Interceptor() {
////            @Override
////            public Response intercept(Chain chain) throws IOException {
////                Request build;
////                if(EmptyUtils.isEmpty(AppContext.getInstance().getUser().getToken()))
////                {
////                    build = chain.request().newBuilder()
////                            .addHeader("Content-Type", "application/json")
//////                        .addHeader("X-Token", AppContext.getInstance().getUser().getToken())
////                            .build();
////                }
////                else {
////                     build = chain.request().newBuilder()
////                            .addHeader("Content-Type", "application/json")
////                            .addHeader("X-Token", AppContext.getInstance().getUser().getToken())
////                            .build();
////                }
////
////                return chain.proceed(build);
////            }
////        };
////
////        builder.addInterceptor(headerInterceptor);
//
//        ClearableCookieJar cookieJar =
//                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(AppContext.getInstance().getApplicationContext()));
//        builder.cookieJar(cookieJar);
//
////        Interceptor apikey = new Interceptor() {
////            @Override
////            public Response intercept(Chain chain) throws IOException {
////                Request request = chain.request();
////                request = request.newBuilder()
////                        .addHeader("apikey",Constants.KEY_API)
////                        .build();
////                return chain.proceed(request);
////            }
////        }
////        设置统一的请求头部参数
////        builder.addInterceptor(apikey);
//
//        //cookie 持久化管理
//        //https://segmentfault.com/a/1190000004345545
//        //http://blog.csdn.net/shengfakun1234/article/details/54615592
////        builder.cookieJar(new CookieJar() {
////                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
////
////                @Override
////                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
////                    cookieStore.put(url.host(), cookies);
////                }
////
////                @Override
////                public List<Cookie> loadForRequest(HttpUrl url) {
////                    List<Cookie> cookies = cookieStore.get(url.host());
////                    return cookies != null ? cookies : new ArrayList<Cookie>();
////                }
////            });
//
////        try {
////            InputStream inputStream = AppContext.getInstance().getAssets().open("srca.cer");
////            InputStream[] inputStreams = new InputStream[]{inputStream};
////
////            // 设置https
////            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(inputStreams, null, null);
////            builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
////
////            builder.hostnameVerifier(new HostnameVerifier() {
////                @Override
////                public boolean verify(String s, SSLSession sslSession) {
////                    return true;
////                }
////            });
//////            builder.setDefaultHostnameVerifier
////        }
////        catch (IOException ex)
////        {
////
////        }
//
//        //设置缓存
//        builder.addInterceptor(Api.mRewriteCacheControlInterceptor);
//        builder.addNetworkInterceptor(Api.mRewriteCacheControlInterceptor);
//        builder.cache(cache);
//        //设置超时
//        builder.connectTimeout(Api.CONNECT_TIME_OUT, TimeUnit.SECONDS);
//        builder.readTimeout(Api.READ_TIME_OUT, TimeUnit.SECONDS);
//        builder.writeTimeout(20, TimeUnit.SECONDS);
//        //错误重连
//        builder.retryOnConnectionFailure(true);
//        return builder.build();
    }

    private Retrofit createRetrofit(Retrofit.Builder builder, OkHttpClient client, String url) {
        return builder
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    @ExamUrl
    Retrofit provideTestRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return createRetrofit(builder, client, ExamApis.HOST);
    }

    @Singleton
    @Provides
    ExamApis provideTestService(@ExamUrl Retrofit retrofit) {
        return retrofit.create(ExamApis.class);
    }

}
