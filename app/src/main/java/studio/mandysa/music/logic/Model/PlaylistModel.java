package studio.mandysa.music.logic.Model;

import mandysax.anna2.annotation.Array;
import mandysax.anna2.annotation.Value;

@Array
public class PlaylistModel {
    @Value("id")
    public String id;

    @Value("name")
    public String name;

    @Value("copywriter")
    public String info;

    @Value("picUrl")
    public String picUrl;

}
