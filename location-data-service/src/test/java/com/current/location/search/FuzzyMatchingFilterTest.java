package com.current.location.search;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FuzzyMatchingFilterTest {

  @Test
  void testFuzzyMatch() {
    FuzzyMatchingFilter fuzzyMatchingFilter = new FuzzyMatchingFilter(2);
    List<String> matches = fuzzyMatchingFilter.apply("endys",
        Stream.of("wendys", "Wendy's", "windies", "warbies", "w", "endyse"), s -> s)
        .collect(Collectors.toList());
    Assertions.assertEquals(List.of("wendys", "Wendy's", "endyse"), matches);
  }

  @Test
  void testEquality() {
    FuzzyMatchingFilter fuzzyMatchingFilter = new FuzzyMatchingFilter(0);
    List<String> matches = fuzzyMatchingFilter.apply("endys",
        Stream.of("wendys", "Wendy's", "endys"), s -> s)
        .collect(Collectors.toList());
    Assertions.assertEquals(List.of("endys"), matches);
  }

  @Test
  void testEmpty() {
    FuzzyMatchingFilter fuzzyMatchingFilter = new FuzzyMatchingFilter(10);
    List<String> matches = fuzzyMatchingFilter.apply("endys", Stream.<String>of(), s -> s)
        .collect(Collectors.toList());
    Assertions.assertEquals(matches, List.of());
  }

}
