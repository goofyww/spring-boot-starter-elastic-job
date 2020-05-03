package com.zcoox.boot.elasticjob.annotation;

import com.zcoox.boot.elasticjob.autoconfigure.ElasticJobAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ElasticJobAutoConfig.class})
public @interface EnableElasticJob {

}