package life.majiang.community.community.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private String acountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;


}
