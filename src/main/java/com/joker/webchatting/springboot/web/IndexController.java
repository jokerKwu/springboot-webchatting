package com.joker.webchatting.springboot.web;

import com.joker.webchatting.springboot.config.auth.LoginUser;
import com.joker.webchatting.springboot.config.auth.dto.SessionUser;
import com.joker.webchatting.springboot.domain.posts.Posts;
import com.joker.webchatting.springboot.domain.posts.PostsRepository;
import com.joker.webchatting.springboot.service.posts.PostsService;
import com.joker.webchatting.springboot.web.dto.PostDto;
import com.joker.webchatting.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @Autowired
    PostsRepository postsRepository;

    @GetMapping("/")
    public String index(Model model ,@LoginUser SessionUser user) {

        model.addAttribute("posts", postsService.findAllDesc());

        if (user != null) {
            model.addAttribute("name",user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(Model model,@LoginUser SessionUser user) {
        model.addAttribute("name",user.getName());

        return "posts-save";
    }

    @GetMapping("/posts/contents/{id}")
    public String postsUpdate(@PathVariable Long id, Model model,@LoginUser SessionUser user) {
        PostsResponseDto dto = postsService.findById(id);
        if(user.getName().equals(dto.getAuthor())) {
            model.addAttribute("same",user.getName());
        }
        model.addAttribute("post", dto);

        return "posts-contents";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdateComplete(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }

    @GetMapping("/chat")
    public String chat(Model model,@LoginUser SessionUser user){
        model.addAttribute("user",user.getName());
        return "chat";
    }

    @GetMapping("/post/search")
    public String search(@RequestParam(value="keyword")String keyword, Model model, @LoginUser SessionUser user){
        List<PostDto> postDtoList = postsService.searchPosts(keyword);

        model.addAttribute("posts", postDtoList);
        model.addAttribute("same",user.getName());
        return "index";
    }
}