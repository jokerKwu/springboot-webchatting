package com.joker.webchatting.springboot.web;

import com.joker.webchatting.springboot.config.auth.LoginUser;
import com.joker.webchatting.springboot.config.auth.dto.SessionUser;
import com.joker.webchatting.springboot.domain.user.User;
import com.joker.webchatting.springboot.service.posts.PostsService;
import com.joker.webchatting.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
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
}