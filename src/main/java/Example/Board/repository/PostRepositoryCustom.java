package Example.Board.repository;

import Example.Board.domain.Post;
import Example.Board.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
