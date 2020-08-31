package com.joker.webchatting.springboot.web;

import com.joker.webchatting.springboot.config.auth.LoginUser;
import com.joker.webchatting.springboot.config.auth.dto.SessionUser;
import com.joker.webchatting.springboot.service.posts.PostsService;
import com.joker.webchatting.springboot.web.dto.PostDto;
import com.joker.webchatting.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model,@RequestParam(value="page",defaultValue="1")Integer pageNum ,@LoginUser SessionUser user) {
        List<PostDto> postList = postsService.getPostlist(pageNum);
        Integer[] pageList = postsService.getPageList(pageNum);

        model.addAttribute("posts", postsService.findAllDesc());
        model.addAttribute("postList",postList);
        model.addAttribute("pageList",pageList);
        System.out.println("포스트 리스트"+postList);
        System.out.println("페이지리스트 "+ pageList);

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
        System.out.println(dto.getAuthor() +  "     " + user.getName() );
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