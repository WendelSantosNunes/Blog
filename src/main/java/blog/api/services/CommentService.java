package blog.api.services;

import blog.api.domain.comment.Comment;
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
}
