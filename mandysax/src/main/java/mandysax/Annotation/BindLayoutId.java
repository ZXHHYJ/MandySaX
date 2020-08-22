package mandysax.Annotation;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import android.renderscript.Type;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindLayoutId {
    int value()
}
