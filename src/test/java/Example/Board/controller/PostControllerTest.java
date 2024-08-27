package Example.Board.controller;

import Example.Board.domain.Post;
import Example.Board.repository.PostRepository;
import Example.Board.request.PostCreate;
import Example.Board.request.PostEdit;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository repository;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Test
    void test1() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("title")
                .content("content")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("단건 조회")
    void test2() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        repository.save(post);

        //expected
        mockMvc.perform(get("/posts/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("여러개 조회")
    void test3() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1,31)
                .mapToObj(i->Post.builder()
                        .title("제목 " + i)
                        .content("Practice Spring " + i)
                        .build())
                .collect(Collectors.toList());

        repository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0].id").value(requestPosts.get(29).getId()))
                .andDo(print());
    }

    @Test
    @DisplayName("페이지를 0으로 요청해면 첫 페이지를 가져온다")
    void test4() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1,31)
                .mapToObj(i->Post.builder()
                        .title("제목 " + i)
                        .content("Practice Spring " + i)
                        .build())
                .collect(Collectors.toList());

        repository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0].id").value(requestPosts.get(29).getId()))
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test5() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        repository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌맨")
                .content("내용입니다")
                .build();

        //expected
        mockMvc.perform(patch("/posts/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test6() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        repository.save(post);

        //expected
        mockMvc.perform(delete("/posts/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test7() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        repository.save(post);

        //expected
        mockMvc.perform(delete("/posts/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test8() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        repository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌맨")
                .content("내용입니다")
                .build();

        //expected
        mockMvc.perform(patch("/posts/{id}", post.getId()+1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}