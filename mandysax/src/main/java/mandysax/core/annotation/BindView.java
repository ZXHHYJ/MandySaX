package mandysax.core.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BindView
{
	int value();
}

