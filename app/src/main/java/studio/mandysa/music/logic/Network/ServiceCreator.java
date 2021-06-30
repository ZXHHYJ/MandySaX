package studio.mandysa.music.logic.Network;

import mandysax.anna2.Anna2;

public class ServiceCreator {
    private static final Anna2 anna = Anna2.Build().baseUrl("http://47.100.93.91:3000/");

    public static <T> T create(Class<T> clazz) {
        return anna.newProxy(clazz);
    }
}
