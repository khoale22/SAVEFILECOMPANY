package com.heb.pm;

import java.io.Serializable;
import java.util.*;

/**
 * Base class for reporting items found and not found as part of a search.
 *
 * @author d116773
 * @since 2.14.0
 */
public class HitsBase<T extends Comparable> implements Serializable{

	private static final long serialVersionUID = 5586388424766539552L;


	private int matchCount;
	private int noMatchCount;
	private List<T> noMatchList;
	private String hitsType;
	private String hitsTypePlural;

	/**
	 * Constructs a new HitsBase object.
	 *
	 * @param matchCount  The number of items that match the search parameters.
	 * @param noMatchCount The number of items that do not match the search parameters.
	 * @param noMatchList  The list of items that did not match.
	 * @param hitsType The description of the type of objects searched for(for display on the front end).
	 * @param hitsTypePlural A plural wording for the hits type.
	 */
	HitsBase(int matchCount, int noMatchCount, List<T> noMatchList, String hitsType, String hitsTypePlural) {
		this.matchCount = matchCount;
		this.noMatchCount = noMatchCount;
		this.noMatchList = noMatchList;
		this.hitsType = hitsType;
		this.hitsTypePlural = hitsTypePlural;
	}

	/**
	 * Returns the number of items that match the search parameters.
	 *
	 * @return The number of items that match the search parameters.
	 */
	public int getMatchCount() {
		return matchCount;
	}

	/**
	 * Returns the number of items that do not match the search parameters.
	 *
	 * @return The number of items that do not match the search parameters.
	 */
	public int getNoMatchCount() {
		return noMatchCount;
	}

	/**
	 * Returns the list of items that did not match.
	 *
	 * @return The list of items that did not match.
	 */
	public List<T> getNoMatchList() {
		return noMatchList;
	}

	/**
	 * Returns the description of the type of objects searched for(for display on the front end).
	 *
	 * @return The description of the type of objects searched for(for display on the front end).
	 */
	public String getHitsType() {
		return hitsType;
	}

	/**
	 * Returns a plural wording for the hits type.
	 *
	 * @return A plural wording for the hits type.
	 */
	public String getHitsTypePlural() {
		return this.hitsTypePlural;
	}

	/**
	 * Calculates match and not matching search counts and returns a BaseHits object constructed using the
	 * counts parameters.
	 *
	 * @param allList all search input from user
	 * @param matches  matches found in the database
	 * @param hitsType The description of the type of objects searched for(for display on the front end).
	 * @param hitsTypePlural A plural wording for the hits type.
	 * @return A BaseHits object based on the lists provided
	 */
	public static <BT extends Comparable> HitsBase<BT> from(final List<BT> allList, final List<BT> matches,
															String hitsType, String hitsTypePlural) {
		Set<BT> allSet = new HashSet<>(allList);//Convert to Set to remove duplicates
		Set<BT> matchesSet = new HashSet<>(matches);//Convert to Set to remove duplicates
		allSet.removeAll(matchesSet);//Removes all valid matches. Rest in the List/Set is the No-match-found items
		List<BT> nonMatchingList = new ArrayList<>(allSet);
		Collections.sort(nonMatchingList);
		return new HitsBase<>(matchesSet.size(),nonMatchingList.size(), nonMatchingList, hitsType, hitsTypePlural);
	}
}
