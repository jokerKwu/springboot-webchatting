package com.joker.webchatting.springboot.web.dto;


import com.joker.webchatting.springboot.domain.posts.Posts;
import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String author;
    private String content;
    private LocalDateTime modifiedDate;

    public Posts toEntity(){
        Posts postEntity = Posts.builder()
                .title(title)
                .author(author)
                .content(content)
                .build();
        return postEntity;
    }

    @Builder
    public PostDto(Long id, String title, String author, LocalDateTime modifiedDate){
        this.id=id;
        this.title=title;
        this.author =author;
        this.modifiedDate=modifiedDate;

    }
}
