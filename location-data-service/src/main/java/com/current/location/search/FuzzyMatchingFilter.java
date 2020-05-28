package com.current.location.search;

import java.util.function.Function;
import java.util.stream.Stream;
import org.apache.commons.text.similarity.LevenshteinDistance;

public class FuzzyMatchingFilter {
  // NOTE: if the levenshtein distance goes above the threshold, the `apply` method returns this value
  private static final int LEV_DISTANCE_OVERFLOW_VALUE = -1;
  int maxLevenshteinDistance;
  LevenshteinDistance levenshteinDistance;

  public FuzzyMatchingFilter(int maxLevenshteinDistance) {
    this.maxLevenshteinDistance = maxLevenshteinDistance;
    this.levenshteinDistance = new LevenshteinDistance(maxLevenshteinDistance);
  }

  // Filter out all candidate strings that don't fuzzy match with our query string
  public <T> Stream<T> apply(String queryString,
                             Stream<T> candidateStrings,
                             Function<T, String> stringMapper) {
    return candidateStrings.filter(candidate ->
        this.levenshteinDistance.apply(queryString, stringMapper.apply(candidate)) != LEV_DISTANCE_OVERFLOW_VALUE);
  }

}
