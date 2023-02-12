package com.yapp.artie;

import com.yapp.artie.global.util.DateUtils;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class DateUtilTest {

  @Test
  public void test_local_date_time_동등성_비교() throws Exception {
    //given
    HashMap<LocalDateTime, Integer> hash = new HashMap<>();
    HashSet<LocalDateTime> set = new HashSet<>();

    LocalDateTime test1 = DateUtils.getFirstDayOf(2023, 12);
    LocalDateTime test2 = DateUtils.getFirstDayOf(2023, 12);

    //when
    hash.put(test1, 1);
    hash.put(test2, 1);
    set.add(test1);
    set.add(test2);

    //then
    Assertions.assertThat(hash.size()).isEqualTo(1);
    Assertions.assertThat(test1.equals(test2)).isTrue();
    Assertions.assertThat(test1 == test2).isFalse();
    Assertions.assertThat(set.size()).isEqualTo(1);
  }
}
