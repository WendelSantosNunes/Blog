package blog.api.services;

import blog.api.domain.comment.Comment;
import blog.api.domain.post.Post;
import blog.api.domain.post.exceptions.PostNotFoundException;
import blog.api.dto.comment.CommentRegisterDTO;
import blog.api.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getComment(){
        return this.commentRepository.findAll();
    }

    public void registerComment(Comment dados){
        this.commentRepository.save(dados);
    }

    public Comment getCommentId(Long id){
        return this.commentRepository.findById(id).orElseThrow(
                () -> new PostNotFoundException("Comment not found with id: " + id)
        );
    }

    public void putComment(CommentRegisterDTO dados, Long id){
        Comment currentComment = getCommentId(id);

        currentComment.setContent(dados.content());

        this.commentRepository.save(currentComment);
    }
}
