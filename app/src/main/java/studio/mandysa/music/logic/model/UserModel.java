package studio.mandysa.music.logic.model;

import mandysax.anna2.annotation.Value;

/**
 * @author liuxiaoliu66
 */
public class UserModel {
    @Value("userId")
    public String userId;

    @Value("nickname")
    public String nickname;

    @Value("avatarUrl")
    public String avatarUrl;

    @Value("signature")
    public String signature;

    @Value("vipType")
    public int vipType;

    @Value("birthday")
    public int birthday;

    @Value("createTime")
    public String createTime;

    //province":450000,"city":451200
}
