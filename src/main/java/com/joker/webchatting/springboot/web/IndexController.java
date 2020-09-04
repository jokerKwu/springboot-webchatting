package com.joker.webchatting.springboot.web;

import com.joker.webchatting.springboot.config.auth.LoginUser;
import com.joker.webchatting.springboot.config.auth.dto.SessionUser;
import com.joker.webchatting.springboot.domain.posts.Posts;
import com.joker.webchatting.springboot.service.posts.PostsService;
import com.joker.webchatting.springboot.web.dto.PostDto;
import com.joker.webchatting.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model ,@PageableDefault Pageable pageable, @LoginUser SessionUser user) {

        model.addAttribute("posts", postsService.findAllDesc());


        Page<Posts> postList = postsService.getPostList(pageable);
        model.addAttribute("posts",postList);
        System.out.println("총 엘리먼트 수   : "+ postList.getTotalElements());
        System.out.println("전체 페이지 수   : "+ postList.getTotalPages());
        System.out.println("페이지에 표시할 엘리먼트 수    : "+ postList.getSize());
        System.out.println("현재 페이지 인덱스   : "+ postList.getNumber());

        List<Integer> pages = new ArrayList<>();
        for(int i=0;i<postList.getTotalPages();i++){
            Integer num = new Integer(i+1);
            pages.add(num);
        }
        model.addAttribute("pageNumber",pages);

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

    @GetMapping("/posts")
    public String postView(@PageableDefault Pageable pageable, Model model){
        Page<Posts> postList = postsService.getPostList(pageable);
        model.addAttribute("posts",postList);

        System.out.println("총 엘리먼트 수   : "+ postList.getTotalElements());
        System.out.println("전체 페이지 수   : "+ postList.getTotalPages());
        System.out.println("페이지에 표시할 엘리먼트 수    : "+ postList.getSize());
        System.out.println("현재 페이지 인덱스   : "+ postList.getNumber());

        return "index";
    }

}