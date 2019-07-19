/*
 *
 * Hits.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm;

import com.heb.util.list.LongPopulator;

import java.io.Serializable;
import java.util.*;

/**
 * Represents the Search result Hits count for the multiple input search criteria. Holds the count of matches found,
 * not found and as well the not found only from the original list.
 *
 * @author vn40486
 * @since 2.0.0
 */
public class Hits extends HitsBase<Long> implements Serializable{
    private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new Hits.
	 *
	 * @param matchCount   the match count
	 * @param noMatchCount the no match count
	 * @param noMatchList  the no match list
	 */
	public Hits(int matchCount, int noMatchCount, List<Long> noMatchList) {
        super(matchCount, noMatchCount, noMatchList, null, null);
    }

    /**
     * Calculates match  and not matching search counts and returns a Hits object constructed using the
     * counts parameters.
     *
     * @param allList all search input from user
     * @param matches  matches found in the database
     * @return hits of match info.
     */
    public static Hits calculateHits(final List<Long> allList, final List<Long> matches) {
        Set<Long> allSet = new HashSet<>(allList);//Convert to Set to remove duplicates
        Set<Long> matchesSet = new HashSet<>(matches);//Convert to Set to remove duplicates
        allSet.removeAll(matchesSet);//Removes all valid matches. Rest in the List/Set is the No-match-found items
        allSet.remove((long) LongPopulator.DEFAULT_VALUE);//Remove the Default Zero objects added by the LongPopulator
        List<Long> nonMatchingList = new ArrayList<>(allSet);
        Collections.sort(nonMatchingList);
        return new Hits(matchesSet.size(),nonMatchingList.size(), nonMatchingList);
    }
}
