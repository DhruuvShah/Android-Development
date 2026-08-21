package com.example.fairnessengine;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FairnessAllocator {

    public static Roommate getNextAssignee(List<Roommate> roommates, List<AssignmentLog> history) {
        return getNextAssigneeForChore(roommates, history, null, -1);
    }

    public static Roommate getNextAssigneeExcluding(List<Roommate> roommates, List<AssignmentLog> history, int excludedRoommateId) {
        return getNextAssigneeForChore(roommates, history, null, excludedRoommateId);
    }

    public static Roommate getNextAssigneeForChore(List<Roommate> roommates, List<AssignmentLog> history, Chore chore, int excludedRoommateId) {
        if (roommates == null || roommates.isEmpty()) return null;

        Set<Integer> eligibleIds = new HashSet<>();
        if (chore != null && chore.eligibleRoommateIds != null && !chore.eligibleRoommateIds.isEmpty()) {
            for (String id : chore.eligibleRoommateIds.split(",")) {
                try { eligibleIds.add(Integer.parseInt(id.trim())); } catch (Exception ignored) {}
            }
        } else {
            for (Roommate r : roommates) eligibleIds.add(r.id);
        }

        List<Roommate> validRoommates = new ArrayList<>();
        for (Roommate r : roommates) {
            if (eligibleIds.contains(r.id) && r.id != excludedRoommateId) {
                validRoommates.add(r);
            }
        }
        
        if (validRoommates.isEmpty()) {
            // If everyone eligible is excluded, just pick one eligible
            for (Roommate r : roommates) {
                if (eligibleIds.contains(r.id)) validRoommates.add(r);
            }
        }
        
        if (validRoommates.isEmpty()) return roommates.get(0); // absolute fallback

        double minEffort = Double.MAX_VALUE;
        for (Roommate r : validRoommates) {
            if (r.cumulativeEffort < minEffort) {
                minEffort = r.cumulativeEffort;
            }
        }

        List<Roommate> candidates = new ArrayList<>();
        for (Roommate r : validRoommates) {
            if (r.cumulativeEffort == minEffort) {
                candidates.add(r);
            }
        }

        if (candidates.size() == 1) return candidates.get(0);

        Roommate best = null;
        long oldestTime = Long.MAX_VALUE;

        for (Roommate r : candidates) {
            long lastAssigned = 0;
            if (history != null) {
                for (AssignmentLog log : history) {
                    if (log.roommateId == r.id && log.dateAssigned > lastAssigned) {
                        lastAssigned = log.dateAssigned;
                    }
                }
            }
            if (lastAssigned < oldestTime) {
                oldestTime = lastAssigned;
                best = r;
            }
        }

        return best != null ? best : candidates.get(0);
    }
}
