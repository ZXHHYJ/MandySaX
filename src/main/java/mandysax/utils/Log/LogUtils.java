package mandysax.utils.Log;
import android.util.Log;

public class LogUtils {
    /** * 是否开启debug */
    public static boolean isDebug = true;

    /** * 打印错误 * @param clazz * @param msg * @date 2015-09-25 * @time am 09:20 */
    public static void e(Class<?> clazz, String msg) {
        if (isDebug) {
            Log.i(clazz.getSimpleName(), msg + "");
        }
    }

    /** * 打印信息 * @param clazz * @param msg * @date 2015-09-25 * @time am 09:20 */
    public static void i(Class<?> clazz, String msg) {
        if (isDebug) {
            Log.i(clazz.getSimpleName(), msg + "");
        }
    }

    /** * 打印警告 * @param clazz * @param msg * @date 2015-09-25 * @time am 09:20 */
    public static void w(Class<?> clazz, String msg) {
        if (isDebug) {
            Log.w(clazz.getSimpleName(), msg + "");
        }
    }

    /** * 打印debug * @param clazz * @param msg * @date 2015-09-25 * @time am 09:20 */
    public static void d(Class<?> clazz, String msg) {
        if (isDebug) {
            Log.d(clazz.getSimpleName(), msg + "");
        }
    }
}
