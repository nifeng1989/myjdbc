package net.fengni.jdbc.annotation;

import net.fengni.jdbc.shard.DefaultShardRule;
import net.fengni.jdbc.shard.IShardRule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by fengni on 2015/10/28.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Shard {
    Class<? extends IShardRule> shardRule() default DefaultShardRule.class;

    int size() default 8;
}
