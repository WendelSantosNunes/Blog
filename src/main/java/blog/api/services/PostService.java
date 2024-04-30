package blog.api.services;

import blog.api.domain.post.Post;
import blog.api.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getPost(){
        return postRepository.findAll();
    }

    public void registerPost(Post newPost){
        postRepository.save(newPost);
    }
}
