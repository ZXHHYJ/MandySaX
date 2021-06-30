package studio.mandysa.music.logic.Model;

import mandysax.anna2.annotation.Array;
import mandysax.anna2.annotation.Value;
import mandysax.anna2.annotation.Path;

@Array
public class UserPlaylistModel {
    @Value("id")
    public String id;

    @Value("name")
    public String name;

    @Value("nickname")
    @Path("creator")
    public String nickname;

    @Value("coverImgUrl")
    public String coverImgUrl;

    @Value("signature")
    public String signature;

}
