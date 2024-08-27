package Example.Board.repository;

import Example.Board.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository  extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
