package Example.Board.service;

import Example.Board.Exception.PostNotFound;
import Example.Board.domain.Post;
import Example.Board.repository.PostRepository;
import Example.Board.request.PostCreate;
import Example.Board.request.PostEdit;
import Example.Board.request.PostSearch;
import Example.Board.response.PostResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void clear(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        //when
        postService.write(postCreate);

        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다", post.getTitle());
        assertEquals("내용입니다", post.getContent());
    }
    
    @Test
    @DisplayName("단건 조회")
    void test2() {
        //given
        Post post = Post.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        postRepository.save(post);
        //when
        PostResponse post1 = postService.findOne(post.getId());

        //then
        assertNotNull(post);
        assertEquals(1L, postRepository.count());
        assertEquals(post.getTitle(), post1.getTitle());
        assertEquals(post.getContent(), post1.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test3() {
        //given
        List<Post> requestPosts = IntStream.range(1,31)
                .mapToObj(i->Post.builder()
                        .title("제목 " + i)
                        .content("Practice Spring " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        //when
        List<PostResponse> posts = postService.getList(postSearch);

        //then
        assertEquals(10L, posts.size());
        assertEquals("제목 30", posts.get(0).getTitle());
        assertEquals("제목 26", posts.get(4).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test4() {
        //given
        Post post = Post.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌맨")
                .build();

        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post chadngedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다."));

        assertEquals(postEdit.getTitle(), chadngedPost.getTitle());
        assertEquals("내용입니다", chadngedPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test5() {
        //given
        Post post = Post.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();
        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then
        Assertions.assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("단건 조회 - 존재하지 않는 글")
    void test6() {
        //given
        Post post = Post.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        postRepository.save(post);

        //expected
        assertThrows(PostNotFound.class,
                () -> postService.findOne(post.getId() + 1L));

    }
}