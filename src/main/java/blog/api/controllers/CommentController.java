package blog.api.controllers;

import blog.api.domain.comment.Comment;
import blog.api.domain.post.Post;
import blog.api.domain.user.User;
import blog.api.dto.comment.CommentRegisterDTO;
import blog.api.dto.post.PostRegisterDTO;
import blog.api.services.CommentService;
import blog.api.services.PostService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity getComment(){
        return ResponseEntity.ok().body(this.commentService.getComment());
    }

    @PostMapping
    public ResponseEntity postComment(@RequestBody @Valid CommentRegisterDTO dados,
                                      @RequestHeader("Authorization") String token){

        token =  token.replace("Bearer ", "");

        DecodedJWT jwt = JWT.decode(token);
        String currentEmail = jwt.getSubject();

        Post post = postService.getPostId(dados.post_id());

        Comment comment = new Comment(
                dados.content(),
                post,
                currentEmail
        );

        this.commentService.registerComment(comment);

        return ResponseEntity.ok().body("Comentário Cadastrado!");
    }

    @PutMapping("/{id}")
    public ResponseEntity putComment(@RequestBody @Valid CommentRegisterDTO dados,
                           @RequestHeader("Authorization") String token,
                           @PathVariable Long id){

        token =  token.replace("Bearer ", "");

        DecodedJWT jwt = JWT.decode(token);
        String currentEmail = jwt.getSubject();

        Comment putComment = this.commentService.getCommentId(id);

        if(!currentEmail.equals(putComment.getEmail_user())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        this.commentService.putComment(dados, id);

        return ResponseEntity.ok().build();
    }
}
