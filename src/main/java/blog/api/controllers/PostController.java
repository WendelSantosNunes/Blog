package blog.api.controllers;

import blog.api.domain.post.Post;
import blog.api.domain.user.User;
import blog.api.dto.post.PostRegisterDTO;
import blog.api.services.PostService;
import blog.api.services.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<List<Post>> getPost(){
        return ResponseEntity.ok().body(this.postService.getPost());
    }

    @PostMapping()
    public void userPost(@RequestBody @Valid PostRegisterDTO dados, @RequestHeader("Authorization") String token){
        token =  token.replace("Bearer ", "");

        DecodedJWT jwt = JWT.decode(token);
        String currentEmail = jwt.getSubject();

        User user = userService.getEmailUser(currentEmail);

        Post newPost = new Post(
                dados.title(),
                dados.content(),
                user
        );

        this.postService.registerPost(newPost);
    }
}
