package Example.Board.controller;

import Example.Board.request.PostCreate;
import Example.Board.request.PostEdit;
import Example.Board.request.PostSearch;
import Example.Board.response.PostResponse;
import Example.Board.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate params) {
        postService.write(params);
    }

    @GetMapping("/posts/{id}")
    public PostResponse getPost(@PathVariable("id") Long id) {
        return postService.findOne(id);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{id}")
    public void edit(@PathVariable(name = "id") Long id, @Valid @RequestBody PostEdit request) {
        postService.edit(id, request);
    }

    @DeleteMapping("/posts/{id}")
    public void delete(@PathVariable("id") Long id) {
        postService.delete(id);
    }
}
