package blog.api.domain.comment;

import blog.api.domain.post.Post;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name="comments")
@Table(name="comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Comment {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private Post post;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String email_user;

    public Comment(String content, Post post, String email_user) {
        this.content = content;
        this.post = post;
        this.email_user = email_user;
        this.createdAt = LocalDateTime.now();
    }

}
