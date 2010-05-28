package com.icefaces.project.memory.comparator;

import java.util.Comparator;

import com.icefaces.project.memory.user.UserModel;

/**
 * Comparator used to sort a list of users by their score value.
 */
public class UserByScoreComparator implements Comparator<UserModel> {
	public int compare(UserModel user1, UserModel user2) {
		return ((Integer)user2.getScore()).compareTo(user1.getScore());
	}
}
