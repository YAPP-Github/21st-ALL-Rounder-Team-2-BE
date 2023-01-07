package com.yapp.artie.domain.archive.service;

import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.domain.exhibit.ExhibitContents;
import com.yapp.artie.domain.archive.dto.exhibit.PostInfoDto;
import com.yapp.artie.domain.archive.exception.ExhibitNotFoundException;
import com.yapp.artie.domain.archive.exception.NotOwnerOfExhibitException;
import com.yapp.artie.domain.archive.repository.ExhibitRepository;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExhibitService {

  private final ExhibitRepository exhibitRepository;
  private final UserService userService;

  public PostInfoDto getExhibitInformation(Long id, Long userId) {
    User user = userService.findById(userId).get();
    Exhibit exhibit = exhibitRepository.findExhibitEntityGraphById(id);

    validateExhibitFound(exhibit);
    validateOwnedByUser(exhibit, user);

    ExhibitContents contents = exhibit.contents();
    return new PostInfoDto(exhibit.getId(), contents.getName(),
        contents.getDate(), exhibit.isDraft());
  }

  private void validateExhibitFound(Exhibit exhibit) {
    if (exhibit == null) {
      throw new ExhibitNotFoundException();
    }
  }

  private void validateOwnedByUser(Exhibit exhibit, User user) {
    if (!exhibit.ownedBy(user)) {
      throw new NotOwnerOfExhibitException();
    }
  }
}
