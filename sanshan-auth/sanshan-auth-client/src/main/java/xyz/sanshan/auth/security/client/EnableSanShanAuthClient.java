package xyz.sanshan.auth.security.client;

import org.springframework.context.annotation.Import;
import xyz.sanshan.auth.security.client.configuration.AutoConfiguration;

import java.lang.annotation.*;

/**
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AutoConfiguration.class)
@Documented
@Inherited
public @interface EnableSanShanAuthClient {
}
