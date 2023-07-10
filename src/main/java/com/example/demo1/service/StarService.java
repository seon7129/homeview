package com.example.demo1.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.demo1.dto.StarDTO;
import com.example.demo1.entity.Member;
import com.example.demo1.entity.Room;
import com.example.demo1.entity.Star;
import com.example.demo1.repository.MemberRepository;
import com.example.demo1.repository.RoomRepository;
import com.example.demo1.repository.StarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StarService {

    private final StarRepository starRepository;
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;

    /*관심목록 추가*/
    @Transactional
    public void starRoom(StarDTO starDTO) throws Exception{
        Member member = memberRepository.findById(starDTO.getMember_id())
                .orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다."));
        Room room = roomRepository.findById(starDTO.getRoom_id())
                .orElseThrow(() -> new NotFoundException("해당 방을 찾을 수 없습니다."));

        if(starRepository.findByMemberAndRoom(member, room).isPresent()){
            throw new Exception();
        }

        Star star = Star.builder()
                .member(member)
                .room(room)
                .build();

        starRepository.save(star);
    }

    /*관심목록 삭제*/
    @Transactional
    public void deleteStar(StarDTO starDTO) throws Exception{
        Member member = memberRepository.findById(starDTO.getMember_id())
                .orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다."));
        Room room = roomRepository.findById(starDTO.getRoom_id())
                .orElseThrow(() -> new NotFoundException("해당 방을 찾을 수 없습니다."));
        Star star = starRepository.findByMemberAndRoom(member, room)
                .orElseThrow(() -> new NotFoundException("해당 관심목록을 찾을 수 없습니다."));

        starRepository.delete(star);

    }

}