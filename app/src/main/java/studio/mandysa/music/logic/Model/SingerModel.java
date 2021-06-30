package studio.mandysa.music.logic.Model;

import mandysax.anna2.annotation.Array;
import mandysax.anna2.annotation.Value;

@Array
public class SingerModel {
    @Value("id")
    public String id;

    @Value("name")
    public String name;

    @Value("img1v1Url")//头像
    public String picUrl;
}
