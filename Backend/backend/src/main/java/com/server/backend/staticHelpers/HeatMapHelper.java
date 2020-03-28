package com.server.backend.staticHelpers;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.server.backend.Event;
import com.server.backend.GroupMembership;
import com.server.backend.GroupMembershipRepository;
import com.server.backend.HeatMapDay;
import com.server.backend.HeatMapDayRepository;

public class HeatMapHelper {

	/**
	 * Updates heat map for the adding of an event.
	 * @param groupMemRepos A group membership repository.
	 * @param e The event to be added.
	 * @param hmRepo a heat map day repository.
	 */
	public static void addEventHM(GroupMembershipRepository groupMemRepos, Event e, HeatMapDayRepository hmRepo) {
		class EffectedUserGroup {
			int groupId;
			int numUsersEffected;

			public EffectedUserGroup(int groupId) {
				this.groupId = groupId;
				numUsersEffected = 1;
			}

			@Override
			public boolean equals(Object o) {
				if (o.getClass() != EffectedUserGroup.class)
					return false;
				return this.groupId == ((EffectedUserGroup) o).groupId;
			}

			@Override
			public int hashCode() {
				return groupId;
			}
		}
		ArrayList<Integer> usersEffected = new ArrayList<>();
		ArrayList<EffectedUserGroup> groupsEffected = new ArrayList<>();
		Iterable<GroupMembership> gms = groupMemRepos.findAll();
		for (GroupMembership gm : gms) {
			if (gm.getGroup().getId().intValue() == e.getGroup().getId().intValue()) {
				usersEffected.add(gm.getUser().getId());
			}
		}
		for (GroupMembership gm : gms) {
			if (usersEffected.contains(gm.getUser().getId())) {
				boolean found = false;
				for (EffectedUserGroup eug : groupsEffected) {
					if (eug.groupId == gm.getGroup().getId().intValue()) {
						eug.numUsersEffected++;
						found = true;
					}
				}
				if (!found)
					groupsEffected.add(new EffectedUserGroup(gm.getGroup().getId().intValue()));
			}
		}
		for (HeatMapDay hmd : hmRepo.findAll()) {
			if ((hmd.getDate().getTime() == (e.getEnd().getTime() - (e.getEnd().getTime() % 86400000)))
					&& (hmd.getDate().getTime() == (e.getStart().getTime() - e.getStart().getTime() % 86400000))) {
				int[] usersBusy = hmd.getUsersBusy();
				for (EffectedUserGroup eug : groupsEffected) {
					if (eug.groupId == hmd.getGroup().getId().intValue()) {
						for (int i = 0; i < 96; i++) {
							Timestamp tsStart = e.getStart();
							Timestamp tsEnd = e.getEnd();
							if (15 * (i + 1) > (tsStart.getTime() % 86400000) / 60000
									&& 15 * i < (tsEnd.getTime() % 86400000) / 60000) {
								usersBusy[i] = usersBusy[i] + eug.numUsersEffected;
							}
						}
						break;
					}
				}
				hmd.setUsersBusy(usersBusy);
				hmRepo.save(hmd);
			}
		}
	}

	/**
	 * Updates heat map for the deleting of an event.
	 * @param groupMemRepos A group membership repository.
	 * @param e The event to be added.
	 * @param hmRepo a heat map day repository.
	 */
	public static void deleteEventHM(GroupMembershipRepository groupMemRepos, Event e, HeatMapDayRepository hmRepo) {

		class EffectedUserGroup {
			int groupId;
			int numUsersEffected;

			public EffectedUserGroup(int groupId) {
				this.groupId = groupId;
				numUsersEffected = 1;
			}

			@Override
			public boolean equals(Object o) {
				if (o.getClass() != EffectedUserGroup.class)
					return false;
				return this.groupId == ((EffectedUserGroup) o).groupId;
			}

			@Override
			public int hashCode() {
				return groupId;
			}
		}
		ArrayList<Integer> usersEffected = new ArrayList<>();
		ArrayList<EffectedUserGroup> groupsEffected = new ArrayList<>();
		Iterable<GroupMembership> gms = groupMemRepos.findAll();
		for (GroupMembership gm : gms) {
			if (gm.getGroup().getId().intValue() == e.getGroup().getId().intValue()) {
				usersEffected.add(gm.getUser().getId());
			}
		}
		for (GroupMembership gm : gms) {
			if (usersEffected.contains(gm.getUser().getId())) {
				boolean found = false;
				for (EffectedUserGroup eug : groupsEffected) {
					if (eug.groupId == gm.getGroup().getId().intValue()) {
						eug.numUsersEffected++;
						found = true;
					}
				}
				if (!found)
					groupsEffected.add(new EffectedUserGroup(gm.getGroup().getId().intValue()));
			}
		}
		for (HeatMapDay hmd : hmRepo.findAll()) {
			if ((hmd.getDate().getTime() == (e.getEnd().getTime() - (e.getEnd().getTime() % 86400000)))
					&& (hmd.getDate().getTime() == (e.getStart().getTime() - e.getStart().getTime() % 86400000))) {
				int[] usersBusy = hmd.getUsersBusy();
				for (EffectedUserGroup eug : groupsEffected) {
					if (eug.groupId == hmd.getGroup().getId().intValue()) {
						for (int i = 0; i < 96; i++) {
							Timestamp tsStart = e.getStart();
							Timestamp tsEnd = e.getEnd();
							if (15 * (i + 1) > (tsStart.getTime() % 86400000) / 60000
									&& 15 * i < (tsEnd.getTime() % 86400000) / 60000) {
								usersBusy[i] = usersBusy[i] - eug.numUsersEffected;
							}
						}
						break;
					}
				}
				hmd.setUsersBusy(usersBusy);
				hmRepo.save(hmd);
			}
		}
	}

