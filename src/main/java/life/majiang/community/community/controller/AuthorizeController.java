package life.majiang.community.community.controller;


import life.majiang.community.community.dto.AccessTokenDTO;
import life.majiang.community.community.dto.GithubUser;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.User;
import life.majiang.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessTokon = githubProvider.getAccessTokon(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessTokon);
        if(githubUser != null){

            User user1 = new User();
            user1.setToken(UUID.randomUUID().toString());
            user1.setName(githubUser.getName());
            user1.setAcoundId(String.valueOf(githubUser.getId()));
            user1.setGmtCreate(System.currentTimeMillis());
            user1.setGmtModified(user1.getGmtCreate());
            userMapper.insert(user1);
            //登陆成功，写cookie和session
            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";

        }
        else{
            return "redirect:/";
        }
    }

}
