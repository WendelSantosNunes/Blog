package blog.api.services;

import blog.api.domain.post.Post;
import blog.api.domain.post.exceptions.PostNotFoundException;
import blog.api.domain.user.User;
import blog.api.domain.user.exceptions.UserNotFoundException;
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

    public Post getPostId(Long id){
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("User not found with id: " + id));
    }

    public void registerPost(Post newPost){
        postRepository.save(newPost);
    }

    public Post updatePost(Post updatePost, Post post) {
        post.setContent(updatePost.getContent());
        post.setTitle(updatePost.getTitle());

        return postRepository.save(post);
    }
}
