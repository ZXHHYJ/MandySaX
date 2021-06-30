package studio.mandysa.music.logic.Model;

import mandysax.anna2.annotation.Value;

public class UserModel {
    @Value("userId")
    public String userId;

    @Value("nickname")
    public String nickname;

    @Value("avatarUrl")
    public String avatarUrl;

    @Value("signature")
    public String signature;
}