	/**
	 * Updates heat map for the update of an event.
	 * @param groupMemRepos A group membership repository.
	 * @param e The event to be added.
	 * @param eLast The event before it was updated.
	 * @param hmRepo a heat map day repository.
	 */
	public static void updateEventHM(GroupMembershipRepository groupMemRepos, Event e, Event eLast,
			HeatMapDayRepository hmRepo) {
		class EffectedUserGroup {
			int groupId;
			int numUsersEffected;

			public EffectedUserGroup(int groupId) {
				this.groupId = groupId;
				numUsersEffected = 1;
			}

			@Override
			public boolean equals(Object o) {
				if (o.getClass() != EffectedUserGroup.class)
					return false;
				return this.groupId == ((EffectedUserGroup) o).groupId;
			}

			@Override
			public int hashCode() {
				return groupId;
			}
		}
		ArrayList<Integer> usersEffected = new ArrayList<>();
		ArrayList<EffectedUserGroup> groupsEffected = new ArrayList<>();
		Iterable<GroupMembership> gms = groupMemRepos.findAll();
		for (GroupMembership gm : gms) {
			if (gm.getGroup().getId().intValue() == e.getGroup().getId().intValue()) {
				usersEffected.add(gm.getUser().getId());
			}
		}
		for (GroupMembership gm : gms) {
			if (usersEffected.contains(gm.getUser().getId())) {
				boolean found = false;
				for (EffectedUserGroup eug : groupsEffected) {
					if (eug.groupId == gm.getGroup().getId().intValue()) {
						eug.numUsersEffected++;
						found = true;
					}
				}
				if (!found)
					groupsEffected.add(new EffectedUserGroup(gm.getGroup().getId().intValue()));
			}
		}
		for (HeatMapDay hmd : hmRepo.findAll()) {
			if ((hmd.getDate().getTime() == (eLast.getEnd().getTime() - (eLast.getEnd().getTime() % 86400000))) && (hmd
					.getDate().getTime() == (eLast.getStart().getTime() - eLast.getStart().getTime() % 86400000))) {
				int[] usersBusy = hmd.getUsersBusy();
				for (EffectedUserGroup eug : groupsEffected) {
					if (eug.groupId == hmd.getGroup().getId().intValue()) {
						for (int i = 0; i < 96; i++) {
							Timestamp tsStart = eLast.getStart();
							Timestamp tsEnd = eLast.getEnd();
							if (15 * (i + 1) > (tsStart.getTime() % 86400000) / 60000
									&& 15 * i < (tsEnd.getTime() % 86400000) / 60000) {
								usersBusy[i] = usersBusy[i] - eug.numUsersEffected;
							}
						}
						break;
					}
				}
				hmd.setUsersBusy(usersBusy);
				hmRepo.save(hmd);
			}
		}
		boolean hmFound = false;
		for (HeatMapDay hmd : hmRepo.findAll()) {
			if ((hmd.getDate().getTime() == (e.getEnd().getTime() - (e.getEnd().getTime() % 86400000)))
					&& (hmd.getDate().getTime() == (e.getStart().getTime() - e.getStart().getTime() % 86400000))) {
				int[] usersBusy = hmd.getUsersBusy();
				for (EffectedUserGroup eug : groupsEffected) {
					if (eug.groupId == hmd.getGroup().getId().intValue()) {
						for (int i = 0; i < 96; i++) {
							Timestamp tsStart = e.getStart();
							Timestamp tsEnd = e.getEnd();
							if (15 * (i + 1) > (tsStart.getTime() % 86400000) / 60000
									&& 15 * i < (tsEnd.getTime() % 86400000) / 60000) {
								usersBusy[i] = usersBusy[i] + eug.numUsersEffected;
							}
						}
						break;
					}
				}
				hmd.setUsersBusy(usersBusy);
				hmRepo.save(hmd);
				hmFound = true;
			}
		}
		if (!hmFound && (e.getEnd().getTime() - (e.getEnd().getTime() % 86400000)) == (e.getStart().getTime()
				- e.getStart().getTime() % 86400000) && e.getStart() != null) {
			HeatMapDay hmd = new HeatMapDay();
			int[] zeros = new int[96];
			for (int i = 0; i < 96; i++) {
				zeros[i] = 0;
			}
			hmd.setUsersBusy(zeros);
			hmd.setGroup(e.getGroup());
			hmd.setDate(new Date(e.getStart().getTime()));
			hmRepo.save(hmd);
		}

	}
}
