package xyz.sanshan.auth.security.client.interceptor;

import lombok.extern.java.Log;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import xyz.sanshan.auth.security.client.config.ServiceAuthConfig;
import xyz.sanshan.auth.security.client.jwt.ServiceAuthUtil;
import xyz.sanshan.common.info.PosCodeEnum;

import java.io.IOException;


/**
 * @author ace
 */
@Component
@Log
public class OkHttpTokenInterceptor implements Interceptor {
	@Autowired
	@Lazy
	private ServiceAuthUtil serviceAuthUtil;
	@Autowired
	@Lazy
	private ServiceAuthConfig serviceAuthConfig;


	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
	    Response response = chain.proceed(request);
		if(HttpStatus.FORBIDDEN.value()==response.code()){
			if(response.body().string().contains(String.valueOf(PosCodeEnum.CLIENT_INVALID_CODE))){
				log.info("Client Token Expire,Retry to request...");
				serviceAuthUtil.refreshClientToken();
				Request newRequest = chain.request()
						.newBuilder()
						.header(serviceAuthConfig.getTokenHeader(),serviceAuthUtil.getClientToken())
						.build();
				response = chain.proceed(newRequest);
			}
		}
	    return response;
	}

}
