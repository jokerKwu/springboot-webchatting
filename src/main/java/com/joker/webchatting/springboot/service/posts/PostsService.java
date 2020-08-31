package com.joker.webchatting.springboot.service.posts;


import com.joker.webchatting.springboot.domain.posts.Posts;
import com.joker.webchatting.springboot.domain.posts.PostsRepository;
import com.joker.webchatting.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        postsRepository.delete(posts);
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }
    //키워드 검색
    @Transactional
    public List<PostDto> searchPosts(String keyword) {
        List<Posts> postEntities = postsRepository.findByTitleContaining(keyword);
        List<PostDto> postDtoList = new ArrayList<>();

        if(postEntities.isEmpty()) return postDtoList;

        for(Posts postEntity : postEntities){
            postDtoList.add(this.convertEntityToDto(postEntity));
        }
        return postDtoList;
    }

    private PostDto convertEntityToDto(Posts postEntity){
        return PostDto.builder()
                .author(postEntity.getAuthor())
                .title(postEntity.getTitle())
                .modifiedDate(postEntity.getModifiedDate())
                .id(postEntity.getId())
                .build();
    }


}