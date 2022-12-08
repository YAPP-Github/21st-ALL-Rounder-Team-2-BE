package com.yapp.archiveServer.demo.service;

import com.yapp.archiveServer.demo.domain.DemoMember;
import com.yapp.archiveServer.demo.dto.DemoSignupRequestDto;
import com.yapp.archiveServer.demo.repository.DemoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DemoService {

    private final DemoRepository demoRepository;

    public DemoService(DemoRepository demoRepository) {
        this.demoRepository = demoRepository;
    }

    @Transactional
    public String join(DemoSignupRequestDto dto) {
        validateDuplicateUserName(dto.getName());
        demoRepository.save(dto.toEntity());
        return "회원가입에 성공했습니다.";
    }

    private void validateDuplicateUserName(String name) {
        List<DemoMember> findMembers = demoRepository.findByName(name);
        if (!findMembers.isEmpty())
            throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
}
