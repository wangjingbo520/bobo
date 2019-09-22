package com.zynet.bobo.http;

import com.zynet.bobo.utils.SpUtils;

import java.io.IOException;

import me.jessyan.autosize.utils.LogUtils;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author Bobo
 * @date 2019/9/22 0022
 * describe
 */
public class ReceivedCookiesInterceptor  implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            //解析Cookie
            for (String header : originalResponse.headers("Set-Cookie")) {

                stringBuilder.append(header);


                if(header.contains("JSESSIONID")){

                    String cookie = header.substring(header.indexOf("JSESSIONID"), header.indexOf(";"));
                    SpUtils.SetConfigString("cookie", cookie);
                    LogUtils.d("cookie---qzs   "+cookie);


                }
            }
            LogUtils.d("cookie全部-----   " + stringBuilder.toString());
        }

        return originalResponse;

    }
}
